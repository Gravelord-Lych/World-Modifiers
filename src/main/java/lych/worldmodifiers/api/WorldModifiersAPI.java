package lych.worldmodifiers.api;

import com.mojang.logging.LogUtils;
import lych.worldmodifiers.WorldModifiersMod;
import lych.worldmodifiers.api.modifier.IntModifierBuilder;
import lych.worldmodifiers.api.modifier.Modifier;
import lych.worldmodifiers.api.modifier.category.ModifierCategory;
import lych.worldmodifiers.api.modifier.texture.DynamicModifierTextureProviderBuilder;
import lych.worldmodifiers.api.modifier.texture.RangedModifierTextureProviderBuilder;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import org.slf4j.Logger;

import javax.annotation.Nullable;
import java.util.Optional;

/**
 * The currently incomplete API for World Modifiers mod.
 */
public interface WorldModifiersAPI {
    /**
     * The modid of the World Modifiers mod.
     */
    String MODID = "worldmodifiers";
    /**
     * The logger for the API.
     */
    Logger LOGGER = LogUtils.getLogger();

    /**
     * Gets the instance of the API.
     *
     * @return the instance of the API.
     */
    static WorldModifiersAPI getInstance() {
        return APIUtils.INSTANCE_SUPPLIER.get();
    }

    /**
     * Checks if the World Modifiers Mod exists.
     * @return <code>true</code> if the World Modifiers Mod exists, <code>false</code> otherwise
     */
    static boolean isWorldModifierModPresent() {
        return !getInstance().isDummy();
    }

    /**
     * Checks if the API is a dummy.
     * @return <code>true</code> if the API is a dummy, <code>false</code> otherwise
     */
    default boolean isDummy() {
        return true;
    }

    /**
     * Returns a unique version number for this version of the API. When anything is added,
     * this number will be incremented.
     * @return the version number of the API
     */
    default int getAPIVersion() {
        return 0;
    }

    /**
     * Initializes the API.
     */
    default void init() {}

    /**
     * Gets a modifier which is categorized in the default modifier category by the given name.
     *
     * @param name the name of the modifier
     * @return the modifier
     */
    default Optional<Modifier<?>> getModifierByName(String name) {
        return getModifierByName(WorldModifiersMod.MODID, name);
    }

    /**
     * Gets a modifier by the given id and name.
     *
     * @param id   the id of the modifier
     * @param name the name of the modifier
     * @return the modifier
     */
    default Optional<Modifier<?>> getModifierByName(String id, String name) {
        return Optional.empty();
    }

    /**
     * Gets the value of the modifier in the given level.
     *
     * @param <T>      the type of the modifier's value
     * @param level    the level, can be either {@link ClientLevel}
     *                 or {@link ServerLevel}
     * @param modifier the modifier
     * @return the value of the modifier
     */
    default <T> T getModifierValue(LevelAccessor level, Modifier<T> modifier) {
        return modifier.getDefaultValue();
    }

    /**
     * Gets the value of the modifier in the given server.
     *
     * @param <T>      the type of the modifier's value
     * @param server   the server
     * @param modifier the modifier
     * @return the value of the modifier
     */
    default <T> T getModifierValue(MinecraftServer server, Modifier<T> modifier) {
        return modifier.getDefaultValue();
    }

    /**
     * Sets the value of the modifier in the given level.
     *
     * @param <T>      the type of the modifier's value
     * @param level    the level, can be either {@link ClientLevel}
     *                 or {@link ServerLevel}
     * @param modifier the modifier
     * @param value    the value
     */
    default <T> void setModifierValue(Level level, Modifier<T> modifier, T value) {}

    /**
     * Sets the value of the modifier in the given server.
     *
     * @param <T>      the type of the modifier's value
     * @param server   the server
     * @param modifier the modifier
     * @param value    the value
     */
    default <T> void setModifierValue(MinecraftServer server, Modifier<T> modifier, T value) {}

    /**
     * Creates a base modifier category for the given id.
     *
     * @param id the id of the category
     * @return the created category
     */
    default ModifierCategory createBaseCategoryFor(String id) {
        return APIUtils.getDummyModifierCategory();
    }

    /**
     * Creates an instance of {@link IntModifierBuilder}
     *
     * @param id the id of the modifier that will be created
     * @param name the name of the modifier that will be created
     * @return the builder
     * @see IntModifierBuilder
     */
    default IntModifierBuilder getIntModifierBuilder(String id, String name) {
        return new IntModifierBuilder() {};
    }

    /**
     * Creates an instance of {@link DynamicModifierTextureProviderBuilder}
     *
     * @param <T> the type of the modifier's value
     * @param defaultTextureLocation the default texture location. If <code>null</code>, an exception will be thrown
     *                               when no valid texture is found for a modifier value
     * @return the builder
     * @see DynamicModifierTextureProviderBuilder
     */
    default <T> DynamicModifierTextureProviderBuilder<T> getDynamicModifierTextureProviderBuilder(
            @Nullable ResourceLocation defaultTextureLocation
    ) {
        return new DynamicModifierTextureProviderBuilder<>() {};
    }

    /**
     * Creates an instance of {@link RangedModifierTextureProviderBuilder}
     *
     * @param <T> the type of the modifier's value
     * @param defaultTextureLocation the default texture location. If <code>null</code>, an exception will be thrown
     *                               when no valid texture is found for a modifier value
     * @return the builder
     * @see RangedModifierTextureProviderBuilder
     */
    default <T extends Comparable<? super T>> RangedModifierTextureProviderBuilder<T> getRangedModifierTextureProviderBuilder(
            @Nullable ResourceLocation defaultTextureLocation
    ) {
        return new RangedModifierTextureProviderBuilder<>() {};
    }
}
