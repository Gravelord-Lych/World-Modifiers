package lych.worldmodifiers.api.modifier;

import com.google.gson.JsonObject;
import lych.worldmodifiers.api.client.screen.entry.ModifierEntry;
import lych.worldmodifiers.api.modifier.category.ModifierCategory;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Set;

/**
 * Interface for all world modifiers.
 *
 * @apiNote You should not implement this interface directly, but you can use
 * {@link IntModifierBuilder} to create a new modifier.
 *
 * @param <T> the type of the modifier's value
 */
public interface Modifier<T> extends BaseModifier {
    String NAME = "modifier";
    String NAME_PROPERTY = "name";
    String VALUE_PROPERTY = "value";

    /**
     * Serializes the modifier's value to a JSON object.
     * @param value the value to serialize
     * @param data the JSON object to serialize to
     */
    void serializeToJson(T value, JsonObject data);

    /**
     * Deserializes the modifier's value from a JSON object.
     * @param data the JSON object to deserialize from
     * @return the deserialized value
     */
    T deserializeFromJson(JsonObject data);

    /**
     * Serializes the modifier's value to a network buffer.
     * @param value the value to serialize
     * @param buf the network buffer to serialize to
     */
    void serializeToNetwork(T value, FriendlyByteBuf buf);

    /**
     * Deserializes the modifier's value from a network buffer.
     * @param buf the network buffer to deserialize from
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
     * Returns an unmodifiable empty set because modifiers can not have children.
     * @return an empty set
     */
    @Override
    default Set<BaseModifier> getChildren() {
        return Set.of();
    }

    /**
     * Returns the parent category of the modifier.
     * @return the parent category
     */
    @Nonnull
    @Override
    ModifierCategory getParent();

    /**
     * Returns the corresponding specific modifiers of the modifier if this is a generic modifier,
     * otherwise returns an empty set.
     * <p>
     * Specific modifiers are modifiers which are only applicable to a specific type of affectable
     * objects. For example, the max health modifier for animals can only affect animals' max health.
     * Note that a modifier can be both a generic modifier and a specific modifier, or neither a
     * generic modifier nor a specific modifier.
     *
     * @return the specific modifiers
     */
    Set<Modifier<?>> getSpecificModifiers();

    /**
     * Returns the corresponding generic modifier of the modifier if this is a specific modifier,
     * otherwise returns <code>null</code>.
     * <p>
     * Generic modifiers are modifiers which are applicable to all types of affectable objects.
     * For example, the generic max health modifier can affect all living entities' max health.
     *
     * @return the generic modifier
     */
    @Nullable
    Modifier<?> getGenericModifier();

    /**
     * Returns whether the modifier is a specific modifier.
     * @return <code>true</code> if the modifier is a specific modifier, <code>false</code> otherwise
     */
    default boolean isSpecific() {
        return getGenericModifier() != null;
    }

    /**
     * Returns whether the modifier is a generic modifier.
     * @return <code>true</code> if the modifier is a generic modifier, <code>false</code> otherwise
     */
    default boolean isGeneric() {
        return !getSpecificModifiers().isEmpty();
    }

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
