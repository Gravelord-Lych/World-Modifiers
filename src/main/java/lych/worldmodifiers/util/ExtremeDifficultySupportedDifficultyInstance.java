package lych.worldmodifiers.util;

import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;

import javax.annotation.concurrent.Immutable;

@Immutable
public class ExtremeDifficultySupportedDifficultyInstance extends DifficultyInstance {
    public ExtremeDifficultySupportedDifficultyInstance(Difficulty base, long levelTime, long chunkInhabitedTime, float moonPhaseFactor) {
        super(base, levelTime, chunkInhabitedTime, moonPhaseFactor);
        if (base != Difficulty.HARD) {
            throw new IllegalArgumentException("");
        }
    }

    @Override
    public float calculateDifficulty(Difficulty difficulty, long levelTime, long chunkInhabitedTime, float moonPhaseFactor) {
        return super.calculateDifficulty(difficulty, levelTime, chunkInhabitedTime, moonPhaseFactor)
                * (Difficulty.HARD.getId() + 1) / Difficulty.HARD.getId();
    }
}
