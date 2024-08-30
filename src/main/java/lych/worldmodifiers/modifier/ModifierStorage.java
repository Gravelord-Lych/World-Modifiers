package lych.worldmodifiers.modifier;

import com.mojang.logging.LogUtils;
import lych.worldmodifiers.WorldModifiers;
import net.minecraft.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtAccounter;
import net.minecraft.nbt.NbtIo;
import net.minecraft.world.level.storage.FileNameDateFormatter;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.neoforged.neoforge.common.IOUtilities;
import org.slf4j.Logger;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class ModifierStorage {
    public static final LevelResource DATA_DIR = new LevelResource(WorldModifiers.MODID);
    private static final DateTimeFormatter FORMATTER = FileNameDateFormatter.create();
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final String NAME = "modifiers";
    private final File modifierDir;

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public ModifierStorage(LevelStorageSource.LevelStorageAccess levelStorageAccess) {
        this.modifierDir = levelStorageAccess.getLevelPath(DATA_DIR).toFile();
        modifierDir.mkdirs();
    }

    public void save(ModifierMap map) {
        LOGGER.info("Saving world modifiers");
        try {
            CompoundTag tag = map.saveTag(new CompoundTag());
            Path path = this.modifierDir.toPath();
            Path tempDat = Files.createTempFile(path, NAME, ".dat");
            IOUtilities.writeNbtCompressed(tag, tempDat);
            Path dat = path.resolve(NAME + ".dat");
            Path datOld = path.resolve(NAME + ".dat_old");
            Util.safeReplaceFile(dat, tempDat, datOld);
        } catch (Exception exception) {
            LOGGER.warn("Failed to save world modifiers", exception);
        }
    }

    private void backup(String suffix) {
        Path path = modifierDir.toPath();
        Path source = path.resolve(NAME + suffix);
        Path target = path.resolve(NAME + "_corrupted_" + LocalDateTime.now().format(FORMATTER) + suffix);
        if (Files.isRegularFile(source)) {
            try {
                Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.COPY_ATTRIBUTES);
            } catch (Exception exception) {
                LOGGER.warn("Failed to copy the {}.{} file", NAME, suffix, exception);
            }
        }
    }

    private Optional<CompoundTag> load(String suffix) {
        File file = new File(modifierDir, NAME + suffix);
        if (file.exists() && file.isFile()) {
            try {
                return Optional.of(NbtIo.readCompressed(file.toPath(), NbtAccounter.unlimitedHeap()));
            } catch (Exception exception) {
                LOGGER.warn("Failed to load world modifiers", exception);
            }
        }
        return Optional.empty();
    }

    public ModifierMap load() {
        Optional<CompoundTag> tagOptional = load(".dat");
        if (tagOptional.isEmpty()) {
            backup(".dat");
        }
        return tagOptional.or(() -> load(".dat_old")).map(ModifierMap::load).orElseThrow();
    }
}
