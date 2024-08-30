package lych.worldmodifiers.network;

import com.mojang.logging.LogUtils;
import lych.worldmodifiers.util.DifficultyHelper;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.slf4j.Logger;

public final class ExtremeDifficultyNetwork {
    private ExtremeDifficultyNetwork() {}

    public static void sendDifficultyToServer(boolean extremeDifficulty) {
        PacketDistributor.sendToServer(new Packet(extremeDifficulty));
    }

    public static void sendDifficultyToClient(boolean extremeDifficulty) {
        PacketDistributor.sendToAllPlayers(new Packet(extremeDifficulty));
    }

    public enum PayloadHandler {
        INSTANCE;

        private static final Logger LOGGER = LogUtils.getLogger();

        public void handleData(Packet data, IPayloadContext context) {
            context.enqueueWork(() -> {
                        DifficultyHelper.setExtremeDifficulty(context.player().level(), data.extremeDifficulty(), false);
                        LOGGER.info("Received packet with value: {}", data.extremeDifficulty());
                    })
                    .exceptionally(e -> {
                        LOGGER.error("Error handling packet", e);
                        return null;
                    });
        }
    }

    public record Packet(boolean extremeDifficulty) implements CustomPacketPayload {
        private static final String NAME = "extreme_difficulty_packet";
        public static final Type<Packet> TYPE = new Type<>(ModNetworking.bidirectional(NAME));
        public static final StreamCodec<FriendlyByteBuf, Packet> STREAM_CODEC = StreamCodec.of((buf, packet) -> packet.write(buf), Packet::new);

        public Packet(FriendlyByteBuf buf) {
            this(buf.readBoolean());
        }

        public void write(FriendlyByteBuf buf) {
            buf.writeBoolean(extremeDifficulty);
        }

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }
}
