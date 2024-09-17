package lych.worldmodifiers.modifier;

import lych.worldmodifiers.WorldModifiersMod;
import lych.worldmodifiers.api.modifier.Modifier;
import lych.worldmodifiers.api.modifier.ModifierNames;
import lych.worldmodifiers.modifier.category.ModifierCategories;
import lych.worldmodifiers.modifier.texture.ModifierTextureProviders;

public final class Modifiers {
    public static final Modifier<Integer> MAX_HEALTH = IntModifier.builder(ModifierNames.MAX_HEALTH)
            .setParent(ModifierCategories.LIVING_ENTITY)
            .setValueRange(1, 500)
            .setDefaultValue(100)
            .setRepresentsPercentage()
            .setTextureProvider(ModifierTextureProviders.ranged3(100,
                    1,
                    500,
                    ModifierNames.MAX_HEALTH).build())
            .build();
    public static final Modifier<Integer> MOVEMENT_SPEED = IntModifier.builder(ModifierNames.MOVEMENT_SPEED)
            .setParent(ModifierCategories.LIVING_ENTITY)
            .setValueRange(25, 400)
            .setDefaultValue(100)
            .setRepresentsPercentage()
            .setTextureProvider(ModifierTextureProviders.ranged4(100,
                    25,
                    200,
                    400,
                    ModifierNames.MOVEMENT_SPEED).build())
            .build();

    private static boolean initialized = false;

    private Modifiers() {}

    public static void bootstrap() {
        if (initialized) {
            return;
        }
        WorldModifiersMod.LOGGER.info("Loading world modifiers...");
        initialized = true;
    }
}
