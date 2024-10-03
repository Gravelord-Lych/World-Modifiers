package lych.worldmodifiers.api.modifier;

import lych.worldmodifiers.api.APIUtils;
import lych.worldmodifiers.api.modifier.category.ModifierCategory;
import lych.worldmodifiers.api.modifier.texture.ModifierTextureProvider;

/**
 * Builder for modifiers that have integer values.
 */
public interface IntModifierBuilder {
    /**
     * Sets the parent category of the modifier.
     * @param parent the parent category
     * @return the builder itself
     */
    default IntModifierBuilder setParent(ModifierCategory parent) {
        return this;
    }

    /**
     * Sets the sorting priority of the modifier.
     * @param priority the sorting priority
     * @return the builder itself
     */
    default IntModifierBuilder setSortingPriority(SortingPriority priority) {
        return this;
    }

    /**
     * Sets the texture provider for the modifier.
     * @param textureProvider the texture provider
     * @return the builder itself
     */
    default IntModifierBuilder setTextureProvider(ModifierTextureProvider<? super Integer> textureProvider) {
        return this;
    }

    /**
     * Sets the generic modifier of the modifier.
     * @param genericModifier the generic modifier
     * @return the builder itself
     */
    default IntModifierBuilder setGenericModifier(Modifier<?> genericModifier) {
        return this;
    }

    /**
     * Sets the default value of the modifier.
     * @param defaultValue the default value
     * @return the builder itself
     */
    default IntModifierBuilder setDefaultValue(int defaultValue) {
        return this;
    }

    /**
     * Sets the range of the modifier's value.
     * @param minValue the minimum value
     * @param maxValue the maximum value
     * @return the builder itself
     */
    default IntModifierBuilder setValueRange(int minValue, int maxValue) {
        return setMinValue(minValue).setMaxValue(maxValue);
    }

    /**
     * Sets the minimum value of the modifier.
     * @param minValue the minimum value
     * @return the builder itself
     */
    default IntModifierBuilder setMinValue(int minValue) {
        return this;
    }

    /**
     * Sets the maximum value of the modifier.
     * @param maxValue the maximum value
     * @return the builder itself
     */
    default IntModifierBuilder setMaxValue(int maxValue) {
        return this;
    }

    /**
     * Makes the modifier represent a percentage.
     *
     * @return the builder itself
     */
    default IntModifierBuilder setRepresentsPercentage() {
        return setRepresentsPercentage(true);
    }

    /**
     * Sets whether the modifier represents a percentage.
     * @param representsPercentage whether the modifier represents a percentage
     * @return the builder itself
     */
    default IntModifierBuilder setRepresentsPercentage(boolean representsPercentage) {
        return this;
    }

    /**
     * Builds the modifier.
     * @return the built modifier
     */
    default Modifier<Integer> build() {
        return APIUtils.createDummyIntModifier();
    }
}
