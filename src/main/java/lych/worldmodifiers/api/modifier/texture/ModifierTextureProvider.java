package lych.worldmodifiers.api.modifier.texture;

import net.minecraft.resources.ResourceLocation;

/**
 * This interface provides a way to get a modifier's texture for displaying.
 * @param <T> the type of the modifier's value.
 */
@FunctionalInterface
public interface ModifierTextureProvider<T> {
    /**
     * The relative path to the folder where the textures are stored.
     */
    String TEXTURE_FOLDER_PATH = "textures/modifiers/";

    /**
     * A convenience method for creating a modifier's texture location.
     *
     * @param id the id of the modifier
     * @param name the name of the modifier
     * @param suffix the suffix of the texture
     * @return the texture location
     */
    static ResourceLocation createTextureLocation(String id, String name, String suffix) {
        return ResourceLocation.fromNamespaceAndPath(id,
                (TEXTURE_FOLDER_PATH + "%s.png").formatted(name + suffix));
    }

    /**
     * Returns the texture for the given modifier value.
     * @param modifierValue the value of the modifier.
     * @return the texture
     */
    ResourceLocation getTexture(T modifierValue);
}
