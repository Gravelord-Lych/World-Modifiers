package lych.worldmodifiers.modifier;

import lych.worldmodifiers.api.modifier.category.ModifierCategory;

import javax.annotation.Nullable;

@FunctionalInterface
public interface ModifierEntryVisitor<T> {
    boolean visitEntry(int depth, @Nullable ModifierCategory parent, T entry);
}
