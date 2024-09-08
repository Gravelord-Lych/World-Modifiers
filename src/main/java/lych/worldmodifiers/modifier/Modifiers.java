package lych.worldmodifiers.modifier;

import lych.worldmodifiers.modifier.category.ModifierCategories;

public final class Modifiers {
    public static final IntModifier MAX_HEALTH = IntModifier.create("max_health", ModifierCategories.LIVING_ENTITY, 100, 1, 500);
    public static final IntModifier MOVEMENT_SPEED = IntModifier.create("movement_speed", ModifierCategories.LIVING_ENTITY, 100, 10, 1500);

    private Modifiers() {}

    public static void bootstrap() {}
}
