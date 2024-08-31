package lych.worldmodifiers.util;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import lych.worldmodifiers.modifier.Modifier;
import lych.worldmodifiers.modifier.ModifierMap;
import lych.worldmodifiers.modifier.StoredModifiers;
import lych.worldmodifiers.network.ModifiersNetwork;
import lych.worldmodifiers.util.mixin.IAdditionalClientLevelData;
import lych.worldmodifiers.util.mixin.IMinecraftServerMixin;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.LevelAccessor;

import java.util.Objects;

public final class ModifiersHelper {
    private ModifiersHelper() {}

    public static <T> T getModifierValue(LevelAccessor level, Modifier<T> modifier) {
        if (level.isClientSide()) {
            return ((IAdditionalClientLevelData) level.getLevelData()).worldModifiers$getSynchedModifiers().getModifierValue(modifier);
        }
        return getModifierValue(Objects.requireNonNull(level.getServer()), modifier);
    }

    public static <T> T getModifierValue(MinecraftServer server, Modifier<T> modifier) {
        return ((IMinecraftServerMixin) server).worldModifiers$getStoredModifiers().getModifierValue(modifier);
    }

    @CanIgnoreReturnValue
    public static <T> T setModifierValue(LevelAccessor level, Modifier<T> modifier, T value) {
        T oldValue;
        if (level.isClientSide()) {
            ModifierMap map = ((IAdditionalClientLevelData) level.getLevelData()).worldModifiers$getSynchedModifiers();
            oldValue = map.setModifierValue(modifier, value);
            ModifiersNetwork.sendModifierEntryToServer(modifier, value);
        } else {
            @SuppressWarnings("DataFlowIssue")
            StoredModifiers storedModifiers = ((IMinecraftServerMixin) level.getServer()).worldModifiers$getStoredModifiers();
            oldValue = storedModifiers.setModifierValue(modifier, value);
            ModifiersNetwork.sendModifierEntryToClient(modifier, value);
        }
        return oldValue;
    }

    public static void resetModifiers(LevelAccessor level, ModifierMap map) {
        if (level.isClientSide()) {
            ModifierMap oldMap = ((IAdditionalClientLevelData) level.getLevelData()).worldModifiers$getSynchedModifiers();
            oldMap.reloadFrom(map);
            ModifiersNetwork.sendModifierMapToServer(map);
        } else {
            @SuppressWarnings("DataFlowIssue")
            StoredModifiers storedModifiers = ((IMinecraftServerMixin) level.getServer()).worldModifiers$getStoredModifiers();
            storedModifiers.reloadModifiersFrom(map);
            ModifiersNetwork.sendModifierMapToClient(map);
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
}
