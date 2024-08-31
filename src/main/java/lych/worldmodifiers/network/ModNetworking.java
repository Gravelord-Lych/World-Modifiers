package lych.worldmodifiers.network;

import lych.worldmodifiers.WorldModifiersMod;
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
        registrar.playBidirectional(ModifiersNetwork.MapPacket.TYPE,
                ModifiersNetwork.MapPacket.STREAM_CODEC,
                ModifiersNetwork.PayLoadHandler.INSTANCE::handleMapData);
        registrar.playBidirectional(ModifiersNetwork.EntryPacket.TYPE,
                ModifiersNetwork.EntryPacket.STREAM_CODEC,
                ModifiersNetwork.PayLoadHandler.INSTANCE::handleEntryData);
    }

    public static ResourceLocation bidirectional(String name) {
        return WorldModifiersMod.prefix(name);
    }

    public static ResourceLocation clientbound(String name) {
        return WorldModifiersMod.prefix(name + "_clientbound");
    }

    public static ResourceLocation serverbound(String name) {
        return WorldModifiersMod.prefix(name + "_serverbound");
    }
}
