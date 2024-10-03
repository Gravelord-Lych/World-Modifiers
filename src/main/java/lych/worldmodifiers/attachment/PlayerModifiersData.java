package lych.worldmodifiers.attachment;

import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.common.util.INBTSerializable;

/**
 * Attachment for storing player's data added by World Modifiers Mod.
 */
public interface PlayerModifiersData extends INBTSerializable<CompoundTag> {
    /**
     * Returns true if modifiers have been applied to the player, false otherwise.
      * @return true if modifiers have been applied to the player, false otherwise
     */
    boolean appliedModifiers();

    /**
     * Sets whether modifiers have been applied to the player. If true, the modifiers won't be
     * applied to then player again.
     * @param appliedModifiers true if modifiers have been applied to the player, false otherwise
     */
    void setAppliedModifiers(boolean appliedModifiers);
}
