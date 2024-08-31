package lych.worldmodifiers.mixin;

import lych.worldmodifiers.modifier.StoredModifiers;
import lych.worldmodifiers.util.mixin.IMinecraftServerMixin;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.storage.LevelResource;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import javax.annotation.Nullable;
import java.nio.file.Path;
import java.util.Objects;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin implements IMinecraftServerMixin {
    @Shadow @Final private static Logger LOGGER;

    @Shadow public abstract Path getWorldPath(LevelResource pLevelResource);

    @Nullable
    @Unique
    private StoredModifiers worldModifiers$storedModifiers;

    @Override
    public StoredModifiers worldModifiers$getStoredModifiers() {
        Objects.requireNonNull(worldModifiers$storedModifiers, "Stored modifiers not present");
        return worldModifiers$storedModifiers;
    }

    @Override
    public void worldModifiers$saveModifiers() {
        Objects.requireNonNull(worldModifiers$storedModifiers, "Stored modifiers cannot be null");
        worldModifiers$storedModifiers.save();
    }

    @Override
    public void worldModifiers$loadModifiers() {
        worldModifiers$storedModifiers = new StoredModifiers(getWorldPath(StoredModifiers.MODIFIERS).toFile());
        worldModifiers$storedModifiers.load();
    }
}
