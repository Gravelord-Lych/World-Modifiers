package lych.worldmodifiers.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
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
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

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

    @Inject(method = "getDefaultNumSpawns", at = @At("HEAD"), cancellable = true)
    private void worldModifiers$fixBaseSpawnCount(Raid.RaiderType raiderType, int wave, boolean shouldSpawnBonusGroup, CallbackInfoReturnable<Integer> cir) {
        if (shouldSpawnBonusGroup) {
            if (numGroups >= ((RaiderTypeAccessor) (Object) raiderType).getSpawnsPerWaveBeforeBonus().length) {
                cir.setReturnValue(0);
            }
        } else {
            if (wave >= ((RaiderTypeAccessor) (Object) raiderType).getSpawnsPerWaveBeforeBonus().length) {
                cir.setReturnValue(0);
            }
        }
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

    @Inject(method = "getPotentialBonusSpawns", at = @At("HEAD"), cancellable = true)
    private void worldModifiers$modifyMaxBonusSpawnOfWave89(Raid.RaiderType raiderType, RandomSource random, int wave, DifficultyInstance difficulty, boolean shouldSpawnBonusGroup, CallbackInfoReturnable<Integer> cir) {
        int maxBonusSpawn = DifficultyHelper.updateMaxRaidBonusSpawn(wave, shouldSpawnBonusGroup, raiderType);
        // If true, the raiderType cannot be a raider added by this mod, so do not spawn bonus raiders
        if (wave > 7 && maxBonusSpawn < 0) {
            cir.setReturnValue(0);
        }
        if (maxBonusSpawn >= 0) {
            cir.setReturnValue(maxBonusSpawn > 0 ? random.nextInt(maxBonusSpawn + 1) : 0);
        }
    }
}
