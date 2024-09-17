package lych.worldmodifiers.api.modifier;

import com.google.gson.JsonObject;
import lych.worldmodifiers.api.client.screen.entry.ModifierEntry;
import lych.worldmodifiers.api.modifier.category.ModifierCategory;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nonnull;

/**
 * Interface for all world modifiers. You should not implement this interface directly, but you can use
 * {@link IntModifierBuilder} to create a new modifier.
 * @param <T> the type of the modifier's value
 */
public interface Modifier<T> extends BaseModifier {
    String NAME = "modifier";
    String NAME_PROPERTY = "name";
    String VALUE_PROPERTY = "value";

    /**
     * Serializes the modifier's value to a JSON object.
     * @param value The value to serialize
     * @param data The JSON object to serialize to
     */
    void serializeToJson(T value, JsonObject data);

    /**
     * Deserializes the modifier's value from a JSON object.
     * @param data The JSON object to deserialize from
     * @return the deserialized value
     */
    T deserializeFromJson(JsonObject data);

    /**
     * Serializes the modifier's value to a network buffer.
     * @param value The value to serialize
     * @param buf The network buffer to serialize to
     */
    void serializeToNetwork(T value, FriendlyByteBuf buf);

    /**
     * Deserializes the modifier's value from a network buffer.
     * @param buf The network buffer to deserialize from
     * @return the deserialized value
     */
    T deserializeFromNetwork(FriendlyByteBuf buf);

    /**
     * Returns the class of the modifier's entry.
     * @return the class of the modifier's entry
     */
    Class<? extends ModifierEntry<T>> getEntryClass();

    /**
     * Returns the class of the modifier's value.
     * @return the class of the modifier's value
     */
    Class<T> getValueClass();

    /**
     * Returns the default texture location of the modifier.
     *
     * @return the default texture location
     */
    default ResourceLocation getDefaultTextureLocation() {
        return getTextureLocation(getDefaultValue());
    }

    /**
     * Returns the texture location of the modifier.
     * @param value the value of the modifier
     * @return the texture location
     */
    ResourceLocation getTextureLocation(T value);

    /**
     * Returns the parent category of the modifier.
     * @return the parent category
     */
    @Nonnull
    @Override
    ModifierCategory getParent();

    /**
     * Returns the default value of the modifier.
     * @return the default value
     */
    T getDefaultValue();

    /**
     * For integer modifiers, returns whether the modifier's value represents a percentage.
     * For other types of modifiers, this method always returns <code>false</code>.
     * @return <code>true</code> if the modifier's value represents a percentage, <code>false</code> otherwise
     */
    default boolean representsPercentage() {
        return false;
    }

    /**
     * Returns whether the modifier has a value range.
     * @return <code>true</code> if the modifier has a value range, <code>false</code> otherwise
     */
    default boolean hasValueRange() {
        return false;
    }

    /**
     * Returns the minimum value of the modifier if the modifier has a value range.
     * @return the minimum value of the modifier
     * @throws UnsupportedOperationException if the modifier does not have a value range
     */
    default T getMinValue() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns the maximum value of the modifier if the modifier has a value range.
     * @return the maximum value of the modifier
     * @throws UnsupportedOperationException if the modifier does not have a value range
     */
    default T getMaxValue() {
        throw new UnsupportedOperationException();
    }

    /**
     * Sanitizes the value to keep it within the modifier's value range if the modifier has a value range.
     * @param value the value to sanitize
     * @return the sanitized value
     * @throws UnsupportedOperationException if the modifier does not have a value range
     */
    default T sanitizeValue(T value) {
        throw new UnsupportedOperationException();
    }
}
