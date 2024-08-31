package lych.worldmodifiers.enchantment.provider;

import lych.worldmodifiers.WorldModifiersMod;
import lych.worldmodifiers.util.DifficultyHelper;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.providers.EnchantmentProvider;

public final class ModEnchantmentProviders {
    public static final ResourceKey<EnchantmentProvider> RAID_PILLAGER_POST_WAVE_7 = create("raid/pillager_post_wave_7");
    public static final ResourceKey<EnchantmentProvider> RAID_VINDICATOR_POST_WAVE_7 = create("raid/vindicator_post_wave_7");

    private ModEnchantmentProviders() {}

    public static void bootstrap(BootstrapContext<EnchantmentProvider> context) {
        HolderGetter<Enchantment> enchantmentHolderGetter = context.lookup(Registries.ENCHANTMENT);
        DifficultyHelper.registerEnchantmentProvidersOfRaiders(context, enchantmentHolderGetter);
    }

    public static ResourceKey<EnchantmentProvider> create(String name) {
        return ResourceKey.create(Registries.ENCHANTMENT_PROVIDER, WorldModifiersMod.prefix(name));
    }
}
