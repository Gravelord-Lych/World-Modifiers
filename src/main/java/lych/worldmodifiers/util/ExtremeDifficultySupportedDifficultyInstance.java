package lych.worldmodifiers.util;

import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;

import javax.annotation.concurrent.Immutable;

@Immutable
public class ExtremeDifficultySupportedDifficultyInstance extends DifficultyInstance {
    public ExtremeDifficultySupportedDifficultyInstance(Difficulty base, long levelTime, long chunkInhabitedTime, float moonPhaseFactor) {
        super(base, levelTime, chunkInhabitedTime, moonPhaseFactor);
        if (base != Difficulty.HARD) {
            throw new IllegalStateException("Invalid difficulty: " + base + " for ExtremeDifficultySupportedDifficultyInstance");
        }
    }

    @Override
    public float calculateDifficulty(Difficulty difficulty, long levelTime, long chunkInhabitedTime, float moonPhaseFactor) {
        // The regional difficulty hasn't been increased by a large amount here because the clamped regional difficulty
        // is already very high after the modification of the regional difficulty in extreme difficulty.
        return super.calculateDifficulty(difficulty, levelTime, chunkInhabitedTime, moonPhaseFactor)
                * (Difficulty.HARD.getId() + 1) / Difficulty.HARD.getId();
    }
}
