package lych.worldmodifiers.mixin;

import com.mojang.serialization.Dynamic;
import com.mojang.serialization.Lifecycle;
import lych.worldmodifiers.util.DifficultyHelper;
import lych.worldmodifiers.util.mixin.IAdditionalLevelData;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.LevelSettings;
import net.minecraft.world.level.levelgen.WorldOptions;
import net.minecraft.world.level.storage.PrimaryLevelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PrimaryLevelData.class)
public class PrimaryLevelDataMixin implements IAdditionalLevelData {
    @Unique
    private boolean worldModifiers$extremeDifficulty;

    @SuppressWarnings("deprecation")
    @Inject(method = "parse", at = @At("RETURN"))
    private static <T> void worldModifiers$parse(Dynamic<T> tag, LevelSettings levelSettings, PrimaryLevelData.SpecialWorldProperty specialWorldProperty, WorldOptions worldOptions, Lifecycle worldGenSettingsLifecycle, CallbackInfoReturnable<PrimaryLevelData> cir) {
        PrimaryLevelData data = cir.getReturnValue();
        ((PrimaryLevelDataMixin) (Object) data).worldModifiers$extremeDifficulty = tag.get(DifficultyHelper.EXTREME_DIFFICULTY_TAG).asBoolean(false);
    }

    @Inject(method = "setTagData", at = @At("RETURN"))
    private void worldModifiers$setTagData(RegistryAccess registry, CompoundTag nbt, CompoundTag playerNBT, CallbackInfo ci) {
        nbt.putBoolean(DifficultyHelper.EXTREME_DIFFICULTY_TAG, worldModifiers$extremeDifficulty);
    }

    @Override
    public boolean worldModifiers$isExtremeDifficulty() {
        return worldModifiers$extremeDifficulty;
    }

    @Override
    public void worldModifiers$setExtremeDifficulty(boolean extremeDifficulty) {
        this.worldModifiers$extremeDifficulty = extremeDifficulty;
    }
}
