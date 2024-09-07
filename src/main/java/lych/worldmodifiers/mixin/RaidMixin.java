package lych.worldmodifiers.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import lych.worldmodifiers.util.DifficultyHelper;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.raid.Raid;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(Raid.class)
public abstract class RaidMixin {
    @Shadow protected abstract boolean shouldSpawnBonusGroup();

    @Shadow @Final private int numGroups;

    @Shadow @Final private ServerLevel level;

    @WrapOperation(method = "<init>(ILnet/minecraft/server/level/ServerLevel;Lnet/minecraft/core/BlockPos;)V",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/raid/Raid;getNumGroups(Lnet/minecraft/world/Difficulty;)I"))
    private int worldModifiers$modifyNumGroups(Raid instance, Difficulty difficulty, Operation<Integer> original, @Local(argsOnly = true) ServerLevel level) {
        return original.call(instance, difficulty) + (DifficultyHelper.isExtremeDifficulty(level) ? DifficultyHelper.EXTREME_DIFFICULTY_RAID_WAVES - 7 : 0);
    }

    @WrapMethod(method = "getPotentialBonusSpawns")
    private int worldModifiers$fixBaseSpawnCount(Raid.RaiderType raiderType, RandomSource random, int wave, DifficultyInstance difficultyInstance, boolean shouldSpawnBonusGroup, Operation<Integer> original) {
        if (shouldSpawnBonusGroup) {
            if (numGroups >= ((RaiderTypeAccessor) (Object) raiderType).getSpawnsPerWaveBeforeBonus().length) {
                return 0;
            }
        } else {
            if (wave >= ((RaiderTypeAccessor) (Object) raiderType).getSpawnsPerWaveBeforeBonus().length) {
                return 0;
            }
        }
        return original.call(raiderType, random, wave, difficultyInstance, shouldSpawnBonusGroup);
    }

    @ModifyExpressionValue(method = "getPotentialBonusSpawns",
            at = @At(value = "CONSTANT", args = "intValue=2", ordinal = 0),
            slice = @Slice(from = @At(value = "INVOKE", target = "Lnet/minecraft/util/RandomSource;nextInt(I)I", ordinal = 0)))
    private int worldModifiers$modifyPillagerAndVindicatorMaxBonusSpawn(int i) {
        if (DifficultyHelper.isExtremeDifficulty(level)) {
            return DifficultyHelper.addRaidBonusPillagersAndVindicators(i);
        }
        return i;
    }

    @ModifyReturnValue(method = "getPotentialBonusSpawns", at = @At("RETURN"))
    private int worldModifiers$modifyMaxBonusSpawnOfWave89(int original,
                                                           @Local(argsOnly = true) Raid.RaiderType raiderType,
                                                           @Local(argsOnly = true) RandomSource random,
                                                           @Local(argsOnly = true) int wave,
                                                           @Local(argsOnly = true) boolean shouldSpawnBonusGroup) {
        // If true, the raiderType must be a vanilla raider, so do not spawn bonus raiders
        if (wave > 7 && (raiderType == Raid.RaiderType.VINDICATOR ||
                        raiderType == Raid.RaiderType.EVOKER ||
                        raiderType == Raid.RaiderType.PILLAGER ||
                        raiderType == Raid.RaiderType.WITCH ||
                        raiderType == Raid.RaiderType.RAVAGER)) {
            return 0;
        }
        int maxBonusSpawn = DifficultyHelper.updateMaxRaidBonusSpawn(wave, shouldSpawnBonusGroup, raiderType);
        if (maxBonusSpawn < 0) {
            return original;
        }
        return (maxBonusSpawn > 0 ? random.nextInt(maxBonusSpawn + 1) : 0);
    }
}
