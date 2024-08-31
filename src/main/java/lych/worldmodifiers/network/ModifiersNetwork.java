package lych.worldmodifiers.network;

import com.mojang.logging.LogUtils;
import lych.worldmodifiers.modifier.Modifier;
import lych.worldmodifiers.modifier.ModifierMap;
import lych.worldmodifiers.modifier.NameToModifierMap;
import lych.worldmodifiers.util.ModifiersHelper;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.slf4j.Logger;

import java.util.Optional;

public final class ModifiersNetwork {
    private ModifiersNetwork() {}

    public static <T> void sendModifierEntryToServer(Modifier<T> modifier, T value) {
        PacketDistributor.sendToServer(new EntryPacket(modifier, value));
    }

    public static <T> void sendModifierEntryToClient(Modifier<T> modifier, T value) {
        PacketDistributor.sendToAllPlayers(new EntryPacket(modifier, value));
    }

    public static void sendModifierMapToServer(ModifierMap map) {
        PacketDistributor.sendToServer(new MapPacket(map));
    }

    public static void sendModifierMapToClient(ModifierMap map) {
        PacketDistributor.sendToAllPlayers(new MapPacket(map));
    }

    public static void sendModifierMapToClient(ServerPlayer player, ModifierMap map) {
        PacketDistributor.sendToPlayer(player, new MapPacket(map));
    }

    public enum PayLoadHandler {
        INSTANCE;

        private static final Logger LOGGER = LogUtils.getLogger();

        @SuppressWarnings("DataFlowIssue")
        public void handleMapData(MapPacket data, IPayloadContext context) {
            context.enqueueWork(() -> {
                Level level = context.player().level();
                if (level.isClientSide()) {
                    ModifiersHelper.reloadModifiersClientside(level, data.map());
                } else {
                    ModifiersHelper.reloadModifiersServerside(level.getServer(), data.map());
                }
                LOGGER.debug("Received modifier map packet with value {}", data.map());
            }).exceptionally(e -> {
                LOGGER.error("Error handling modifier map packet", e);
                return null;
            });
        }

        @SuppressWarnings("DataFlowIssue")
        public void handleEntryData(EntryPacket data, IPayloadContext context) {
            context.enqueueWork(() -> {
                Level level = context.player().level();
                if (level.isClientSide()) {
                    forceSyncModifierValueClientside(level, data.modifier(), data.value());
                } else {
                    forceSyncModifierValueServerside(level.getServer(), data.modifier(), data.value());
                }
                LOGGER.debug("Received modifier entry packet with modifier {} and value {}", data.modifier(), data.value());
            }).exceptionally(e -> {
                LOGGER.error("Error handling modifier entry packet", e);
                return null;
            });
        }

        @SuppressWarnings("unchecked")
        private static <T> void forceSyncModifierValueClientside(Level level, Modifier<T> modifier, Object value) {
            ModifiersHelper.syncModifierValueClientside(level, modifier, (T) value);
        }

        @SuppressWarnings("unchecked")
        private static <T> void forceSyncModifierValueServerside(MinecraftServer server, Modifier<T> modifier, Object value) {
            ModifiersHelper.syncModifierValueServerside(server, modifier, (T) value);
        }
    }

    public record MapPacket(ModifierMap map) implements CustomPacketPayload {
        private static final String NAME = "modifier_map_packet";
        public static final Type<MapPacket> TYPE = new Type<>(ModNetworking.bidirectional(NAME));
        public static final StreamCodec<FriendlyByteBuf, MapPacket> STREAM_CODEC = StreamCodec.of((buf, packet) -> packet.write(buf), MapPacket::new);

        public MapPacket(FriendlyByteBuf buf) {
            this(ModifierMap.deserializeFromNetwork(buf));
        }

        public void write(FriendlyByteBuf buf) {
            map.serializeToNetwork(buf);
        }

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

    public record EntryPacket(Modifier<?> modifier, Object value) implements CustomPacketPayload {
        private static final String NAME = "modifier_entry_packet";
        public static final CustomPacketPayload.Type<EntryPacket> TYPE = new CustomPacketPayload.Type<>(ModNetworking.bidirectional(NAME));
        public static final StreamCodec<FriendlyByteBuf, EntryPacket> STREAM_CODEC = StreamCodec.of((buf, packet) -> packet.write(buf), EntryPacket::read);

        public void write(FriendlyByteBuf buf) {
            forceWrite(buf, modifier, value);
        }

        public static EntryPacket read(FriendlyByteBuf buf) {
            String name = buf.readUtf();
            Optional<Modifier<?>> modifierOptional = NameToModifierMap.byName(name);
            if (modifierOptional.isEmpty()) {
                throw new IllegalStateException("Modifier %s not found".formatted(name));
            }
            Modifier<?> modifier = modifierOptional.get();
            Object value = modifier.deserializeFromNetwork(buf);
            return new EntryPacket(modifier, value);
        }

        @SuppressWarnings("unchecked")
        public static <T> void forceWrite(FriendlyByteBuf buf, Modifier<T> modifier, Object value) {
            buf.writeUtf(modifier.getName().toString());
            modifier.serializeToNetwork((T) value, buf);
        }

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }
}
