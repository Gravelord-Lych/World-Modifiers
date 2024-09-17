package lych.worldmodifiers.modifier.texture;

import com.google.common.collect.ImmutableList;
import lych.worldmodifiers.api.modifier.texture.ModifierTextureProvider;
import lych.worldmodifiers.api.modifier.texture.RangedModifierTextureProviderBuilder;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class RangedModifierTextureProvider<T extends Comparable<? super T>> implements ModifierTextureProvider<T> {
    private final List<TextureInRange<T>> textures;
    @Nullable
    private final ResourceLocation defaultTexture;
    private final Comparator<? super T> valueComparator;

    private RangedModifierTextureProvider(List<TextureInRange<T>> textures, @Nullable ResourceLocation defaultTexture, Comparator<? super T> valueComparator) {
        Objects.requireNonNull(textures);
        Objects.requireNonNull(valueComparator);
        if (textures.isEmpty()) {
            throw new IllegalArgumentException("There are no available textures for RangeModifierTextureProvider");
        }
        this.textures = List.copyOf(textures);
        this.defaultTexture = defaultTexture;
        this.valueComparator = valueComparator;
    }

    @Override
    public ResourceLocation getTexture(T modifierValue) {
        for (TextureInRange<T> textureInRange : textures) {
            if (textureInRange.matches(modifierValue, valueComparator)) {
                return textureInRange.textureLocation;
            }
        }
        if (defaultTexture == null) {
            throw new IllegalStateException("No texture found for modifier value: " + modifierValue);
        }
        return defaultTexture;
    }

    private record TextureInRange<T>(
            ResourceLocation textureLocation,
            T minModifierValue,
            T maxModifierValue
    ) {
        boolean matches(T modifierValue, Comparator<? super T> valueComparator) {
            return valueComparator.compare(minModifierValue, modifierValue) <= 0 &&
                    valueComparator.compare(maxModifierValue, modifierValue) >= 0;
        }
    }

    static class Builder<T extends Comparable<? super T>> implements RangedModifierTextureProviderBuilder<T> {
        @Nullable
        private final ResourceLocation defaultTexture;
        private final ImmutableList.Builder<TextureInRange<T>> textures = new ImmutableList.Builder<>();
        private Comparator<? super T> valueComparator = Comparator.naturalOrder();

        Builder(@Nullable ResourceLocation defaultTexture) {
            this.defaultTexture = defaultTexture;
        }

        @Override
        public RangedModifierTextureProviderBuilder<T> setComparator(Comparator<? super T> valueComparator) {
            this.valueComparator = valueComparator;
            return this;
        }

        @Override
        public RangedModifierTextureProviderBuilder<T> addTexture(T minModifierValue, T maxModifierValue, ResourceLocation textureLocation) {
            textures.add(new TextureInRange<>(textureLocation, minModifierValue, maxModifierValue));
            return this;
        }

        @Override
        public RangedModifierTextureProvider<T> build() {
            return new RangedModifierTextureProvider<>(textures.build(), defaultTexture, valueComparator);
        }
    }
}
