package lych.worldmodifiers.modifier;

import com.google.gson.JsonObject;
import lych.worldmodifiers.WorldModifiersMod;
import lych.worldmodifiers.api.client.screen.entry.ModifierEntry;
import lych.worldmodifiers.api.modifier.IntModifierBuilder;
import lych.worldmodifiers.api.modifier.SortingPriority;
import lych.worldmodifiers.api.modifier.category.ModifierCategory;
import lych.worldmodifiers.api.modifier.texture.ModifierTextureProvider;
import lych.worldmodifiers.client.screen.entry.IntModifierEntry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.Mth;

public final class IntModifier extends AbstractModifier<Integer> {
    private final int defaultValue;
    private final int minValue;
    private final int maxValue;
    private final boolean representsPercentage;

    private IntModifier(String id,
                        String name,
                        ModifierCategory parent,
                        SortingPriority priority,
                        ModifierTextureProvider<? super Integer> textureProvider,
                        int defaultValue,
                        int minValue,
                        int maxValue,
                        boolean representsPercentage) {
        super(id, name, parent, priority, textureProvider);
        if (minValue >= maxValue) {
            throw new IllegalArgumentException("Min value must be less than max value");
        }
        if (defaultValue < minValue || defaultValue > maxValue) {
            throw new IllegalArgumentException("Default value must be between min and max values");
        }
        this.defaultValue = defaultValue;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.representsPercentage = representsPercentage;
    }

    public static IntModifierBuilder builder(String name) {
        return builder(WorldModifiersMod.MODID, name);
    }

    public static IntModifierBuilder builder(String id, String name) {
        return new Builder(id, name);
    }

    @Override
    public Integer getDefaultValue() {
        return defaultValue;
    }

    @Override
    public boolean representsPercentage() {
        return representsPercentage;
    }

    @Override
    public boolean hasValueRange() {
        return true;
    }

    @Override
    public Integer getMinValue() {
        return minValue;
    }

    @Override
    public Integer getMaxValue() {
        return maxValue;
    }

    @Override
    public Integer sanitizeValue(Integer value) {
        return Mth.clamp(value, minValue, maxValue);
    }

    @Override
    public void serializeToJson(Integer value, JsonObject data) {
        data.addProperty(VALUE_PROPERTY, value);
    }

    @Override
    public Integer deserializeFromJson(JsonObject data) {
        return data.get(VALUE_PROPERTY).getAsInt();
    }

    @Override
    public void serializeToNetwork(Integer value, FriendlyByteBuf buf) {
        buf.writeInt(value);
    }

    @Override
    public Integer deserializeFromNetwork(FriendlyByteBuf buf) {
        return buf.readInt();
    }

    @Override
    public Class<? extends ModifierEntry<Integer>> getEntryClass() {
        return IntModifierEntry.class;
    }

    @Override
    public Class<Integer> getValueClass() {
        return Integer.class;
    }

    @SuppressWarnings("NotNullFieldNotInitialized")
    public static class Builder implements IntModifierBuilder {
        private final String id;
        private final String name;
        private ModifierCategory parent;
        private SortingPriority priority = SortingPriority.NORMAL;
        private ModifierTextureProvider<? super Integer> textureProvider;
        private int defaultValue;
        private int minValue;
        private int maxValue;
        private boolean representsPercentage;

        Builder(String id, String name) {
            this.id = id;
            this.name = name;
        }

        @Override
        public Builder setParent(ModifierCategory parent) {
            this.parent = parent;
            return this;
        }

        @Override
        public Builder setSortingPriority(SortingPriority priority) {
            this.priority = priority;
            return this;
        }

        @Override
        public Builder setTextureProvider(ModifierTextureProvider<? super Integer> textureProvider) {
            this.textureProvider = textureProvider;
            return this;
        }

        @Override
        public Builder setDefaultValue(int defaultValue) {
            this.defaultValue = defaultValue;
            return this;
        }

        @Override
        public Builder setMinValue(int minValue) {
            this.minValue = minValue;
            return this;
        }

        @Override
        public Builder setMaxValue(int maxValue) {
            this.maxValue = maxValue;
            return this;
        }

        @Override
        public IntModifierBuilder setRepresentsPercentage(boolean representsPercentage) {
            this.representsPercentage = representsPercentage;
            return this;
        }

        @Override
        public IntModifier build() {
            IntModifier modifier = new IntModifier(id,
                    name,
                    parent,
                    priority,
                    textureProvider,
                    defaultValue,
                    minValue,
                    maxValue,
                    representsPercentage);
            NameToModifierMap.put(modifier);
            parent.addChildModifier(modifier);
            return modifier;
        }
    }
}
