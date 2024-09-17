package lych.worldmodifiers.modifier;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.MoreObjects;
import com.google.common.collect.Streams;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.logging.LogUtils;
import lych.worldmodifiers.api.modifier.Modifier;
import net.minecraft.Util;
import net.minecraft.network.FriendlyByteBuf;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class ModifierMap {
    private static final Logger LOGGER = LogUtils.getLogger();
    private final Map<Modifier<?>, Object> modifiers = new HashMap<>();
    private boolean dirty;

    public ModifierMap() {
        resetAllModifiers();
        setDirty();
    }

    public void resetAllModifiers() {
        NameToModifierMap.viewAll().forEach((name, modifier) -> modifiers.put(modifier, modifier.getDefaultValue()));
    }

    public ModifierMap copy() {
        ModifierMap copy = new ModifierMap();
        copy.reloadFrom(this);
        return copy;
    }

    public void reloadFrom(ModifierMap other) {
        resetAllModifiers();
        boolean changed = false;
        for (Map.Entry<String, Modifier<?>> entry : NameToModifierMap.viewAll().entrySet()) {
            Modifier<?> modifier = entry.getValue();
            Object valueFromOther = other.getModifierValue(modifier);
            Object oldValue = modifiers.put(modifier, valueFromOther);
            if (!Objects.equals(oldValue, valueFromOther)) {
                changed = true;
            }
        }
        setDirty(changed);
    }

    public int difference(ModifierMap other) {
        int diff = 0;
        for (Map.Entry<String, Modifier<?>> entry : NameToModifierMap.viewAll().entrySet()) {
            Object value = modifiers.get(entry.getValue());
            Object otherValue = other.modifiers.get(entry.getValue());
            if (!Objects.equals(value, otherValue)) {
                diff++;
            }
        }
        return diff;
    }

    @VisibleForTesting
    public int size() {
        return modifiers.size();
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
        T sanitizedValue = trySanitizeValue(modifier, value);
        T oldValue = (T) modifiers.put(modifier, sanitizedValue);
        if (oldValue == null) {
            oldValue = modifier.getDefaultValue();
        }
        if (sanitizedValue != oldValue) {
            setDirty();
        }
        return oldValue;
    }

    private static <T> T trySanitizeValue(Modifier<T> modifier, T value) {
        if (!modifier.hasValueRange()) {
            return value;
        }
        T sanitizedValue = modifier.sanitizeValue(value);
        if (!Objects.equals(value, sanitizedValue)) {
            LOGGER.info("Sanitized value for modifier {}: {} -> {}", modifier.getFullName(), value, sanitizedValue);
        }
        return sanitizedValue;
    }

    private static void warnUnknown(String name) {
        LOGGER.warn("Unknown modifier: {}", name);
    }

    public void serializeToJson(JsonArray array) {
        modifiers.entrySet().stream()
                .map(entry -> Util.make(new JsonObject(), data -> forceSerializeToJson(entry.getKey(), entry.getValue(), data)))
                .forEach(array::add);
    }

    public static ModifierMap deserializeFromJson(JsonArray array) {
        ModifierMap map = new ModifierMap();
        Streams.stream(array).map(JsonElement::getAsJsonObject).forEach(data -> {
            if (!data.has(Modifier.NAME_PROPERTY)) {
                LOGGER.warn("Missing name property in a modifier entry in {}: {}", StoredModifiers.FILE_NAME, data);
                return;
            }
            String modifierName = data.get(Modifier.NAME_PROPERTY).getAsString();
            Optional<Modifier<?>> modifierOptional = NameToModifierMap.byFullName(modifierName);
            if (modifierOptional.isEmpty()) {
                warnUnknown(modifierName);
                return;
            }
            Modifier<?> modifier = modifierOptional.get();
            try {
                map.deserializeFromJson(data, modifier);
            } catch (Exception e) {
                LOGGER.error("Failed to deserialize modifier: {}", modifierName, e);
            }
        });
        return map;
    }

    public void serializeToNetwork(FriendlyByteBuf buf) {
        buf.writeInt(modifiers.size());
        modifiers.forEach((modifier, value) -> {
            buf.writeUtf(modifier.getFullName().toString());
            forceSerializeToNetwork(modifier, value, buf);
        });
    }

    public static ModifierMap deserializeFromNetwork(FriendlyByteBuf buf) {
        ModifierMap map = new ModifierMap();
        int size = buf.readInt();
        for (int i = 0; i < size; i++) {
            String modifierName = buf.readUtf();
            Optional<Modifier<?>> modifierOptional = NameToModifierMap.byFullName(modifierName);
            if (modifierOptional.isEmpty()) {
                warnUnknown(modifierName);
                continue;
            }
            Modifier<?> modifier = modifierOptional.get();
            map.deserializeFromNetwork(buf, modifier);
        }
        return map;
    }

    @SuppressWarnings("unchecked")
    private <T> void forceSerializeToJson(Modifier<T> modifier, Object value, JsonObject data) {
        data.addProperty(Modifier.NAME_PROPERTY, modifier.getFullName().toString());
        modifier.serializeToJson((T) value, data);
    }

    @SuppressWarnings("unchecked")
    private <T> void forceSerializeToNetwork(Modifier<T> modifier, Object value, FriendlyByteBuf buf) {
        modifier.serializeToNetwork((T) value, buf);
    }

    private <T> void deserializeFromJson(JsonObject data, Modifier<T> modifier) {
        setModifierValue(modifier, modifier.deserializeFromJson(data));
    }

    private <T> void deserializeFromNetwork(FriendlyByteBuf buf, Modifier<T> modifier) {
        setModifierValue(modifier, modifier.deserializeFromNetwork(buf));
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

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("modifiers", modifiers)
                .add("dirty", dirty)
                .toString();
    }
}
