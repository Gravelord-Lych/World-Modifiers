package lych.worldmodifiers.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import lych.worldmodifiers.util.DifficultyHelper;
import lych.worldmodifiers.util.ExtremeDifficultySupportedDifficultyInstance;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.DifficultyInstance;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(WorldGenRegion.class)
public abstract class WorldGenRegionMixin {
    @Shadow @Final private ServerLevel level;

    @ModifyReturnValue(method = "getCurrentDifficultyAt", at = @At(value = "RETURN", ordinal = 0))
    private DifficultyInstance worldModifiers$correctlyCalculateRegionalDifficultyForExtremeDifficulty(DifficultyInstance original) {
        return DifficultyHelper.isExtremeDifficulty((WorldGenRegion) (Object) this) ?
                new ExtremeDifficultySupportedDifficultyInstance(level.getDifficulty(), level.getDayTime(), 0L, level.getMoonBrightness()) : original;
    }
}
