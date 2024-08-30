package lych.worldmodifiers.mixin.client;

import lych.worldmodifiers.util.mixin.IWorldCreationUiStateMixin;
import net.minecraft.client.gui.screens.worldselection.WorldCreationUiState;
import net.minecraft.world.Difficulty;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(WorldCreationUiState.class)
public abstract class WorldCreationUiStateMixin implements IWorldCreationUiStateMixin {
    @Shadow public abstract void onChanged();

    @Shadow public abstract Difficulty getDifficulty();

    @Shadow public abstract WorldCreationUiState.SelectedGameMode getGameMode();

    @Unique
    private boolean worldModifiers$extremeDifficulty;

    @Override
    public boolean worldModifiers$isExtremeDifficulty() {
        if (getDifficulty() != Difficulty.HARD) {
            return false;
        }
        if (getGameMode() == WorldCreationUiState.SelectedGameMode.HARDCORE) {
            return true;
        }
        return worldModifiers$extremeDifficulty;
    }

    @Override
    public void worldModifiers$setExtremeDifficulty(boolean extremeDifficulty) {
        this.worldModifiers$extremeDifficulty = extremeDifficulty;
        onChanged();
    }
}
