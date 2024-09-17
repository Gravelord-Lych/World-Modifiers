package lych.worldmodifiers.util;

import lych.worldmodifiers.WorldModifiersMod;
import lych.worldmodifiers.api.modifier.Modifier;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.resources.ResourceLocation;

public final class MessageUtils {
    private MessageUtils() {}

    public static String getTranslationKey(Component component) {
        if (!(component.getContents() instanceof TranslatableContents)) {
            throw new IllegalArgumentException("Component must be translatable");
        }
        return ((TranslatableContents) component.getContents()).getKey();
    }

    public static ResourceLocation prefix(String path) {
        return ResourceLocation.fromNamespaceAndPath(WorldModifiersMod.MODID, path);
    }

    public static ResourceLocation prefixTex(String path) {
        return prefixTex(WorldModifiersMod.MODID, path);
    }

    public static ResourceLocation prefixTex(String id, String path) {
        return ResourceLocation.fromNamespaceAndPath(id, "textures/" + path);
    }

    public static MutableComponent prefixMsg(String key) {
        return Component.translatable(WorldModifiersMod.MODID + "." + key);
    }

    public static MutableComponent prefixMsg(String key, Object... args) {
        return Component.translatable(WorldModifiersMod.MODID + "." + key, args);
    }

    public static MutableComponent getPercentSignOrEmpty(Modifier<?> modifier) {
        return modifier.representsPercentage() ? Component.literal("%") : Component.empty();
    }
}
