package lych.worldmodifiers.api.modifier.texture;

import lych.worldmodifiers.api.APIUtils;
import lych.worldmodifiers.api.modifier.Modifier;
import net.minecraft.resources.ResourceLocation;

import java.util.Comparator;

/**
 * Builder class for {@link ModifierTextureProvider}. The construction result is a texture
 * provider that can provide different textures for different ranges of modifier values.
 * You must ensure that the modifier which uses this texture provider has a value range
 * ({@link Modifier#hasValueRange()} returns {@code true}).
 *
 * @see DynamicModifierTextureProviderBuilder
 * @param <T> the type of the modifier's value
 */
public interface RangedModifierTextureProviderBuilder<T extends Comparable<? super T>> {
    /**
     * Sets the comparator for the modifier's value. The comparator is used to compare the
     * modifier's value with the range bounds. If not set, the natural order of the values
     * will be used.
     *
     * @param valueComparator the comparator
     * @return the builder itself
     */
    default RangedModifierTextureProviderBuilder<T> setComparator(Comparator<? super T> valueComparator) {
        return this;
    }

    /**
     * Binds a texture to a modifier value.
     *
     * @param modifierValue the value of the modifier
     * @param textureLocation  the texture
     * @return the builder itself
     */
    default RangedModifierTextureProviderBuilder<T> addTexture(T modifierValue, ResourceLocation textureLocation) {
        return addTexture(modifierValue, modifierValue, textureLocation);
    }

    /**
     * Binds a texture to a range of modifier value.
     * @param minModifierValue the minimum value (inclusive) of the range
     * @param maxModifierValue the maximum value (inclusive) of the range
     * @param textureLocation the texture
     * @return the builder itself
     */
    default RangedModifierTextureProviderBuilder<T> addTexture(T minModifierValue, T maxModifierValue, ResourceLocation textureLocation) {
        return this;
    }

    /**
     * Builds the {@link ModifierTextureProvider}.
     * @return the built texture provider
     */
    default ModifierTextureProvider<T> build() {
        return modifierValue -> APIUtils.DUMMY_RESOURCE_LOCATION;
    }
}

