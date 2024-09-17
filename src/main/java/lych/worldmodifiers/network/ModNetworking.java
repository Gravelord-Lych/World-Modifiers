package lych.worldmodifiers.network;

import lych.worldmodifiers.WorldModifiersMod;
import lych.worldmodifiers.util.MessageUtils;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = WorldModifiersMod.MODID, bus = EventBusSubscriber.Bus.MOD)
public final class ModNetworking {
    private ModNetworking() {}

    @SubscribeEvent
    public static void register(RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(WorldModifiersMod.MODID);
        registrar.playBidirectional(ExtremeDifficultyNetwork.Packet.TYPE,
                ExtremeDifficultyNetwork.Packet.STREAM_CODEC,
                ExtremeDifficultyNetwork.PayloadHandler.INSTANCE::handleData);
        registrar.playBidirectional(ModifierNetwork.MapPacket.TYPE,
                ModifierNetwork.MapPacket.STREAM_CODEC,
                ModifierNetwork.PayLoadHandler.INSTANCE::handleMapData);
        registrar.playBidirectional(ModifierNetwork.EntryPacket.TYPE,
                ModifierNetwork.EntryPacket.STREAM_CODEC,
                ModifierNetwork.PayLoadHandler.INSTANCE::handleEntryData);
    }

    public static ResourceLocation bidirectional(String name) {
        return MessageUtils.prefix(name);
    }

    public static ResourceLocation clientbound(String name) {
        return MessageUtils.prefix(name + "_clientbound");
    }

    public static ResourceLocation serverbound(String name) {
        return MessageUtils.prefix(name + "_serverbound");
    }
}
