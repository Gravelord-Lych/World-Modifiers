package lych.worldmodifiers.mixin.client;

import it.unimi.dsi.fastutil.objects.Object2BooleanArrayMap;
import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import lych.worldmodifiers.modifier.ModifierMap;
import lych.worldmodifiers.modifier.category.ModifierCategory;
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
    private final ModifierMap worldModifiers$modifierMap = new ModifierMap();
    @Unique
    private final Object2BooleanMap<ModifierCategory> worldModifiers$foldedStateRecorder = new Object2BooleanArrayMap<>();
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

    @Override
    public ModifierMap worldModifiers$getModifierMap() {
        return worldModifiers$modifierMap;
    }

    @Override
    public void worldModifiers$setModifierMap(ModifierMap modifierMap) {
        this.worldModifiers$modifierMap.reloadFrom(modifierMap);
        onChanged();
    }

    @Override
    public Object2BooleanMap<ModifierCategory> worldModifiers$getFoldedStateRecorder() {
        return worldModifiers$foldedStateRecorder;
    }

    @Override
    public void worldModifiers$setFoldedStateRecorder(Object2BooleanMap<ModifierCategory> foldedRecorder) {
        this.worldModifiers$foldedStateRecorder.clear();
        this.worldModifiers$foldedStateRecorder.putAll(foldedRecorder);
        onChanged();
    }
}
