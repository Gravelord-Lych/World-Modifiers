package lych.worldmodifiers.util.mixin;

import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import lych.worldmodifiers.modifier.ModifierMap;
import lych.worldmodifiers.modifier.category.ModifierCategory;

public interface IWorldCreationUiStateMixin {
    boolean worldModifiers$isExtremeDifficulty();

    void worldModifiers$setExtremeDifficulty(boolean extremeDifficulty);

    ModifierMap worldModifiers$getModifierMap();

    void worldModifiers$setModifierMap(ModifierMap modifierMap);

    Object2BooleanMap<ModifierCategory> worldModifiers$getFoldedStateRecorder();

    void worldModifiers$setFoldedStateRecorder(Object2BooleanMap<ModifierCategory> foldedRecorder);
}
