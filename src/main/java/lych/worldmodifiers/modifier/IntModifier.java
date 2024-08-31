package lych.worldmodifiers.modifier;

import com.google.common.base.MoreObjects;
import com.google.gson.JsonObject;
import lych.worldmodifiers.WorldModifiersMod;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

import java.util.Objects;

public final class IntModifier implements Modifier<Integer> {
    private final ResourceLocation name;
    private final Component component;
    private final int defaultValue;
    private final int minValue;
    private final int maxValue;

    public IntModifier(String name, int defaultValue, int minValue, int maxValue) {
        this(name, WorldModifiersMod.MODID, defaultValue, minValue, maxValue);
    }

    public IntModifier(String name, String id, int defaultValue, int minValue, int maxValue) {
        this.name = ResourceLocation.fromNamespaceAndPath(id, name);
        this.component = Component.translatable(Modifier.NAME + "." + id + "." + name);
        this.defaultValue = defaultValue;
        this.minValue = minValue;
        this.maxValue = maxValue;
        NameToModifierMap.put(this);
    }

    @Override
    public ResourceLocation getName() {
        return name;
    }

    @Override
    public Component getDisplayName() {
        return component;
    }

    @Override
    public Integer getDefaultValue() {
        return defaultValue;
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
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("name", name)
                .add("defaultValue", defaultValue)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IntModifier that = (IntModifier) o;
        return defaultValue == that.defaultValue && Objects.equals(getName(), that.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getDefaultValue());
    }
}
