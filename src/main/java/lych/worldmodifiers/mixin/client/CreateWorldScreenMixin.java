package lych.worldmodifiers.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.serialization.Lifecycle;
import lych.worldmodifiers.util.DifficultyHelper;
import lych.worldmodifiers.util.mixin.IAdditionalLevelData;
import lych.worldmodifiers.util.mixin.ICreateWorldScreenMixin;
import lych.worldmodifiers.util.mixin.IWorldCreationUiStateMixin;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.screens.worldselection.CreateWorldScreen;
import net.minecraft.client.gui.screens.worldselection.WorldCreationUiState;
import net.minecraft.core.LayeredRegistryAccess;
import net.minecraft.server.RegistryLayer;
import net.minecraft.world.Difficulty;
import net.minecraft.world.level.storage.PrimaryLevelData;
import net.minecraft.world.level.storage.WorldData;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CreateWorldScreen.class)
public class CreateWorldScreenMixin implements ICreateWorldScreenMixin {
    @Final
    @Shadow
    WorldCreationUiState uiState;

    @Override
    public WorldCreationUiState worldModifiers$getUiState() {
        return uiState;
    }

    @SuppressWarnings("deprecation")
    @Inject(method = "createNewWorld", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/storage/WorldData;worldGenSettingsLifecycle()Lcom/mojang/serialization/Lifecycle;"))
    private void worldModifiers$applyAdditionalSettings(PrimaryLevelData.SpecialWorldProperty specialWorldProperty,
                                                        LayeredRegistryAccess<RegistryLayer> access,
                                                        Lifecycle worldGenSettingsLifecycle,
                                                        CallbackInfo ci,
                                                        @Local WorldData data) {
        ((IAdditionalLevelData) data).worldModifiers$setExtremeDifficulty(((IWorldCreationUiStateMixin) uiState).worldModifiers$isExtremeDifficulty());
    }

    @Mixin(CreateWorldScreen.GameTab.class)
    public static class GameTabMixin {
        @Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/WorldVersion;isStable()Z"))
        private void addExtremeDifficulty(CreateWorldScreen this$0, CallbackInfo ci, @Local GridLayout.RowHelper rowHelper) {
            CycleButton<Boolean> extremeModeButton = rowHelper.addChild(
                    CycleButton.onOffBuilder()
                            .withTooltip(value -> Tooltip.create(DifficultyHelper.EXTREME_DIFFICULTY))
                            .create(0, 0, 210, 20, DifficultyHelper.EXTREME_DIFFICULTY, (button, value) ->
                                    ((IWorldCreationUiStateMixin) ((ICreateWorldScreenMixin) this$0).worldModifiers$getUiState())
                                            .worldModifiers$setExtremeDifficulty(value))
            );
            ((ICreateWorldScreenMixin) this$0).worldModifiers$getUiState().addListener(uiState -> {
                extremeModeButton.setValue(((IWorldCreationUiStateMixin) uiState).worldModifiers$isExtremeDifficulty());
                extremeModeButton.active = uiState.getDifficulty() == Difficulty.HARD
                        && uiState.getGameMode() != WorldCreationUiState.SelectedGameMode.HARDCORE;
                extremeModeButton.setTooltip(Tooltip.create(DifficultyHelper.EXTREME_DIFFICULTY_INFO));
            });
        }
    }
}
