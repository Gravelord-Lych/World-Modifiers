package lych.worldmodifiers.mixin.client;

import lych.worldmodifiers.modifier.ModifierMap;
import lych.worldmodifiers.util.mixin.IAdditionalClientLevelData;
import net.minecraft.client.multiplayer.ClientLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ClientLevel.ClientLevelData.class)
public class ClientLevelDataMixin implements IAdditionalClientLevelData {
    @Unique
    private final ModifierMap worldModifiers$synchedModifiers = new ModifierMap();
    @Unique
    private boolean worldModifiers$extremeDifficulty;

    @Override
    public boolean worldModifiers$isExtremeDifficulty() {
        return worldModifiers$extremeDifficulty;
    }

    @Override
    public void worldModifiers$setExtremeDifficulty(boolean extremeDifficulty) {
        this.worldModifiers$extremeDifficulty = extremeDifficulty;
    }

    @Override
    public ModifierMap worldModifiers$getSynchedModifiers() {
        return worldModifiers$synchedModifiers;
    }

    @Override
    public void worldModifiers$reloadSynchedModifiers(ModifierMap synchedModifiers) {
        worldModifiers$synchedModifiers.reloadFrom(synchedModifiers);
    }
}
