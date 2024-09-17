package lych.worldmodifiers.api.modifier.texture;

import lych.worldmodifiers.api.APIUtils;
import net.minecraft.resources.ResourceLocation;

/**
 * Builder class for {@link ModifierTextureProvider}. The construction result is a texture
 * provider that can provide different textures for different modifier values.
 *
 * @see RangedModifierTextureProviderBuilder
 * @param <T> the type of the modifier's value
 */
public interface DynamicModifierTextureProviderBuilder<T> {
    /**
     * Binds a texture to a modifier value.
     *
     * @param textureLocation the texture
     * @param modifierValue   the modifier value
     * @return the builder itself
     */
    default DynamicModifierTextureProviderBuilder<T> addTexture(ResourceLocation textureLocation, T modifierValue) {
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
