package lych.worldmodifiers.modifier.category;

import lych.worldmodifiers.api.modifier.SortingPriority;
import lych.worldmodifiers.api.modifier.category.ModifierCategory;
import lych.worldmodifiers.api.modifier.category.ModifierCategoryNames;

public final class ModifierCategories {
    public static final ModifierCategory VANILLA = BaseModifierCategory.createBaseCategoryFor(ModifierCategoryNames.VANILLA, SortingPriority.HIGHEST);
    public static final ModifierCategory ENTITY = VANILLA.addSubCategory(ModifierCategoryNames.ENTITY);
    public static final ModifierCategory LIVING_ENTITY = ENTITY.addSubCategory(ModifierCategoryNames.LIVING_ENTITY);
    // Modifier categories that contain generic modifiers should always have the highest sorting priority.
    public static final ModifierCategory LIVING_ENTITY_GENERIC = LIVING_ENTITY.addSubCategory(ModifierCategoryNames.LIVING_ENTITY_GENERIC, SortingPriority.HIGHEST);
    public static final ModifierCategory PLAYER = LIVING_ENTITY.addSubCategory(ModifierCategoryNames.PLAYER);
    public static final ModifierCategory MOB = LIVING_ENTITY.addSubCategory(ModifierCategoryNames.MOB);
    public static final ModifierCategory MOB_GENERIC = MOB.addSubCategory(ModifierCategoryNames.MOB_GENERIC, SortingPriority.HIGHEST);
    public static final ModifierCategory MONSTER = MOB.addSubCategory(ModifierCategoryNames.MONSTER);
    public static final ModifierCategory CREATURE = MOB.addSubCategory(ModifierCategoryNames.CREATURE);
    public static final ModifierCategory CREATURE_GENERIC = CREATURE.addSubCategory(ModifierCategoryNames.CREATURE_GENERIC, SortingPriority.HIGHEST);
    public static final ModifierCategory ANIMAL = CREATURE.addSubCategory(ModifierCategoryNames.ANIMAL);

    private ModifierCategories() {}
}
