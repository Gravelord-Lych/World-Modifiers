package lych.worldmodifiers.modifier;

import lych.worldmodifiers.WorldModifiersMod;
import lych.worldmodifiers.api.modifier.IntModifierBuilder;
import lych.worldmodifiers.api.modifier.Modifier;
import lych.worldmodifiers.api.modifier.ModifierNames;
import lych.worldmodifiers.api.modifier.category.ModifierCategory;
import lych.worldmodifiers.modifier.category.ModifierCategories;
import lych.worldmodifiers.modifier.texture.ModifierTextureProviders;

public final class Modifiers {
    public static final Modifier<Integer> ARMOR_GENERIC =
            makeArmorModifier(
                    ModifierNames.ARMOR_GENERIC,
                    ModifierNames.ARMOR_TEXTURE,
                    ModifierCategories.MOB_GENERIC
            ).build();
    public static final Modifier<Integer> ARMOR_MONSTERS =
            makeArmorModifier(
                    ModifierNames.ARMOR_MONSTERS,
                    ModifierNames.ARMOR_TEXTURE,
                    ModifierCategories.MONSTER
            ).setGenericModifier(ARMOR_GENERIC).build();
    public static final Modifier<Integer> ARMOR_CREATURES =
            makeArmorModifier(
                    ModifierNames.ARMOR_CREATURES,
                    ModifierNames.ARMOR_TEXTURE,
                    ModifierCategories.CREATURE_GENERIC
            ).setGenericModifier(ARMOR_GENERIC).build();
    public static final Modifier<Integer> ARMOR_ADDITION_GENERIC =
            makeArmorAdditionModifier(
                    ModifierNames.ARMOR_ADDITION_GENERIC,
                    ModifierNames.ARMOR_ADDITION_TEXTURE,
                    ModifierCategories.MOB_GENERIC
            ).build();
    public static final Modifier<Integer> ARMOR_ADDITION_MONSTERS =
            makeArmorAdditionModifier(
                    ModifierNames.ARMOR_ADDITION_MONSTERS,
                    ModifierNames.ARMOR_ADDITION_TEXTURE,
                    ModifierCategories.MONSTER
            ).setGenericModifier(ARMOR_ADDITION_GENERIC).build();
    public static final Modifier<Integer> ARMOR_ADDITION_CREATURES =
            makeArmorAdditionModifier(
                    ModifierNames.ARMOR_ADDITION_CREATURES,
                    ModifierNames.ARMOR_ADDITION_TEXTURE,
                    ModifierCategories.CREATURE_GENERIC
            ).setGenericModifier(ARMOR_ADDITION_GENERIC).build();
    public static final Modifier<Integer> ARMOR_TOUGHNESS_GENERIC =
            makeArmorToughnessModifier(
                    ModifierNames.ARMOR_TOUGHNESS_GENERIC,
                    ModifierNames.ARMOR_TOUGHNESS_TEXTURE,
                    ModifierCategories.MOB_GENERIC
            ).build();
    public static final Modifier<Integer> ARMOR_TOUGHNESS_MONSTERS =
            makeArmorToughnessModifier(
                    ModifierNames.ARMOR_TOUGHNESS_MONSTERS,
                    ModifierNames.ARMOR_TOUGHNESS_TEXTURE,
                    ModifierCategories.MONSTER
            ).setGenericModifier(ARMOR_TOUGHNESS_GENERIC).build();
    public static final Modifier<Integer> ARMOR_TOUGHNESS_CREATURES =
            makeArmorToughnessModifier(
                    ModifierNames.ARMOR_TOUGHNESS_CREATURES,
                    ModifierNames.ARMOR_TOUGHNESS_TEXTURE,
                    ModifierCategories.CREATURE_GENERIC
            ).setGenericModifier(ARMOR_TOUGHNESS_GENERIC).build();
    public static final Modifier<Integer> ARMOR_TOUGHNESS_ADDITION_GENERIC =
            makeArmorToughnessAdditionModifier(
                    ModifierNames.ARMOR_TOUGHNESS_ADDITION_GENERIC,
                    ModifierNames.ARMOR_TOUGHNESS_ADDITION_TEXTURE,
                    ModifierCategories.MOB_GENERIC
            ).build();
    public static final Modifier<Integer> ARMOR_TOUGHNESS_ADDITION_MONSTERS =
            makeArmorToughnessAdditionModifier(
                    ModifierNames.ARMOR_TOUGHNESS_ADDITION_MONSTERS,
                    ModifierNames.ARMOR_TOUGHNESS_ADDITION_TEXTURE,
                    ModifierCategories.MONSTER
            ).setGenericModifier(ARMOR_TOUGHNESS_ADDITION_GENERIC).build();
    public static final Modifier<Integer> ARMOR_TOUGHNESS_ADDITION_CREATURES =
            makeArmorToughnessAdditionModifier(
                    ModifierNames.ARMOR_TOUGHNESS_ADDITION_CREATURES,
                    ModifierNames.ARMOR_TOUGHNESS_ADDITION_TEXTURE,
                    ModifierCategories.CREATURE_GENERIC
            ).setGenericModifier(ARMOR_TOUGHNESS_ADDITION_GENERIC).build();
    public static final Modifier<Integer> MAX_HEALTH_GENERIC =
            makeMaxHealthModifier(
                    ModifierNames.MAX_HEALTH_GENERIC,
                    ModifierNames.MAX_HEALTH_TEXTURE,
                    ModifierCategories.MOB_GENERIC
            ).build();
    public static final Modifier<Integer> MAX_HEALTH_MONSTERS =
            makeMaxHealthModifier(
                    ModifierNames.MAX_HEALTH_MONSTERS,
                    ModifierNames.MAX_HEALTH_TEXTURE,
                    ModifierCategories.MONSTER
            ).setGenericModifier(MAX_HEALTH_GENERIC).build();
    public static final Modifier<Integer> MAX_HEALTH_CREATURES =
            makeMaxHealthModifier(
                    ModifierNames.MAX_HEALTH_CREATURES,
                    ModifierNames.MAX_HEALTH_TEXTURE,
                    ModifierCategories.CREATURE_GENERIC
            ).setGenericModifier(MAX_HEALTH_GENERIC).build();
    public static final Modifier<Integer> MAX_HEALTH_PLAYER = IntModifier.builder(ModifierNames.MAX_HEALTH_PLAYER)
            .setParent(ModifierCategories.PLAYER)
            .setValueRange(5, 2000)
            .setDefaultValue(100)
            .setRepresentsPercentage()
            .setTextureProvider(ModifierTextureProviders.ranged3(100,
                    5,
                    2000,
                    ModifierNames.MAX_HEALTH_TEXTURE).build())
            .build();
    public static final Modifier<Integer> MOVEMENT_SPEED_GENERIC =
            makeMovementSpeedModifier(
                    ModifierNames.MOVEMENT_SPEED_GENERIC,
                    ModifierNames.MOVEMENT_SPEED_TEXTURE,
                    ModifierCategories.MOB_GENERIC
            ).build();
    public static final Modifier<Integer> MOVEMENT_SPEED_MONSTERS =
            makeMovementSpeedModifier(
                    ModifierNames.MOVEMENT_SPEED_MONSTERS,
                    ModifierNames.MOVEMENT_SPEED_TEXTURE,
                    ModifierCategories.MONSTER
            ).setGenericModifier(MOVEMENT_SPEED_GENERIC).build();
    public static final Modifier<Integer> MOVEMENT_SPEED_CREATURES =
            makeMovementSpeedModifier(
                    ModifierNames.MOVEMENT_SPEED_CREATURES,
                    ModifierNames.MOVEMENT_SPEED_TEXTURE,
                    ModifierCategories.CREATURE_GENERIC
            ).setGenericModifier(MOVEMENT_SPEED_GENERIC).build();
    public static final Modifier<Integer> MOVEMENT_SPEED_PLAYER = IntModifier.builder(ModifierNames.MOVEMENT_SPEED_PLAYER)
            .setParent(ModifierCategories.PLAYER)
            .setValueRange(1, 1000)
            .setDefaultValue(100)
            .setRepresentsPercentage()
            .setTextureProvider(ModifierTextureProviders.ranged4(100,
                    1,
                    200,
                    1000,
                    ModifierNames.MOVEMENT_SPEED_TEXTURE).build())
            .build();

    private static boolean initialized = false;

    private Modifiers() {}

    public static void bootstrap() {
        if (initialized) {
            return;
        }
        WorldModifiersMod.LOGGER.info("Loading default world modifiers...");
        initialized = true;
    }

    private static IntModifierBuilder makeArmorModifier(String name, String textureName, ModifierCategory parent) {
        return IntModifier.builder(name)
                .setParent(parent)
                .setValueRange(10, 500)
                .setDefaultValue(100)
                .setRepresentsPercentage()
                .setTextureProvider(ModifierTextureProviders.ranged3(100,
                        10,
                        500,
                        textureName).build());
    }

    private static IntModifierBuilder makeArmorAdditionModifier(String name, String textureName, ModifierCategory parent) {
        return IntModifier.builder(name)
                .setParent(parent)
                .setValueRange(-30, 30)
                .setDefaultValue(0)
                .setTextureProvider(ModifierTextureProviders.ranged5(0,
                        -30,
                        -16,
                        15,
                        30,
                        textureName).build());
    }

    private static IntModifierBuilder makeArmorToughnessModifier(String name, String textureName, ModifierCategory parent) {
        return IntModifier.builder(name)
                .setParent(parent)
                .setValueRange(10, 300)
                .setDefaultValue(100)
                .setRepresentsPercentage()
                .setTextureProvider(ModifierTextureProviders.ranged3(100,
                        10,
                        300,
                        textureName).build());
    }

    private static IntModifierBuilder makeArmorToughnessAdditionModifier(String name, String textureName, ModifierCategory parent) {
        return IntModifier.builder(name)
                .setParent(parent)
                .setValueRange(-20, 20)
                .setDefaultValue(0)
                .setTextureProvider(ModifierTextureProviders.ranged5(0,
                        -20,
                        -11,
                        10,
                        20,
                        textureName).build());
    }

    private static IntModifierBuilder makeMaxHealthModifier(String name, String textureName, ModifierCategory parent) {
        return IntModifier.builder(name)
                .setParent(parent)
                .setValueRange(1, 500)
                .setDefaultValue(100)
                .setRepresentsPercentage()
                .setTextureProvider(ModifierTextureProviders.ranged3(100,
                        1,
                        500,
                        textureName).build());
    }

    private static IntModifierBuilder makeMovementSpeedModifier(String name, String textureName, ModifierCategory parent) {
        return IntModifier.builder(name)
                .setParent(parent)
                .setValueRange(25, 400)
                .setDefaultValue(100)
                .setRepresentsPercentage()
                .setTextureProvider(ModifierTextureProviders.ranged4(100,
                        25,
                        200,
                        400,
                        textureName).build());
    }
}
