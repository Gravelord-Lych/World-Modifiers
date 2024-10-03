package lych.worldmodifiers.modifier;

import lych.worldmodifiers.api.modifier.Modifier;
import lych.worldmodifiers.modifier.selector.ModifierSelectors;
import lych.worldmodifiers.util.ModifierHelper;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;

public final class ModifierApplier {
    private ModifierApplier() {}

    /**
     * Applies all the armor modifiers.
     */
    public static void applyMobArmor(LivingEntity entity) {
        double armorModifierValue = ModifierHelper.getModifierValue(entity.level(), ModifierSelectors.MOB_ARMOR_SELECTOR, entity) / 100.0;
        double armorAdditionModifierValue = ModifierHelper.getModifierValue(entity.level(), ModifierSelectors.MOB_ARMOR_ADDITION_SELECTOR, entity);
        double armorToughnessModifierValue = ModifierHelper.getModifierValue(entity.level(), ModifierSelectors.MOB_ARMOR_TOUGHNESS_SELECTOR, entity) / 100.0;
        double armorToughnessAdditionModifierValue = ModifierHelper.getModifierValue(entity.level(), ModifierSelectors.MOB_ARMOR_TOUGHNESS_ADDITION_SELECTOR, entity);
        modifyAttributeBaseValue(entity, Attributes.ARMOR, armorModifierValue);
        if (armorAdditionModifierValue != 0) {
            getAttribute(entity, Attributes.ARMOR).addPermanentModifier(createAdditiveAttributeModifier(Modifiers.ARMOR_ADDITION_GENERIC, armorAdditionModifierValue));
        }
        modifyAttributeBaseValue(entity, Attributes.ARMOR_TOUGHNESS, armorToughnessModifierValue);
        if (armorToughnessAdditionModifierValue != 0) {
            getAttribute(entity, Attributes.ARMOR_TOUGHNESS).addPermanentModifier(createAdditiveAttributeModifier(Modifiers.ARMOR_TOUGHNESS_ADDITION_GENERIC, armorToughnessAdditionModifierValue));
        }
    }

    /**
     * Applies all the max health modifiers.
     */
    public static void applyMobMaxHealth(LivingEntity entity) {
        double maxHealthModifierValue = ModifierHelper.getModifierValue(entity.level(), ModifierSelectors.MOB_MAX_HEALTH_SELECTOR, entity) / 100.0;
        modifyAttributeBaseValue(entity, Attributes.MAX_HEALTH, maxHealthModifierValue);
    }

    public static void applyPlayerMaxHealth(Player player) {
        double maxHealthModifierValue = ModifierHelper.getModifierValue(player.level(), Modifiers.MAX_HEALTH_PLAYER) / 100.0;
        modifyAttributeBaseValue(player, Attributes.MAX_HEALTH, maxHealthModifierValue);
    }

    /**
     * Applies all the movement speed modifiers.
     */
    public static void applyMobMovementSpeed(LivingEntity entity) {
        double movementSpeedModifierValue = ModifierHelper.getModifierValue(entity.level(), ModifierSelectors.MOB_MOVEMENT_SPEED_SELECTOR, entity) / 100.0;
        modifyAttributeBaseValue(entity, Attributes.MOVEMENT_SPEED, movementSpeedModifierValue);
    }

    public static void applyPlayerMovementSpeed(Player player) {
        double movementSpeedModifierValue = ModifierHelper.getModifierValue(player.level(), Modifiers.MOVEMENT_SPEED_PLAYER) / 100.0;
        modifyAttributeBaseValue(player, Attributes.MOVEMENT_SPEED, movementSpeedModifierValue);
    }

    private static AttributeInstance getAttribute(LivingEntity entity, Holder<Attribute> attribute) {
        AttributeInstance attributeInstance = entity.getAttribute(attribute);
        if (attributeInstance == null) {
            throw new IllegalArgumentException(entity.getClass().getSimpleName() + " does not have the attribute: " + attribute.getRegisteredName());
        }
        return attributeInstance;
    }

    private static AttributeModifier createAdditiveAttributeModifier(Modifier<?> coreModifier, double value) {
        return createAdditiveAttributeModifier(coreModifier.getFullName(), value);
    }

    private static AttributeModifier createAdditiveAttributeModifier(ResourceLocation id, double value) {
        return new AttributeModifier(id, value, AttributeModifier.Operation.ADD_VALUE);
    }

    private static void modifyAttributeBaseValue(LivingEntity entity, Holder<Attribute> attribute, double multiplier) {
        if (multiplier == 1) {
            return;
        }
        AttributeInstance attributeInstance = getAttribute(entity, attribute);
        attributeInstance.setBaseValue(attributeInstance.getBaseValue() * multiplier);
        if (attribute == Attributes.MAX_HEALTH) {
            entity.setHealth(entity.getMaxHealth());
        }
    }
}
