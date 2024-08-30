package lych.worldmodifiers.util.mixin;

import lych.worldmodifiers.modifier.ModifierMap;

public interface IMinecraftServerMixin {
    ModifierMap worldModifiers$getModifierMap();

    void worldModifiers$saveModifierMap();
}
