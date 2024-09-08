package lych.worldmodifiers.modifier;

import com.google.gson.JsonObject;
import lych.worldmodifiers.WorldModifiersMod;
import lych.worldmodifiers.client.screen.EditModifiersScreen;
import lych.worldmodifiers.client.screen.entry.BooleanModifierEntry;
import lych.worldmodifiers.client.screen.entry.ModifierEntry;
import lych.worldmodifiers.modifier.category.AbstractModifier;
import lych.worldmodifiers.modifier.category.ModifierCategory;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;

import java.util.List;

public final class BooleanModifier extends AbstractModifier<Boolean> {
    private final boolean defaultValue;

    private BooleanModifier(String id, String name, ModifierCategory parent, SortingPriority priority, boolean defaultValue) {
        super(id, name, parent, priority);
        this.defaultValue = defaultValue;
    }

    public static BooleanModifier create(String name, ModifierCategory parent, boolean defaultValue) {
        return create(WorldModifiersMod.MODID, name, parent, defaultValue);
    }

    public static BooleanModifier create(String id, String name, ModifierCategory parent, boolean defaultValue) {
        return create(id, name, parent, SortingPriority.NORMAL, defaultValue);
    }

    public static BooleanModifier create(String name, ModifierCategory parent, SortingPriority priority, boolean defaultValue) {
        return create(WorldModifiersMod.MODID, name, parent, priority, defaultValue);
    }

    public static BooleanModifier create(String id, String name, ModifierCategory parent, SortingPriority priority, boolean defaultValue) {
        BooleanModifier booleanModifier = new BooleanModifier(id, name, parent, priority, defaultValue);
        NameToModifierMap.put(booleanModifier);
        return booleanModifier;
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
    public ModifierEntry createEntry(EditModifiersScreen screen, ModifierMap modifierMap, Component label, List<FormattedCharSequence> tooltip, int entryDepth, String name, Boolean value) {
        return new BooleanModifierEntry(screen, modifierMap, label, tooltip, entryDepth, name, this, value);
    }
}
