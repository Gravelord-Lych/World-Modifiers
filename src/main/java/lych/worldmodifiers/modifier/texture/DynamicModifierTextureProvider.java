package lych.worldmodifiers.modifier.texture;

import com.google.common.collect.ImmutableList;
import lych.worldmodifiers.api.modifier.texture.DynamicModifierTextureProviderBuilder;
import lych.worldmodifiers.api.modifier.texture.ModifierTextureProvider;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

public class DynamicModifierTextureProvider<T> implements ModifierTextureProvider<T> {
    private final List<TexturePair<T>> textures;
    @Nullable
    private final ResourceLocation defaultTexture;

    private DynamicModifierTextureProvider(List<TexturePair<T>> textures, @Nullable ResourceLocation defaultTexture) {
        Objects.requireNonNull(textures);
        if (textures.isEmpty()) {
            throw new IllegalArgumentException("There are no available textures for DynamicModifierTextureProvider");
        }
        this.textures = List.copyOf(textures);
        this.defaultTexture = defaultTexture;
    }

    @Override
    public ResourceLocation getTexture(T modifierValue) {
        for (TexturePair<T> texturePair : textures) {
            if (Objects.equals(texturePair.modifierValue, modifierValue)) {
                return texturePair.textureLocation;
            }
        }
        if (defaultTexture == null) {
            throw new IllegalStateException("No texture found for modifier value: " + modifierValue);
        }
        return defaultTexture;
    }

    private record TexturePair<T>(ResourceLocation textureLocation, T modifierValue) {}

    static class Builder<T> implements DynamicModifierTextureProviderBuilder<T> {
        @Nullable
        private final ResourceLocation defaultTexture;
        private final ImmutableList.Builder<TexturePair<T>> textures = new ImmutableList.Builder<>();

        Builder(@Nullable ResourceLocation defaultTexture) {
            this.defaultTexture = defaultTexture;
        }

        @Override
        public DynamicModifierTextureProviderBuilder<T> addTexture(ResourceLocation textureLocation, T modifierValue) {
            textures.add(new TexturePair<>(textureLocation, modifierValue));
            return this;
        }

        @Override
        public DynamicModifierTextureProvider<T> build() {
            return new DynamicModifierTextureProvider<>(textures.build(), defaultTexture);
        }
    }
}
