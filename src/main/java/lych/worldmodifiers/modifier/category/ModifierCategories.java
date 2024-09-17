package lych.worldmodifiers.modifier.category;

import lych.worldmodifiers.WorldModifiersMod;
import lych.worldmodifiers.api.modifier.category.ModifierCategory;
import lych.worldmodifiers.api.modifier.SortingPriority;

public class ModifierCategories {
    public static final ModifierCategory VANILLA = BaseModifierCategory.createBaseCategoryFor(WorldModifiersMod.MODID, SortingPriority.HIGHEST);
    public static final ModifierCategory ENTITY = VANILLA.addSubCategory("entity");
    public static final ModifierCategory LIVING_ENTITY = ENTITY.addSubCategory("living_entity");
    public static final ModifierCategory PLAYER = LIVING_ENTITY.addSubCategory("player");
    public static final ModifierCategory MOB = LIVING_ENTITY.addSubCategory("mob");
    public static final ModifierCategory MONSTER = MOB.addSubCategory("monster");
    public static final ModifierCategory CREATURE = MOB.addSubCategory("creature");
}
