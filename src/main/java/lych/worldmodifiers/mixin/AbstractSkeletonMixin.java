package lych.worldmodifiers.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import lych.worldmodifiers.util.DifficultyHelper;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AbstractSkeleton.class)
public class AbstractSkeletonMixin {
    @WrapOperation(method = "reassessWeaponGoal", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/monster/AbstractSkeleton;getHardAttackInterval()I"))
    private int worldModifiers$modifyHardAttackInterval(AbstractSkeleton instance, Operation<Integer> original) {
        int originalValue = original.call(instance);
        if (DifficultyHelper.isExtremeDifficulty(instance.level())) {
            return (int) (originalValue * DifficultyHelper.EXTREME_DIFFICULTY_SKELETON_ATTACK_INTERVAL_MULTIPLIER);
        }
        return originalValue;
    }
}
