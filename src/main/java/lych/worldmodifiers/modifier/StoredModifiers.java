package lych.worldmodifiers.modifier;

import com.google.common.io.Files;
import com.google.gson.*;
import com.mojang.logging.LogUtils;
import lych.worldmodifiers.WorldModifiersMod;
import lych.worldmodifiers.modifier.category.Modifier;
import net.minecraft.world.level.storage.LevelResource;
import org.slf4j.Logger;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class StoredModifiers {
    public static final String FILE_NAME = WorldModifiersMod.MODID + ".json";
    public static final LevelResource MODIFIERS = new LevelResource(FILE_NAME);
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private final File file;
    private final ModifierMap map;

//    @SuppressWarnings("ResultOfMethodCallIgnored")
    public StoredModifiers(File file) {
        this.file = file;
        this.map = new ModifierMap();
    }

    public void save() {
        try {
            JsonArray array = new JsonArray();
            map.serializeToJson(array);
            try (BufferedWriter writer = Files.newWriter(file, StandardCharsets.UTF_8)) {
                GSON.toJson(array, GSON.newJsonWriter(writer));
            }
        } catch (IOException e) {
            LOGGER.error("Failed to save world modifiers", e);
        }
    }

    public void load() {
        if (!file.exists()) {
            LOGGER.info("{} does not exist, using default modifiers", FILE_NAME);
            map.resetAllModifiers();
            return;
        }
        try {
            try (BufferedReader reader = Files.newReader(file, StandardCharsets.UTF_8)) {
                JsonArray array = GSON.fromJson(reader, JsonArray.class);
                if (array == null) {
                    return;
                }
                map.reloadFrom(ModifierMap.deserializeFromJson(array));
            }
        } catch (Exception e) {
            LOGGER.error("Failed to load world modifiers", e);
            map.resetAllModifiers();
        }
    }

    public ModifierMap getModifierMap() {
        return map;
    }

    public <T> T getModifierValue(Modifier<T> modifier) {
        return map.getModifierValue(modifier);
    }

    public <T> T setModifierValue(Modifier<T> modifier, T value) {
        T oldValue = map.setModifierValue(modifier, value);
        saveFileIfDirty();
        return oldValue;
    }

    public void reloadModifiersFrom(ModifierMap source) {
        map.reloadFrom(source);
        saveFileIfDirty();
    }

    private void saveFileIfDirty() {
        if (map.isDirty()) {
            save();
            map.setDirty(false);
        }
    }
}
