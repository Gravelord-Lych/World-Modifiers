package lych.worldmodifiers.mixin;

import net.minecraft.world.entity.raid.Raid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Raid.RaiderType.class)
public interface RaiderTypeAccessor {
    @Accessor
    int[] getSpawnsPerWaveBeforeBonus();
}
