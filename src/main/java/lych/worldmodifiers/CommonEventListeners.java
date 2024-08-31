package lych.worldmodifiers;

import lych.worldmodifiers.network.ExtremeDifficultyNetwork;
import lych.worldmodifiers.util.DifficultyHelper;
import lych.worldmodifiers.util.IDedicatedServerPropertiesMixin;
import lych.worldmodifiers.util.mixin.IAdditionalLevelData;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.server.ServerAboutToStartEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;

@EventBusSubscriber(modid = WorldModifiers.MODID)
public final class CommonEventListeners {
    private CommonEventListeners() {}

    @SubscribeEvent
    public static void onLevelTick(LevelTickEvent.Post event) {
        debugExtremeDifficulty(event);
    }

    private static void debugExtremeDifficulty(LevelTickEvent.Post event) {
        if (!event.getLevel().isClientSide() && DifficultyHelper.isExtremeDifficulty(event.getLevel()) && !event.getLevel().players().isEmpty()) {
//            WorldModifiers.LOGGER.info("Level ticked on server side with extreme difficulty!");
            event.getLevel().players().getFirst().addEffect(new MobEffectInstance(MobEffects.GLOWING, 20));
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onLivingIncomingDamage(LivingIncomingDamageEvent event) {
        if (event.getEntity() instanceof Player player) {
            handleExtremeDifficultyDamage(event, player);
            handleExtremeDifficultyStarvationDamage(event, player);
        }
    }

    @SubscribeEvent
    public static void onServerAboutToStart(ServerAboutToStartEvent event) {
        if (event.getServer() instanceof DedicatedServer server) {
            ((IAdditionalLevelData) server.getWorldData()).worldModifiers$setExtremeDifficulty(
                            ((IDedicatedServerPropertiesMixin) server.getProperties()).worldModifiers$isExtremeDifficulty());
        }
    }

    @SuppressWarnings("deprecation")
    private static void handleExtremeDifficultyDamage(LivingIncomingDamageEvent event, Player player) {
        if (event.getSource().scalesWithDifficulty() && DifficultyHelper.isExtremeDifficulty(player.level())) {
            event.setAmount(DifficultyHelper.scaleDamage(event.getAmount()));
        }
    }

    private static void handleExtremeDifficultyStarvationDamage(LivingIncomingDamageEvent event, Player player) {
        if (event.getSource().is(DamageTypes.STARVE) && DifficultyHelper.isExtremeDifficulty(player.level())) {
            event.setAmount(DifficultyHelper.scaleStarvationDamage(event.getAmount()));
        }
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        ExtremeDifficultyNetwork.sendDifficultyToClient(DifficultyHelper.isExtremeDifficultyInData(event.getEntity().level().getLevelData()));
    }
}
