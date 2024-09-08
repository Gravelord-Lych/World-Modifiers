package lych.worldmodifiers.modifier.category;

import lych.worldmodifiers.modifier.SortingPriority;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;

import java.util.Objects;

public abstract class AbstractModifierSetEntry implements ModifierSetEntry {
    private final ResourceLocation name;
    private final Component displayName;
    private final SortingPriority priority;

    protected AbstractModifierSetEntry(String type, String id, String name, SortingPriority priority) {
        this.name = ResourceLocation.fromNamespaceAndPath(id, name);
        this.displayName = Component.translatable(type + "." + id + "." + name);
        this.priority = priority;
    }

    @Override
    public ResourceLocation getName() {
        return name;
    }

    @Override
    public MutableComponent getDisplayName() {
        return displayName.copy();
    }

    @Override
    public SortingPriority getPriority() {
        return priority;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractModifierSetEntry that = (AbstractModifierSetEntry) o;
        return Objects.equals(getName(), that.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getName());
    }

    public abstract String toString();
}
