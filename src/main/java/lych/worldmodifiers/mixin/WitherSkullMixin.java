package lych.worldmodifiers.mixin;

import lych.worldmodifiers.util.DifficultyHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.entity.projectile.WitherSkull;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(WitherSkull.class)
public class WitherSkullMixin extends AbstractHurtingProjectile {
    private WitherSkullMixin(EntityType<? extends AbstractHurtingProjectile> entityType, Level level) {
        super(entityType, level);
    }

    @ModifyVariable(method = "onHitEntity", at = @At(value = "NEW", target = "(Lnet/minecraft/core/Holder;II)Lnet/minecraft/world/effect/MobEffectInstance;"))
    private int worldModifiers$modifyPoisonDuration(int i) {
        return DifficultyHelper.isExtremeDifficulty(level()) ?
                Math.max(DifficultyHelper.EXTREME_DIFFICULTY_WITHER_SKULL_WITHER_DURATION_SECONDS, i) : i;
    }
}
