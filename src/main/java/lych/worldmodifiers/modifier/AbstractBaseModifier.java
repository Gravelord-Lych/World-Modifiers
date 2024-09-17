package lych.worldmodifiers.modifier;

import lych.worldmodifiers.api.modifier.BaseModifier;
import lych.worldmodifiers.api.modifier.SortingPriority;
import lych.worldmodifiers.util.MessageUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;

import java.util.Objects;

public abstract class AbstractBaseModifier implements BaseModifier {
    private final ResourceLocation name;
    private final Component displayName;
    private final SortingPriority priority;

    protected AbstractBaseModifier(String type, String id, String name, SortingPriority priority) {
        Objects.requireNonNull(type, "Modifier type must not be null");
        Objects.requireNonNull(id, "Modifier id must not be null");
        Objects.requireNonNull(name, "Modifier name must not be null");
        this.name = ResourceLocation.fromNamespaceAndPath(id, name);
        this.displayName = Component.translatable(type + "." + id + "." + name);
        Objects.requireNonNull(priority, "Sorting priority must not be null");
        this.priority = priority;
    }

    @Override
    public ResourceLocation getFullName() {
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
    public MutableComponent getDescription() {
        return Component.translatable(MessageUtils.getTranslationKey(getDisplayName()) + ".description");
    }

    @Override
    public MutableComponent getWarning() {
        return Component.translatable(MessageUtils.getTranslationKey(getDisplayName()) + ".warning");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractBaseModifier that = (AbstractBaseModifier) o;
        return Objects.equals(getFullName(), that.getFullName());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getFullName());
    }

    public abstract String toString();
}
