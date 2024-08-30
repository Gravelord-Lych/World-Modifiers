package lych.worldmodifiers.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import lych.worldmodifiers.enchantment.provider.ModEnchantmentProviders;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.monster.Pillager;
import net.minecraft.world.entity.raid.Raid;
import net.minecraft.world.item.enchantment.providers.EnchantmentProvider;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Pillager.class)
public class PillagerMixin {
    @WrapOperation(method = "applyRaidBuffs", at = @At(value = "FIELD",
            target = "Lnet/minecraft/world/item/enchantment/providers/VanillaEnchantmentProviders;RAID_PILLAGER_POST_WAVE_5:Lnet/minecraft/resources/ResourceKey;",
            opcode = Opcodes.GETSTATIC))
    private ResourceKey<EnchantmentProvider> worldModifiers$applyStrongerRaidBuffs(Operation<ResourceKey<EnchantmentProvider>> original, @Local(argsOnly = true) int wave, @Local Raid raid) {
        if (wave > raid.getNumGroups(Difficulty.HARD)) {
            return ModEnchantmentProviders.RAID_PILLAGER_POST_WAVE_7;
        }
        return original.call();
    }
}
