package lych.worldmodifiers.mixin.client;

import lych.worldmodifiers.modifier.ModifierMap;
import lych.worldmodifiers.util.mixin.IAdditionalLevelData;
import net.minecraft.client.multiplayer.ClientLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ClientLevel.ClientLevelData.class)
public class ClientLevelDataMixin implements IAdditionalLevelData {
    @Unique
    private boolean worldModifiers$extremeDifficulty;

    @Override
    public ModifierMap worldModifiers$getModifiers() {
        throw new IllegalStateException("Wrong side");
    }

    @Override
    public boolean worldModifiers$isExtremeDifficulty() {
        return worldModifiers$extremeDifficulty;
    }

    @Override
    public void worldModifiers$setExtremeDifficulty(boolean extremeDifficulty) {
        this.worldModifiers$extremeDifficulty = extremeDifficulty;
    }
}
