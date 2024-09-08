package lych.worldmodifiers.modifier;

import com.google.gson.JsonObject;
import lych.worldmodifiers.WorldModifiersMod;
import lych.worldmodifiers.client.screen.EditModifiersScreen;
import lych.worldmodifiers.client.screen.entry.IntModifierEntry;
import lych.worldmodifiers.client.screen.entry.ModifierEntry;
import lych.worldmodifiers.modifier.category.AbstractModifier;
import lych.worldmodifiers.modifier.category.ModifierCategory;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;

import java.util.List;

public final class IntModifier extends AbstractModifier<Integer> {
    private final int defaultValue;
    private final int minValue;
    private final int maxValue;

    private IntModifier(String id, String name, ModifierCategory parent, SortingPriority priority, int defaultValue, int minValue, int maxValue) {
        super(id, name, parent, priority);
        this.defaultValue = defaultValue;
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    public static IntModifier create(String name, ModifierCategory parent, int defaultValue, int minValue, int maxValue) {
        return create(WorldModifiersMod.MODID, name, parent, defaultValue, minValue, maxValue);
    }

    public static IntModifier create(String id, String name, ModifierCategory parent, int defaultValue, int minValue, int maxValue) {
        return create(id, name, parent, SortingPriority.NORMAL, defaultValue, minValue, maxValue);
    }

    public static IntModifier create(String name, ModifierCategory parent, SortingPriority priority, int defaultValue, int minValue, int maxValue) {
        return create(WorldModifiersMod.MODID, name, parent, priority, defaultValue, minValue, maxValue);
    }

    public static IntModifier create(String id, String name, ModifierCategory parent, SortingPriority priority, int defaultValue, int minValue, int maxValue) {
        IntModifier intModifier = new IntModifier(id, name, parent, priority, defaultValue, minValue, maxValue);
        NameToModifierMap.put(intModifier);
        parent.addChildModifier(intModifier);
        return intModifier;
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
    public ModifierEntry createEntry(EditModifiersScreen screen, ModifierMap modifierMap, Component label, List<FormattedCharSequence> tooltip, int entryDepth, String name, Integer value) {
        return new IntModifierEntry(screen, modifierMap, label, tooltip, entryDepth, name, this, value);
    }
}
