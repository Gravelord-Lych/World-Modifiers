package lych.worldmodifiers.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import lych.worldmodifiers.util.DifficultyHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Spider.class)
public class SpiderMixin extends Monster {
    private SpiderMixin(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }

    @ModifyExpressionValue(method = "finalizeSpawn", at = @At(value = "CONSTANT", args = "floatValue=0.1F"))
    private float worldModifiers$modifySpiderSpecialEffectChance(float original) {
        if (DifficultyHelper.isExtremeDifficulty(level())) {
            return original * DifficultyHelper.EXTREME_DIFFICULTY_SPIDER_SPECIAL_EFFECT_CHANCE_MULTIPLIER;
        }
        return original;
    }
}
