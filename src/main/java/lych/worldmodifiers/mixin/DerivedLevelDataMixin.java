package lych.worldmodifiers.mixin;

import lych.worldmodifiers.modifier.ModifierMap;
import lych.worldmodifiers.util.mixin.IAdditionalLevelData;
import net.minecraft.world.level.storage.DerivedLevelData;
import net.minecraft.world.level.storage.ServerLevelData;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(DerivedLevelData.class)
public class DerivedLevelDataMixin implements IAdditionalLevelData {
    @Shadow @Final private ServerLevelData wrapped;

    @Override
    public ModifierMap worldModifiers$getModifiers() {
        return ((IAdditionalLevelData) wrapped).worldModifiers$getModifiers();
    }

    @Override
    public boolean worldModifiers$isExtremeDifficulty() {
        return ((IAdditionalLevelData) wrapped).worldModifiers$isExtremeDifficulty();
    }

    @Override
    public void worldModifiers$setExtremeDifficulty(boolean extremeDifficulty) {
        ((IAdditionalLevelData) wrapped).worldModifiers$setExtremeDifficulty(extremeDifficulty);
    }
}
