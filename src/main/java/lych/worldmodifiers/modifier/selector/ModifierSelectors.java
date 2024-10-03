package lych.worldmodifiers.modifier.selector;

import lych.worldmodifiers.modifier.Modifiers;
import lych.worldmodifiers.modifier.selector.ModifierSelector.Entry;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Enemy;

import java.util.List;

public final class ModifierSelectors {
    public static final ModifierSelector<LivingEntity, Integer> MOB_ARMOR_SELECTOR = new ModifierSelector<>(
            List.of(
                    new Entry<>(entity -> entity instanceof Enemy, Modifiers.ARMOR_MONSTERS),
                    new Entry<>(entity -> entity.getType().getCategory().isFriendly(), Modifiers.ARMOR_CREATURES)
            ),
            Modifiers.ARMOR_GENERIC
    );
    public static final ModifierSelector<LivingEntity, Integer> MOB_ARMOR_ADDITION_SELECTOR = new ModifierSelector<>(
            List.of(
                    new Entry<>(entity -> entity instanceof Enemy, Modifiers.ARMOR_ADDITION_MONSTERS),
                    new Entry<>(entity -> entity.getType().getCategory().isFriendly(), Modifiers.ARMOR_ADDITION_CREATURES)
            ),
            Modifiers.ARMOR_ADDITION_GENERIC
    );
    public static final ModifierSelector<LivingEntity, Integer> MOB_ARMOR_TOUGHNESS_SELECTOR = new ModifierSelector<>(
            List.of(
                    new Entry<>(entity -> entity instanceof Enemy, Modifiers.ARMOR_TOUGHNESS_MONSTERS),
                    new Entry<>(entity -> entity.getType().getCategory().isFriendly(), Modifiers.ARMOR_TOUGHNESS_CREATURES)
            ),
            Modifiers.ARMOR_TOUGHNESS_GENERIC
    );
    public static final ModifierSelector<LivingEntity, Integer> MOB_ARMOR_TOUGHNESS_ADDITION_SELECTOR = new ModifierSelector<>(
            List.of(
                    new Entry<>(entity -> entity instanceof Enemy, Modifiers.ARMOR_TOUGHNESS_ADDITION_MONSTERS),
                    new Entry<>(entity -> entity.getType().getCategory().isFriendly(), Modifiers.ARMOR_TOUGHNESS_ADDITION_CREATURES)
            ),
            Modifiers.ARMOR_TOUGHNESS_ADDITION_GENERIC
    );
    public static final ModifierSelector<LivingEntity, Integer> MOB_MAX_HEALTH_SELECTOR = new ModifierSelector<>(
            List.of(
                    new Entry<>(entity -> entity instanceof Enemy, Modifiers.MAX_HEALTH_MONSTERS),
                    new Entry<>(entity -> entity.getType().getCategory().isFriendly(), Modifiers.MAX_HEALTH_CREATURES)
            ),
            Modifiers.MAX_HEALTH_GENERIC
    );
    public static final ModifierSelector<LivingEntity, Integer> MOB_MOVEMENT_SPEED_SELECTOR = new ModifierSelector<>(
            List.of(
                    new Entry<>(entity -> entity instanceof Enemy, Modifiers.MOVEMENT_SPEED_MONSTERS),
                    new Entry<>(entity -> entity.getType().getCategory().isFriendly(), Modifiers.MOVEMENT_SPEED_CREATURES)
            ),
            Modifiers.MOVEMENT_SPEED_GENERIC
    );

    private ModifierSelectors() {}
}
