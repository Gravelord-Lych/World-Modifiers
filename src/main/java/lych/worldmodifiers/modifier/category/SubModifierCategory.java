package lych.worldmodifiers.modifier.category;

import lych.worldmodifiers.modifier.SortingPriority;

import javax.annotation.Nonnull;
import java.util.Objects;

public final class SubModifierCategory extends AbstractModifierCategory {
    private final ModifierCategory parent;

    SubModifierCategory(String id, String name, SortingPriority priority, ModifierCategory parent) {
        super(id, name, priority);
        Objects.requireNonNull(parent);
        this.parent = parent;
    }

    @Nonnull
    @Override
    public ModifierCategory getParent() {
        return parent;
    }

    @Override
    public String toString() {
        return "ModifierCategory[%s, parent=%s]".formatted(getName(), getParent().getName());
    }
}
