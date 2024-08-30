package lych.worldmodifiers.mixin;

import lych.worldmodifiers.util.DifficultyHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.CaveSpider;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(CaveSpider.class)
public class CaveSpiderMixin extends Spider {
    public CaveSpiderMixin(EntityType<? extends Spider> entityType, Level level) {
        super(entityType, level);
    }

    @ModifyVariable(method = "doHurtTarget", at = @At(value = "NEW", target = "(Lnet/minecraft/core/Holder;II)Lnet/minecraft/world/effect/MobEffectInstance;"))
    private int worldModifiers$modifyPoisonDuration(int i) {
        return DifficultyHelper.isExtremeDifficulty(level()) ?
                Math.max(DifficultyHelper.EXTREME_DIFFICULTY_CAVE_SPIDER_POISON_DURATION_SECONDS, i) : i;
    }
}
