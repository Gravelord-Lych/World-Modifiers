package lych.worldmodifiers;

import com.mojang.logging.LogUtils;
import lych.worldmodifiers.modifier.Modifiers;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(WorldModifiersMod.MODID)
public class WorldModifiersMod
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "worldmodifiers";
    public static final String MODNAME = "World Modifiers";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public WorldModifiersMod(IEventBus modEventBus, ModContainer modContainer) {
        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
        Modifiers.bootstrap();
    }

    public static ResourceLocation prefix(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }

    public static ResourceLocation prefixTex(String path) {
        return prefixTex(MODID, path);
    }

    public static ResourceLocation prefixTex(String id, String path) {
        return ResourceLocation.fromNamespaceAndPath(id, "textures/" + path);
    }

    public static MutableComponent prefixMsg(String key) {
        return Component.translatable(MODID + "." + key);
    }

    public static MutableComponent prefixMsg(String key, Object... args) {
        return Component.translatable(MODID + "." + key, args);
    }
}
