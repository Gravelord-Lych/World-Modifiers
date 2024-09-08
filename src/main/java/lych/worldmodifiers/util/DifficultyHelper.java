package lych.worldmodifiers.util;

import lych.worldmodifiers.WorldModifiersMod;
import lych.worldmodifiers.enchantment.provider.ModEnchantmentProviders;
import lych.worldmodifiers.network.ExtremeDifficultyNetwork;
import lych.worldmodifiers.util.mixin.IAdditionalLevelData;
import net.minecraft.core.HolderGetter;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.network.chat.Component;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.raid.Raid;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.providers.EnchantmentProvider;
import net.minecraft.world.item.enchantment.providers.SingleEnchantment;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.storage.LevelData;

public final class DifficultyHelper {
    public static final Component EXTREME_DIFFICULTY = WorldModifiersMod.prefixMsg("selectWorld.extremeDifficulty");
    public static final Component EXTREME_DIFFICULTY_INFO = WorldModifiersMod.prefixMsg("selectWorld.extremeDifficulty.info");
    public static final String EXTREME_DIFFICULTY_TAG = "ExtremeDifficulty";
    public static final double OPTION_SCREEN_HEAD_HEIGHT_MULTIPLIER = 1.35;
    public static final float EXTREME_DIFFICULTY_DAMAGE_SCALING_FROM_NORMAL = 2.0F;
    public static final float EXTREME_DIFFICULTY_STARVATION_DAMAGE_SCALING = 2.0F;
    public static final int EXTREME_DIFFICULTY_BEE_POISON_DURATION_SECONDS = 26;
    public static final int EXTREME_DIFFICULTY_CAVE_SPIDER_POISON_DURATION_SECONDS = 25;
    public static final int EXTREME_DIFFICULTY_WITHER_SKULL_WITHER_DURATION_SECONDS = 90;
    public static final double EXTREME_DIFFICULTY_SKELETON_ATTACK_INTERVAL_MULTIPLIER = 0.6;
    public static final float EXTREME_DIFFICULTY_ZOMBIE_HAS_WEAPON_PROBABILITY = 0.15F;
    public static final float EXTREME_DIFFICULTY_SPIDER_SPECIAL_EFFECT_CHANCE_MULTIPLIER = 2F;
    public static final int EXTREME_DIFFICULTY_RAID_WAVES = 9;

    private DifficultyHelper() {}

    public static boolean isExtremeDifficulty(LevelAccessor level) {
        return isExtremeDifficulty(level.getLevelData());
    }

    public static boolean isExtremeDifficulty(LevelData levelData) {
        if (levelData.isHardcore()) {
            return true;
        }
        if (levelData.getDifficulty() != Difficulty.HARD) {
            return false;
        }
        return isExtremeDifficultyInData(levelData);
    }

    public static boolean isExtremeDifficultyInData(LevelData levelData) {
        return ((IAdditionalLevelData) levelData).worldModifiers$isExtremeDifficulty();
    }

    public static void setExtremeDifficulty(Level level, boolean extremeDifficulty) {
        setExtremeDifficulty(level, extremeDifficulty, true);
    }

    public static void setExtremeDifficulty(Level level, boolean extremeDifficulty, boolean needsUpdate) {
        ((IAdditionalLevelData) level.getLevelData()).worldModifiers$setExtremeDifficulty(extremeDifficulty);
        if (needsUpdate) {
            if (level.isClientSide()) {
                ExtremeDifficultyNetwork.sendDifficultyToServer(extremeDifficulty);
            } else {
                ExtremeDifficultyNetwork.sendDifficultyToClient(extremeDifficulty);
            }
        }
    }

    public static int addRaidBonusPillagersAndVindicators(int oldMaxBonus) {
        return oldMaxBonus + 1;
    }

    public static float scaleDamage(float hardDifficultyDamageAmount) {
        return hardDifficultyDamageAmount * EXTREME_DIFFICULTY_DAMAGE_SCALING_FROM_NORMAL * 2F / 3F;
    }

    public static float scaleStarvationDamage(float starvationDamage) {
        return starvationDamage * EXTREME_DIFFICULTY_STARVATION_DAMAGE_SCALING;
    }

    public static float modifyRandomFloatForHigherZombieSpawnWithWeaponsProbability(float originalRandomFloat) {
        return originalRandomFloat * (0.05F / EXTREME_DIFFICULTY_ZOMBIE_HAS_WEAPON_PROBABILITY);
    }

    public static void registerEnchantmentProvidersOfRaiders(BootstrapContext<EnchantmentProvider> context, HolderGetter<Enchantment> holderGetter) {
        context.register(ModEnchantmentProviders.RAID_PILLAGER_POST_WAVE_7, new SingleEnchantment(holderGetter.getOrThrow(Enchantments.QUICK_CHARGE), ConstantInt.of(3)));
        context.register(ModEnchantmentProviders.RAID_VINDICATOR_POST_WAVE_7, new SingleEnchantment(holderGetter.getOrThrow(Enchantments.SHARPNESS), ConstantInt.of(3)));
    }

    public static int updateMaxRaidBonusSpawn(int wave, boolean shouldSpawnBonusGroup, Raid.RaiderType raiderType) {
        if (wave <= 7) {
            return -1;
        }
        int maxBonusSpawn = -1;
        if (raiderType == ExtraRaiderTypes.VINDICATOR89_PROXY.getValue()) {
            maxBonusSpawn = addRaidBonusPillagersAndVindicators(2);
        } else if (raiderType == ExtraRaiderTypes.EVOKER89_PROXY.getValue()) {
            maxBonusSpawn = wave == 8 ? 1 : 0;
        } else if (raiderType == ExtraRaiderTypes.PILLAGER89_PROXY.getValue()) {
            maxBonusSpawn = addRaidBonusPillagersAndVindicators(2);
        } else if (raiderType == ExtraRaiderTypes.WITCH89_PROXY.getValue()) {
            maxBonusSpawn = 1;
        } else if (raiderType == ExtraRaiderTypes.RAVAGER89_PROXY.getValue()) {
            maxBonusSpawn = shouldSpawnBonusGroup ? 1 : 0;
        }
        return maxBonusSpawn;
    }
}
