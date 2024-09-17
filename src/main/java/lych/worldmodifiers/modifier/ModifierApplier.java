package lych.worldmodifiers.modifier;

import lych.worldmodifiers.api.modifier.Modifier;
import lych.worldmodifiers.util.ModifierHelper;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

import java.util.Optional;

public final class ModifierApplier {
    private ModifierApplier() {}

    /**
     * Applies all the max health modifiers.
     */
    public static void applyMaxHealth(LivingEntity entity) {
        double maxHealthModifierValue = ModifierHelper.getModifierValue(entity.level(), Modifiers.MAX_HEALTH) / 100.0 - 1;
        getAttribute(entity, Attributes.MAX_HEALTH).addPermanentModifier(createMultiplicativeAttributeModifier(Modifiers.MAX_HEALTH, maxHealthModifierValue));
        entity.setHealth(entity.getMaxHealth());
    }

    /**
     * Applies all the movement speed modifiers.
     */
    public static void applyMovementSpeed(LivingEntity entity) {
        double movementSpeedModifierValue = ModifierHelper.getModifierValue(entity.level(), Modifiers.MOVEMENT_SPEED) / 100.0 - 1;
        getAttribute(entity, Attributes.MOVEMENT_SPEED).addPermanentModifier(createMultiplicativeAttributeModifier(Modifiers.MOVEMENT_SPEED, movementSpeedModifierValue));
    }

    private static AttributeInstance getAttribute(LivingEntity entity, Holder<Attribute> attribute) {
        return Optional.ofNullable(entity.getAttribute(attribute)).orElseThrow(() ->
                new IllegalArgumentException(entity.getClass().getSimpleName() + " does not have the attribute: " + attribute.getRegisteredName()));
    }

    private static AttributeModifier createMultiplicativeAttributeModifier(Modifier<?> coreModifier, double value) {
        return createMultiplicativeAttributeModifier(coreModifier.getFullName(), value);
    }

    private static AttributeModifier createMultiplicativeAttributeModifier(ResourceLocation id, double value) {
        return new AttributeModifier(id, value, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
    }
}
