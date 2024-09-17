package lych.worldmodifiers.api.modifier;

import com.google.gson.JsonObject;
import lych.worldmodifiers.api.APIUtils;
import lych.worldmodifiers.api.client.screen.entry.ModifierEntry;
import lych.worldmodifiers.api.modifier.category.ModifierCategory;
import lych.worldmodifiers.api.modifier.texture.ModifierTextureProvider;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.Set;

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
        return new Modifier<>() {
            @Override
            public void serializeToJson(Integer value, JsonObject data) {}

            @Override
            public Integer deserializeFromJson(JsonObject data) {
                return 0;
            }

            @Override
            public void serializeToNetwork(Integer value, FriendlyByteBuf buf) {}

            @Override
            public Integer deserializeFromNetwork(FriendlyByteBuf buf) {
                return 0;
            }

            @Override
            public Class<? extends ModifierEntry<Integer>> getEntryClass() {
                Modifier<Integer> self = this;
                class DummyEntry implements ModifierEntry<Integer> {
                    @Override
                    public Modifier<Integer> getModifier() {
                        return self;
                    }
                }
                return DummyEntry.class;
            }

            @Override
            public Class<Integer> getValueClass() {
                return Integer.class;
            }

            @Override
            public ResourceLocation getTextureLocation(Integer value) {
                return APIUtils.DUMMY_RESOURCE_LOCATION;
            }

            @Nonnull
            @Override
            public ModifierCategory getParent() {
                return APIUtils.createDummyModifierCategory(Set.of(this));
            }

            @Override
            public Integer getDefaultValue() {
                return 0;
            }

            @Override
            public ResourceLocation getFullName() {
                return APIUtils.DUMMY_RESOURCE_LOCATION;
            }

            @Override
            public MutableComponent getDisplayName() {
                return Component.empty();
            }

            @Override
            public Set<BaseModifier> getChildren() {
                return Set.of();
            }

            @Override
            public SortingPriority getPriority() {
                return SortingPriority.NORMAL;
            }

            @Override
            public MutableComponent getDescription() {
                return Component.empty();
            }

            @Override
            public MutableComponent getWarning() {
                return Component.empty();
            }
        };
    }
}
