package lych.worldmodifiers.modifier;

import lych.worldmodifiers.api.modifier.BaseModifier;
import lych.worldmodifiers.api.modifier.category.ModifierCategory;

import javax.annotation.Nullable;

@FunctionalInterface
public interface SingleModifierVisitor<T extends BaseModifier> {
    boolean visit(int depth, @Nullable ModifierCategory parent, T baseModifier);
}
