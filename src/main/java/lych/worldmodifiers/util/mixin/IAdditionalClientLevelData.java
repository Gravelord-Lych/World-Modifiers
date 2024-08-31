package lych.worldmodifiers.util.mixin;

import lych.worldmodifiers.modifier.ModifierMap;

public interface IAdditionalClientLevelData extends IAdditionalLevelData {
    ModifierMap worldModifiers$getSynchedModifiers();

    void worldModifiers$reloadSynchedModifiers(ModifierMap synchedModifiers);
}
