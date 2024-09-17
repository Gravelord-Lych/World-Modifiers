package lych.worldmodifiers.api.modifier;

import lych.worldmodifiers.api.modifier.category.ModifierCategory;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;
import java.util.Set;

/**
 * The super interface for modifiers and modifier categories.
 */
public interface BaseModifier {
    /**
     * Returns the full name of the modifier or modifier category.
     * @return the full name
     */
    ResourceLocation getFullName();

    /**
     * Returns the display name of the modifier or modifier category.
     * @return the display name
     */
    MutableComponent getDisplayName();

    /**
     * Returns an unmodifiable set containing all children of the modifier category. For modifiers
     * or modifier categories that do not have any children, this method will return an empty set.
     * @return the children
     */
    Set<BaseModifier> getChildren();

    /**
     * Returns the parent category of the modifier or modifier category.
     * @return the parent category, or <code>null</code> if it is a base modifier category.
     */
    @Nullable
    ModifierCategory getParent();

    /**
     * Returns the modifier or modifier category's sorting priority that is used for sorting
     * modifier entries in the edit modifiers screen.
     * @return the sorting priority
     */
    SortingPriority getPriority();

    /**
     * Returns the description of the modifier or modifier category.
     * @return the description
     */
    MutableComponent getDescription();

    /**
     * Returns the warning description text of the modifier or modifier category.
     * Warning will only be displayed if the translation of the description of the
     * modifier or modifier category exists.
     * @return the warning description.
     */
    MutableComponent getWarning();
}
