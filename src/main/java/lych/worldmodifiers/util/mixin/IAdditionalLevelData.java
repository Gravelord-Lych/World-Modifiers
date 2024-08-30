package lych.worldmodifiers.util.mixin;

import lych.worldmodifiers.modifier.ModifierMap;

public interface IAdditionalLevelData {
    ModifierMap worldModifiers$getModifiers();

    boolean worldModifiers$isExtremeDifficulty();

    void worldModifiers$setExtremeDifficulty(boolean extremeDifficulty);
}
