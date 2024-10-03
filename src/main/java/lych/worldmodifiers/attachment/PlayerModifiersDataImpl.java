package lych.worldmodifiers.attachment;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;

public class PlayerModifiersDataImpl implements PlayerModifiersData {
    private static final String APPLIED_MODIFIERS_TAG = "AppliedModifiers";
    private boolean appliedModifiers;

    @Override
    public boolean appliedModifiers() {
        return appliedModifiers;
    }

    @Override
    public void setAppliedModifiers(boolean appliedModifiers) {
        this.appliedModifiers = appliedModifiers;
    }

    @Override
    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        tag.putBoolean(APPLIED_MODIFIERS_TAG, appliedModifiers());
        return tag;
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
        setAppliedModifiers(nbt.getBoolean(APPLIED_MODIFIERS_TAG));
    }
}
