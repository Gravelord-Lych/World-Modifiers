package lych.worldmodifiers.mixin;

import lych.worldmodifiers.util.IDedicatedServerPropertiesMixin;
import net.minecraft.server.dedicated.DedicatedServerProperties;
import net.minecraft.server.dedicated.Settings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.Properties;

@Mixin(DedicatedServerProperties.class)
public abstract class DedicatedServerPropertiesMixin extends Settings<DedicatedServerProperties> implements IDedicatedServerPropertiesMixin {
    @Unique
    private final boolean worldModifiers$extremeDifficulty = get("world-modifiers-extreme-difficulty", false);

    private DedicatedServerPropertiesMixin(Properties properties) {
        super(properties);
    }

    @Override
    public boolean worldModifiers$isExtremeDifficulty() {
        return worldModifiers$extremeDifficulty;
    }
}
