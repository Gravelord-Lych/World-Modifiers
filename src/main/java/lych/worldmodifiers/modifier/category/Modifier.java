package lych.worldmodifiers.modifier.category;

import com.google.gson.JsonObject;
import lych.worldmodifiers.WorldModifiersMod;
import lych.worldmodifiers.client.screen.EditModifiersScreen;
import lych.worldmodifiers.client.screen.entry.ModifierEntry;
import lych.worldmodifiers.modifier.ModifierMap;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;

import javax.annotation.Nonnull;
import java.util.List;

public interface Modifier<T> extends ModifierSetEntry {
    String NAME = "modifier";
    String NAME_PROPERTY = "name";
    String DATA_PROPERTY = "data";
    String VALUE_PROPERTY = "value";

    T getDefaultValue();

    void serializeToJson(T value, JsonObject data);

    T deserializeFromJson(JsonObject data);

    void serializeToNetwork(T value, FriendlyByteBuf buf);

    T deserializeFromNetwork(FriendlyByteBuf buf);

    ModifierEntry createEntry(EditModifiersScreen screen, ModifierMap modifierMap, Component label, List<FormattedCharSequence> tooltip, int entryDepth, String name, T value);

    default ResourceLocation getTextureLocation() {
        return WorldModifiersMod.prefixTex(getName().getNamespace(), "modifiers/%s.png".formatted(getName().getPath()));
    }

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

    @Nonnull
    @Override
    ModifierCategory getParent();
}
