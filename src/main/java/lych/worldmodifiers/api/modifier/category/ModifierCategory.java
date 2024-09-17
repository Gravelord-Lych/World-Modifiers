package lych.worldmodifiers.api.modifier.category;

import lych.worldmodifiers.api.WorldModifiersAPI;
import lych.worldmodifiers.api.modifier.BaseModifier;
import lych.worldmodifiers.api.modifier.Modifier;
import lych.worldmodifiers.api.modifier.SortingPriority;

/**
 * Interface for all modifier categories. You should not implement this interface directly, but you can use
 * {@link WorldModifiersAPI#createBaseCategoryFor(String)} to create a base modifier category, and then use
 * {@link ModifierCategory#addSubCategory(String, String, SortingPriority)} to create a sub-category which
 * belongs to the base category.
 */
public interface ModifierCategory extends BaseModifier {
    String NAME = "modifierCategory";

    /**
     * Categorizes a modifier into this category.
     * @param modifier the modifier to categorize
     */
    void addChildModifier(Modifier<?> modifier);

    /**
     * Creates a sub-category and categorizes it into this category.
     * @param name the name of the sub-category
     * @return the newly created sub-category
     */
    default ModifierCategory addSubCategory(String name) {
        return addSubCategory(WorldModifiersAPI.MODID, name);
    }

    /**
     * Creates a sub-category and categorizes it into this category.
     * @param name the name of the sub-category
     * @param priority the sorting priority of the sub-category
     * @return the newly created sub-category
     */
    default ModifierCategory addSubCategory(String name, SortingPriority priority) {
        return addSubCategory(WorldModifiersAPI.MODID, name, priority);
    }

    /**
     * Creates a sub-category and categorizes it into this category.
     * @param namespace the namespace of the sub-category
     * @param name the name of the sub-category
     * @return the newly created sub-category
     */
    default ModifierCategory addSubCategory(String namespace, String name) {
        return addSubCategory(namespace, name, SortingPriority.NORMAL);
    }

    /**
     * Creates a sub-category and categorizes it into this category.
     * @param namespace the namespace of the sub-category
     * @param name the name of the sub-category
     * @param priority the sorting priority of the sub-category
     * @return the newly created sub-category
     */
    ModifierCategory addSubCategory(String namespace, String name, SortingPriority priority);
}
