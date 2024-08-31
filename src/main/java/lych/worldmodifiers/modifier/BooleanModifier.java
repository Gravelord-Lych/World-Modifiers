package lych.worldmodifiers.modifier;

import com.google.common.base.MoreObjects;
import com.google.gson.JsonObject;
import lych.worldmodifiers.WorldModifiersMod;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.Objects;

public final class BooleanModifier implements Modifier<Boolean> {
    private final ResourceLocation name;
    private final Component component;
    private final boolean defaultValue;

    public BooleanModifier(String name, boolean defaultValue) {
        this(name, WorldModifiersMod.MODID, defaultValue);
    }

    public BooleanModifier(String name, String id, boolean defaultValue) {
        this.name = ResourceLocation.fromNamespaceAndPath(id, name);
        this.component = Component.translatable(Modifier.NAME + "." + id + "." + name);
        this.defaultValue = defaultValue;
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
    public Boolean getDefaultValue() {
        return defaultValue;
    }

    @Override
    public void serializeToJson(Boolean value, JsonObject data) {
        data.addProperty(VALUE_PROPERTY, value);
    }

    @Override
    public Boolean deserializeFromJson(JsonObject data) {
        return data.get(VALUE_PROPERTY).getAsBoolean();
    }

    @Override
    public void serializeToNetwork(Boolean value, FriendlyByteBuf buf) {
        buf.writeBoolean(value);
    }

    @Override
    public Boolean deserializeFromNetwork(FriendlyByteBuf buf) {
        return buf.readBoolean();
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
        BooleanModifier that = (BooleanModifier) o;
        return defaultValue == that.defaultValue && Objects.equals(getName(), that.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getDefaultValue());
    }
}
