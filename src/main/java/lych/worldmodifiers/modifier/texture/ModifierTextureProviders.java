package lych.worldmodifiers.modifier.texture;

import lych.worldmodifiers.WorldModifiersMod;
import lych.worldmodifiers.api.modifier.texture.DynamicModifierTextureProviderBuilder;
import lych.worldmodifiers.api.modifier.texture.ModifierTextureProvider;
import lych.worldmodifiers.api.modifier.texture.RangedModifierTextureProviderBuilder;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;

public final class ModifierTextureProviders {
    private static final String INCREASED = "_increased";
    private static final String DECREASED = "_decreased";
    private static final String GREATLY_DECREASED = "_greatly_decreased";
    private static final String GREATLY_INCREASED = "_greatly_increased";

    private ModifierTextureProviders() {}

    public static <T> ModifierTextureProvider<T> single(String name) {
        return single(createDefaultTextureLocation(name));
    }

    public static <T> ModifierTextureProvider<T> single(ResourceLocation textureLocation) {
        return modifierValue -> textureLocation;
    }

    public static <T> DynamicModifierTextureProviderBuilder<T> dynamic() {
        return dynamic(null);
    }

    public static <T> DynamicModifierTextureProviderBuilder<T> dynamic(@Nullable ResourceLocation defaultTexture) {
        return new DynamicModifierTextureProvider.Builder<>(defaultTexture);
    }

    public static <T extends Comparable<? super T>> RangedModifierTextureProviderBuilder<T> ranged() {
        return ranged(null);
    }

    public static <T extends Comparable<? super T>> RangedModifierTextureProviderBuilder<T> ranged(@Nullable ResourceLocation defaultTexture) {
        return new RangedModifierTextureProvider.Builder<>(defaultTexture);
    }

    public static RangedModifierTextureProviderBuilder<Integer> ranged3(int defaultValue, int minValue, int maxValue, String name) {
        if (minValue >= defaultValue || maxValue <= defaultValue) {
            throw new IllegalArgumentException("Invalid value range. The correct ordering of the 3 integer arguments is minValue < defaultValue < maxValue");
        }
        return ModifierTextureProviders.<Integer>ranged(createDefaultTextureLocation(name))
                .addTexture(minValue, defaultValue - 1, createTextureLocation(name, DECREASED))
                .addTexture(defaultValue, createDefaultTextureLocation(name))
                .addTexture(defaultValue + 1, maxValue, createTextureLocation(name, INCREASED));
    }

    public static RangedModifierTextureProviderBuilder<Integer> ranged4(int defaultValue, int minValue, int mediumHighValue, int maxValue, String name) {
        if (minValue >= defaultValue || mediumHighValue <= defaultValue || maxValue <= mediumHighValue) {
            throw new IllegalArgumentException("Invalid value range. The correct ordering of the 4 integer arguments is minValue < defaultValue < mediumHighValue < maxValue");
        }
        return ModifierTextureProviders.<Integer>ranged(createDefaultTextureLocation(name))
                .addTexture(minValue, defaultValue - 1, createTextureLocation(name, DECREASED))
                .addTexture(defaultValue, createDefaultTextureLocation(name))
                .addTexture(defaultValue + 1, mediumHighValue, createTextureLocation(name, INCREASED))
                .addTexture(mediumHighValue + 1, maxValue, createTextureLocation(name, GREATLY_INCREASED));
    }

    public static RangedModifierTextureProviderBuilder<Integer> ranged5(int defaultValue, int minValue, int mediumLowValue, int mediumHighValue, int maxValue, String name) {
        if (minValue >= defaultValue || mediumLowValue <= minValue || mediumHighValue <= mediumLowValue || maxValue <= mediumHighValue) {
            throw new IllegalArgumentException("Invalid value range. The correct ordering of the 5 integer arguments is minValue < mediumLowValue < defaultValue < mediumHighValue < maxValue");
        }
        return ModifierTextureProviders.<Integer>ranged(createDefaultTextureLocation(name))
                .addTexture(minValue, mediumLowValue, createTextureLocation(name, GREATLY_DECREASED))
                .addTexture(mediumLowValue + 1, defaultValue - 1, createTextureLocation(name, DECREASED))
                .addTexture(defaultValue, createDefaultTextureLocation(name))
                .addTexture(defaultValue + 1, mediumHighValue, createTextureLocation(name, INCREASED))
                .addTexture(mediumHighValue + 1, maxValue, createTextureLocation(name, GREATLY_INCREASED));
    }

    public static ResourceLocation createDefaultTextureLocation(String name) {
        return createTextureLocation(name, "");
    }

    public static ResourceLocation createTextureLocation(String name, String suffix) {
        return ModifierTextureProvider.createTextureLocation(WorldModifiersMod.MODID, name, suffix);
    }
}
