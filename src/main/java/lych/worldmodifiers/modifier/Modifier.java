package lych.worldmodifiers.modifier;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public interface Modifier<T> {
    String NAME = "modifier";
    String NAME_PROPERTY = "name";
    String DATA_PROPERTY = "data";
    String VALUE_PROPERTY = "value";

    ResourceLocation getName();

    Component getDisplayName();

    T getDefaultValue();

    void serializeToJson(T value, JsonObject data);

    T deserializeFromJson(JsonObject data);

    void serializeToNetwork(T value, FriendlyByteBuf buf);

    T deserializeFromNetwork(FriendlyByteBuf buf);

    default boolean hasValueRange() {
        return false;
    }

    default T getMinValue() {
        throw new UnsupportedOperationException();
    }

    default T getMaxValue() {
        throw new UnsupportedOperationException();
    }

    default T sanitizeValue(T value) {
        throw new UnsupportedOperationException();
    }
}
