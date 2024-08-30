package lych.worldmodifiers.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import lych.worldmodifiers.enchantment.provider.ModEnchantmentProviders;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.monster.Vindicator;
import net.minecraft.world.entity.raid.Raid;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.providers.EnchantmentProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Vindicator.class)
public class VindicatorMixin {
    @WrapOperation(method = "applyRaidBuffs", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/enchantment/EnchantmentHelper;enchantItemFromProvider(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/core/RegistryAccess;Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/world/DifficultyInstance;Lnet/minecraft/util/RandomSource;)V"))
    private void worldModifiers$applyStrongerRaidBuffs(ItemStack stack, RegistryAccess registries, ResourceKey<EnchantmentProvider> key, DifficultyInstance difficulty, RandomSource random, Operation<Void> original, @Local(argsOnly = true) int wave, @Local Raid raid) {
        original.call(stack,
                registries,
                wave > raid.getNumGroups(Difficulty.HARD) ? ModEnchantmentProviders.RAID_VINDICATOR_POST_WAVE_7 : key,
                difficulty,
                random);
    }
}
