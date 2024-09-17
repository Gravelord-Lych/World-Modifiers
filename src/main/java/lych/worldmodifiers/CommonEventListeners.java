package lych.worldmodifiers;

import lych.worldmodifiers.modifier.ModifierApplier;
import lych.worldmodifiers.network.ExtremeDifficultyNetwork;
import lych.worldmodifiers.network.ModifierNetwork;
import lych.worldmodifiers.util.DifficultyHelper;
import lych.worldmodifiers.util.IDedicatedServerPropertiesMixin;
import lych.worldmodifiers.util.mixin.IAdditionalLevelData;
import lych.worldmodifiers.util.mixin.IMinecraftServerMixin;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.server.ServerAboutToStartEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;

@EventBusSubscriber(modid = WorldModifiersMod.MODID)
public final class CommonEventListeners {
    private CommonEventListeners() {}

    @SubscribeEvent
    public static void onLevelTick(LevelTickEvent.Post event) {
//        debugExtremeDifficulty(event);
    }

    private static void debugExtremeDifficulty(LevelTickEvent.Post event) {
        if (!event.getLevel().isClientSide() && DifficultyHelper.isExtremeDifficulty(event.getLevel()) && !event.getLevel().players().isEmpty()) {
//            WorldModifiers.LOGGER.info("Level ticked on server side with extreme difficulty!");
            event.getLevel().players().getFirst().addEffect(new MobEffectInstance(MobEffects.GLOWING, 20));
        }
    }

    @SubscribeEvent
    public static void onEntityJoinLevel(EntityJoinLevelEvent event) {
        if (event.getEntity() instanceof Mob mob && !event.getLevel().isClientSide() && !event.loadedFromDisk()) {
            ModifierApplier.applyMaxHealth(mob);
            ModifierApplier.applyMovementSpeed(mob);
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
        if (event.getServer() instanceof DedicatedServer dedicatedServer) {
            ((IAdditionalLevelData) dedicatedServer.getWorldData()).worldModifiers$setExtremeDifficulty(
                            ((IDedicatedServerPropertiesMixin) dedicatedServer.getProperties()).worldModifiers$isExtremeDifficulty());
        }
        ((IMinecraftServerMixin) event.getServer()).worldModifiers$loadModifiers();
        ((IMinecraftServerMixin) event.getServer()).worldModifiers$saveModifiers();
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

    @SuppressWarnings("DataFlowIssue")
    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        ServerPlayer player = (ServerPlayer) event.getEntity();
        ExtremeDifficultyNetwork.sendDifficultyToClient(DifficultyHelper.isExtremeDifficultyInData(player.level().getLevelData()));
        ModifierNetwork.sendModifierMapToClient(player,
                ((IMinecraftServerMixin) player.getServer()).worldModifiers$getStoredModifiers().getModifierMap());
    }
}
