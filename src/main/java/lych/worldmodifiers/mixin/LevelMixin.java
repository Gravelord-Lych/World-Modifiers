package lych.worldmodifiers.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import lych.worldmodifiers.util.DifficultyHelper;
import lych.worldmodifiers.util.ExtremeDifficultySupportedDifficultyInstance;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Level.class)
public abstract class LevelMixin implements LevelAccessor {
    @Shadow public abstract long getDayTime();

    @ModifyReturnValue(method = "getCurrentDifficultyAt", at = @At(value = "RETURN", ordinal = 0))
    private DifficultyInstance worldModifiers$correctlyCalculateRegionalDifficultyForExtremeDifficulty(DifficultyInstance original,
                                                                                                       @Local long chunkInhabitedTime,
                                                                                                       @Local float moonPhaseFactor) {
        return DifficultyHelper.isExtremeDifficulty((Level) (Object) this) ?
                new ExtremeDifficultySupportedDifficultyInstance(getDifficulty(), getDayTime(), chunkInhabitedTime, moonPhaseFactor) : original;
    }
}
