package lych.worldmodifiers.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.serialization.Lifecycle;
import it.unimi.dsi.fastutil.objects.Object2BooleanArrayMap;
import lych.worldmodifiers.client.screen.EditModifiersScreen;
import lych.worldmodifiers.modifier.StoredModifiers;
import lych.worldmodifiers.util.DifficultyHelper;
import lych.worldmodifiers.util.ModifiersHelper;
import lych.worldmodifiers.util.mixin.IAdditionalLevelData;
import lych.worldmodifiers.util.mixin.IWorldCreationUiStateMixin;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.screens.worldselection.CreateWorldScreen;
import net.minecraft.client.gui.screens.worldselection.WorldCreationUiState;
import net.minecraft.core.LayeredRegistryAccess;
import net.minecraft.server.RegistryLayer;
import net.minecraft.world.Difficulty;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraft.world.level.storage.PrimaryLevelData;
import net.minecraft.world.level.storage.WorldData;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(CreateWorldScreen.class)
public class CreateWorldScreenMixin {
    @Final
    @Shadow
    WorldCreationUiState uiState;

    @SuppressWarnings("deprecation")
    @Inject(method = "createNewWorld", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/storage/WorldData;worldGenSettingsLifecycle()Lcom/mojang/serialization/Lifecycle;"))
    private void worldModifiers$applyAdditionalSettings(PrimaryLevelData.SpecialWorldProperty specialWorldProperty,
                                                        LayeredRegistryAccess<RegistryLayer> access,
                                                        Lifecycle worldGenSettingsLifecycle,
                                                        CallbackInfo ci,
                                                        @SuppressWarnings("OptionalUsedAsFieldOrParameterType") @Local
                                                            Optional<LevelStorageSource.LevelStorageAccess> levelStorageAccessOptional,
                                                        @Local WorldData data) {
        ((IAdditionalLevelData) data).worldModifiers$setExtremeDifficulty(((IWorldCreationUiStateMixin) uiState).worldModifiers$isExtremeDifficulty());
        levelStorageAccessOptional.ifPresent(levelStorageAccess -> {
            StoredModifiers storedModifiers = new StoredModifiers(levelStorageAccess.getLevelPath(StoredModifiers.MODIFIERS).toFile());
            storedModifiers.reloadModifiersFrom(((IWorldCreationUiStateMixin) uiState).worldModifiers$getModifierMap());
            storedModifiers.save();
        });
    }

    @Mixin(CreateWorldScreen.GameTab.class)
    public static class GameTabMixin {
        @Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/WorldVersion;isStable()Z"))
        private void addExtremeDifficulty(CreateWorldScreen this$0, CallbackInfo ci, @Local GridLayout.RowHelper rowHelper) {
            CycleButton<Boolean> extremeModeButton = rowHelper.addChild(
                    CycleButton.onOffBuilder()
                            .withTooltip(value -> Tooltip.create(DifficultyHelper.EXTREME_DIFFICULTY))
                            .create(0, 0, 210, 20, DifficultyHelper.EXTREME_DIFFICULTY, (button, value) ->
                                    ((IWorldCreationUiStateMixin) this$0.getUiState())
                                            .worldModifiers$setExtremeDifficulty(value))
            );
            this$0.getUiState().addListener(uiState -> {
                extremeModeButton.setValue(((IWorldCreationUiStateMixin) uiState).worldModifiers$isExtremeDifficulty());
                extremeModeButton.active = uiState.getDifficulty() == Difficulty.HARD
                        && uiState.getGameMode() != WorldCreationUiState.SelectedGameMode.HARDCORE;
                extremeModeButton.setTooltip(Tooltip.create(DifficultyHelper.EXTREME_DIFFICULTY_INFO));
            });
        }
    }

    @Mixin(CreateWorldScreen.MoreTab.class)
    public static class MoreTabMixin {
        @Final
        @Shadow
        CreateWorldScreen this$0;

        @Inject(method = "<init>", at = @At("TAIL"))
        private void worldModifiers$addModifiersButton(CreateWorldScreen this$0, CallbackInfo ci, @Local GridLayout.RowHelper rowHelper) {
            rowHelper.addChild(
                    Button.builder(
                                    ModifiersHelper.MODIFIERS,
                                    button -> worldModifiers$openModifiersScreen()
                            )
                            .width(210)
                            .build()
            );
        }

        @Unique
        private void worldModifiers$openModifiersScreen() {
            Minecraft minecraft = Minecraft.getInstance();
            IWorldCreationUiStateMixin uiState = worldModifiers$getUiStateAccessor();
            minecraft.setScreen(new EditModifiersScreen(uiState.worldModifiers$getModifierMap().copy(),
                    new Object2BooleanArrayMap<>(uiState.worldModifiers$getFoldedStateRecorder()),
                    (modifierMapOptional, foldedStateRecorderOptional) -> {
                        minecraft.setScreen(this$0);
                        modifierMapOptional.ifPresent(uiState::worldModifiers$setModifierMap);
                        foldedStateRecorderOptional.ifPresent(uiState::worldModifiers$setFoldedStateRecorder);
                    }));
        }

        @Unique
        private IWorldCreationUiStateMixin worldModifiers$getUiStateAccessor() {
            return (IWorldCreationUiStateMixin) this$0.getUiState();
        }
    }
}
