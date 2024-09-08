package lych.worldmodifiers.modifier.category;

import lych.worldmodifiers.WorldModifiersMod;
import lych.worldmodifiers.modifier.SortingPriority;

public interface ModifierCategory extends ModifierSetEntry {
    String NAME = "modifierCategory";

    void addChildModifier(Modifier<?> modifier);

    default ModifierCategory addSubCategory(String name) {
        return addSubCategory(WorldModifiersMod.MODID, name);
    }

    default ModifierCategory addSubCategory(String name, SortingPriority priority) {
        return addSubCategory(WorldModifiersMod.MODID, name, priority);
    }

    default ModifierCategory addSubCategory(String id, String name) {
        return addSubCategory(id, name, SortingPriority.NORMAL);
    }

    ModifierCategory addSubCategory(String id, String name, SortingPriority priority);
}
