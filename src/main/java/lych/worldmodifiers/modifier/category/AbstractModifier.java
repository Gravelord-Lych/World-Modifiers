package lych.worldmodifiers.modifier.category;

import com.google.common.base.MoreObjects;
import lych.worldmodifiers.modifier.SortingPriority;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.Set;

public abstract class AbstractModifier<T> extends AbstractModifierSetEntry implements Modifier<T> {
    private final ModifierCategory parent;

    protected AbstractModifier(String id, String name, ModifierCategory parent, SortingPriority priority) {
        super(Modifier.NAME, id, name, priority);
        Objects.requireNonNull(parent);
        this.parent = parent;
    }

    @Nonnull
    @Override
    public ModifierCategory getParent() {
        return parent;
    }

    @Override
    public final Set<ModifierSetEntry> getChildren() {
        return Set.of();
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("name", getName())
                .add("defaultValue", getDefaultValue())
                .toString();
    }
}
