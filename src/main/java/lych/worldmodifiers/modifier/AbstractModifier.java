package lych.worldmodifiers.modifier;

import com.google.common.base.MoreObjects;
import lych.worldmodifiers.api.modifier.BaseModifier;
import lych.worldmodifiers.api.modifier.Modifier;
import lych.worldmodifiers.api.modifier.SortingPriority;
import lych.worldmodifiers.api.modifier.category.ModifierCategory;
import lych.worldmodifiers.api.modifier.texture.ModifierTextureProvider;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.Set;

public abstract class AbstractModifier<T> extends AbstractBaseModifier implements Modifier<T> {
    private final ModifierCategory parent;
    private final ModifierTextureProvider<? super T> textureProvider;

    protected AbstractModifier(String id,
                               String name,
                               ModifierCategory parent,
                               SortingPriority priority,
                               ModifierTextureProvider<? super T> textureProvider) {
        super(Modifier.NAME, id, name, priority);
        Objects.requireNonNull(parent, "Parent category must not be null");
        Objects.requireNonNull(textureProvider, "Texture provider must not be null");
        this.parent = parent;
        this.textureProvider = textureProvider;
    }

    @Override
    public ResourceLocation getTextureLocation(T value) {
        return textureProvider.getTexture(value);
    }

    @Nonnull
    @Override
    public ModifierCategory getParent() {
        return parent;
    }

    @Override
    public final Set<BaseModifier> getChildren() {
        return Set.of();
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("name", getFullName())
                .add("defaultValue", getDefaultValue())
                .toString();
    }
}
