package lych.worldmodifiers.util;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import lych.worldmodifiers.api.client.screen.entry.ModifierEntry;
import lych.worldmodifiers.api.modifier.Modifier;
import lych.worldmodifiers.client.screen.EditModifiersScreen;
import lych.worldmodifiers.client.screen.entry.ModifierEntryContext;
import lych.worldmodifiers.modifier.ModifierMap;
import lych.worldmodifiers.modifier.StoredModifiers;
import lych.worldmodifiers.modifier.selector.ModifierSelector;
import lych.worldmodifiers.network.ModifierNetwork;
import lych.worldmodifiers.util.mixin.IAdditionalClientLevelData;
import lych.worldmodifiers.util.mixin.IMinecraftServerMixin;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.level.LevelAccessor;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Objects;

public final class ModifierHelper {
    public static final Component MODIFIERS = MessageUtils.prefixMsg("selectWorld.modifiers");

    private ModifierHelper() {}

    public static <T> T getModifierValue(LevelAccessor level, Modifier<T> modifier) {
        if (level.isClientSide()) {
            return ((IAdditionalClientLevelData) level.getLevelData()).worldModifiers$getSynchedModifiers().getModifierValue(modifier);
        }
        return getModifierValue(Objects.requireNonNull(level.getServer()), modifier);
    }

    public static <T> T getModifierValue(MinecraftServer server, Modifier<T> modifier) {
        return ((IMinecraftServerMixin) server).worldModifiers$getStoredModifiers().getModifierValue(modifier);
    }

    public static <T, R> R getModifierValue(LevelAccessor level, ModifierSelector<? super T, R> selector, T object) {
        if (level.isClientSide()) {
            return ((IAdditionalClientLevelData) level.getLevelData()).worldModifiers$getSynchedModifiers().getModifierValue(selector, object);
        }
        return getModifierValue(Objects.requireNonNull(level.getServer()), selector, object);
    }

    public static <T, R> R getModifierValue(MinecraftServer server, ModifierSelector<? super T, R> selector, T object) {
        return ((IMinecraftServerMixin) server).worldModifiers$getStoredModifiers().getModifierValue(selector, object);
    }

    @CanIgnoreReturnValue
    public static <T> T setModifierValue(LevelAccessor level, Modifier<T> modifier, T value) {
        T oldValue;
        if (level.isClientSide()) {
            ModifierMap map = ((IAdditionalClientLevelData) level.getLevelData()).worldModifiers$getSynchedModifiers();
            oldValue = map.setModifierValue(modifier, value);
            ModifierNetwork.sendModifierEntryToServer(modifier, value);
        } else {
            oldValue = ModifierHelper.setModifierValue(Objects.requireNonNull(level.getServer()), modifier, value);
        }
        return oldValue;
    }

    @CanIgnoreReturnValue
    public static <T> T setModifierValue(MinecraftServer server, Modifier<T> modifier, T value) {
        StoredModifiers storedModifiers = ((IMinecraftServerMixin) server).worldModifiers$getStoredModifiers();
        T oldValue = storedModifiers.setModifierValue(modifier, value);
        ModifierNetwork.sendModifierEntryToClient(modifier, value);
        return oldValue;
    }

    public static void resetModifiers(LevelAccessor level, ModifierMap map) {
        if (level.isClientSide()) {
            ModifierMap oldMap = ((IAdditionalClientLevelData) level.getLevelData()).worldModifiers$getSynchedModifiers();
            oldMap.reloadFrom(map);
            ModifierNetwork.sendModifierMapToServer(map);
        } else {
            @SuppressWarnings("DataFlowIssue")
            StoredModifiers storedModifiers = ((IMinecraftServerMixin) level.getServer()).worldModifiers$getStoredModifiers();
            storedModifiers.reloadModifiersFrom(map);
            ModifierNetwork.sendModifierMapToClient(map);
        }
    }

    public static void reloadModifiersClientside(LevelAccessor level, ModifierMap map) {
        if (!level.isClientSide()) {
            throw new IllegalArgumentException("Wrong side");
        }
        ((IAdditionalClientLevelData) level.getLevelData()).worldModifiers$getSynchedModifiers().reloadFrom(map);
    }

    public static void reloadModifiersServerside(MinecraftServer server, ModifierMap map) {
        Objects.requireNonNull(server, "MinecraftServer cannot be null");
        ((IMinecraftServerMixin) server).worldModifiers$getStoredModifiers().reloadModifiersFrom(map);
    }

    public static <T> void syncModifierValueClientside(LevelAccessor level, Modifier<T> modifier, T value) {
        if (!level.isClientSide()) {
            throw new IllegalArgumentException("Wrong side");
        }
        ((IAdditionalClientLevelData) level.getLevelData()).worldModifiers$getSynchedModifiers().setModifierValue(modifier, value);
    }

    public static <T> void syncModifierValueServerside(MinecraftServer server, Modifier<T> modifier, T value) {
        Objects.requireNonNull(server, "MinecraftServer cannot be null");
        ((IMinecraftServerMixin) server).worldModifiers$getStoredModifiers().setModifierValue(modifier, value);
    }

    public static <T, E extends ModifierEntry<T>> E createEntry(Class<E> entryClass,
                                                             Class<T> valueClass,
                                                             EditModifiersScreen editModifiersScreen,
                                                             ModifierMap modifierMap,
                                                             Component displayName,
                                                             List<FormattedCharSequence> tooltip,
                                                             int depth,
                                                             String defaultValueText,
                                                             Modifier<T> modifier,
                                                             T value) {
        Constructor<E> constructor;
        try {
            constructor = entryClass.getConstructor(ModifierEntryContext.class, valueClass);
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("Missing constructor for entry class %s".formatted(entryClass.getSimpleName()), e);
        }
        E entry;
        try {
            entry = constructor.newInstance(new ModifierEntryContext<>(editModifiersScreen, modifierMap, displayName, tooltip, depth, defaultValueText, modifier), value);
        } catch (ReflectiveOperationException e) {
            throw new IllegalArgumentException("Failed to create entry for class %s".formatted(entryClass.getSimpleName()), e);
        }
        return entry;
    }
}
