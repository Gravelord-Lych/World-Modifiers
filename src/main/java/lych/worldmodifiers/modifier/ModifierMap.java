package lych.worldmodifiers.modifier;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ModifierMap {
    private static final String MODIFIER_TAG = "Modifiers";
    private static final String MODIFIER_NAME_TAG = "ModifierName";
    private static final String MODIFIER_VALUE_TAG = "ModifierValue";
    private final Map<Modifier<?>, Object> modifiers = new HashMap<>();
    private boolean dirty;

    public ModifierMap() {
        NameToModifierMap.viewAll().forEach((name, modifier) -> modifiers.put(modifier, modifier.getDefaultValue()));
        setDirty();
    }

    @SuppressWarnings("unchecked")
    public <T> T getModifierValue(Modifier<T> modifier) {
        T value = (T) modifiers.get(modifier);
        if (value == null) {
            value = modifier.getDefaultValue();
        }
        return value;
    }

    @SuppressWarnings("unchecked")
    public <T> T setModifierValue(Modifier<T> modifier, T value) {
        Objects.requireNonNull(value, "Value cannot be null");
        T oldValue = (T) modifiers.put(modifier, value);
        if (oldValue == null) {
            oldValue = modifier.getDefaultValue();
        }
        if (value != oldValue) {
            setDirty();
        }
        return oldValue;
    }

    public CompoundTag saveTag(CompoundTag tag) {
        ListTag modifierTag = new ListTag();
        for (Map.Entry<Modifier<?>, ?> entry : modifiers.entrySet()) {
            CompoundTag singleTag = forceSave(entry.getKey(), entry.getValue());
            modifierTag.add(singleTag);
        }
        tag.put(MODIFIER_TAG, modifierTag);
        return tag;
    }

    @SuppressWarnings("unchecked")
    private static <T> CompoundTag forceSave(Modifier<T> modifier, Object value) {
        CompoundTag singleTag = new CompoundTag();
        singleTag.putString(MODIFIER_NAME_TAG, modifier.getName());
        singleTag.put(MODIFIER_VALUE_TAG, modifier.save((T) value));
        return singleTag;
    }

    @SuppressWarnings("unused")
    public static ModifierMap load(CompoundTag tag) {
        ModifierMap map = new ModifierMap();
        ListTag modifierTag = tag.getList(MODIFIER_TAG, Tag.TAG_COMPOUND);

        for (int i = 0; i < modifierTag.size(); i++) {
            CompoundTag singleTag = modifierTag.getCompound(i);
            String name = singleTag.getString(MODIFIER_NAME_TAG);
            Modifier<?> modifier = NameToModifierMap.byName(name).orElseThrow(() -> new IllegalStateException("Unknown modifier: " + name));
            map.modifiers.put(modifier, modifier.load(singleTag.getCompound(MODIFIER_VALUE_TAG)));
        }

        return map;
    }

    public boolean isDirty() {
        return dirty;
    }

    public void setDirty() {
        setDirty(true);
    }

    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }
}
