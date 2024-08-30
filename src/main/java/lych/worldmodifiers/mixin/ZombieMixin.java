package lych.worldmodifiers.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import lych.worldmodifiers.util.DifficultyHelper;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Zombie.class)
public class ZombieMixin extends Monster {
    protected ZombieMixin(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }

    @WrapOperation(method = "populateDefaultEquipmentSlots", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/RandomSource;nextFloat()F"))
    private float worldModifiers$moreLikelyToHaveWeaponsIfExtreme(RandomSource instance, Operation<Float> original) {
        float randomValue = original.call(instance);
        if (DifficultyHelper.isExtremeDifficulty(level())) {
            return DifficultyHelper.modifyRandomFloatForHigherZombieSpawnWithWeaponsProbability(randomValue);
        }
        return randomValue;
    }
}
