package lych.worldmodifiers.network;

import lych.worldmodifiers.WorldModifiers;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = WorldModifiers.MODID, bus = EventBusSubscriber.Bus.MOD)
public final class ModNetworking {
    private ModNetworking() {}

    @SubscribeEvent
    public static void register(RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(WorldModifiers.MODID);
        registrar.playBidirectional(ExtremeDifficultyNetwork.Packet.TYPE,
                ExtremeDifficultyNetwork.Packet.STREAM_CODEC,
                ExtremeDifficultyNetwork.PayloadHandler.INSTANCE::handleData);
    }

    public static ResourceLocation bidirectional(String name) {
        return WorldModifiers.prefix(name);
    }

    public static ResourceLocation clientbound(String name) {
        return WorldModifiers.prefix(name + "_clientbound");
    }

    public static ResourceLocation serverbound(String name) {
        return WorldModifiers.prefix(name + "_serverbound");
    }
}
