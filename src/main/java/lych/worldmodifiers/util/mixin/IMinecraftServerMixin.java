package lych.worldmodifiers.util.mixin;

import lych.worldmodifiers.modifier.StoredModifiers;

public interface IMinecraftServerMixin {
    StoredModifiers worldModifiers$getStoredModifiers();

    void worldModifiers$saveModifiers();

    void worldModifiers$loadModifiers();
}
