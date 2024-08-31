package lych.worldmodifiers.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import lych.worldmodifiers.util.DifficultyHelper;
import lych.worldmodifiers.util.ExtremeDifficultySupportedDifficultyInstance;
import net.minecraft.client.gui.components.DebugScreenOverlay;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(DebugScreenOverlay.class)
public class DebugScreenOverlayMixin {
    @WrapOperation(method = "getGameInformation", at = @At(value = "NEW", target = "(Lnet/minecraft/world/Difficulty;JJF)Lnet/minecraft/world/DifficultyInstance;"))
    private DifficultyInstance worldModifiers$correctRegionalDifficulty(Difficulty base, long levelTime, long chunkInhabitedTime, float moonPhaseFactor, Operation<DifficultyInstance> original, @Local Level level) {
        return DifficultyHelper.isExtremeDifficulty(level) ?
                new ExtremeDifficultySupportedDifficultyInstance(base, levelTime, chunkInhabitedTime, moonPhaseFactor) :
                original.call(base, levelTime, chunkInhabitedTime, moonPhaseFactor);
    }
}
