package lych.worldmodifiers.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import lych.worldmodifiers.util.DifficultyHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.LockIconButton;
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
import net.minecraft.client.gui.layouts.LayoutElement;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.OptionsScreen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Difficulty;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;
import java.util.Objects;

@Mixin(OptionsScreen.class)
public abstract class OptionsScreenMixin extends Screen {
    @Shadow @Nullable private LockIconButton lockButton;
    @Shadow @Nullable private CycleButton<Difficulty> difficultyButton;

    @Shadow @Final private HeaderAndFooterLayout layout;
    @Nullable
    @Unique
    private CycleButton<Boolean> worldModifiers$extremeDifficultyButton;

    private OptionsScreenMixin(Component title) {
        super(title);
    }

    @Inject(method = "init", at = @At(value = "NEW", target = "()Lnet/minecraft/client/gui/layouts/GridLayout;"))
    private void worldModifier$addExtremeDifficultyButton(CallbackInfo ci, @Local(ordinal = 0) LinearLayout headerLayout) {
        LayoutElement extremeDifficultyButton = worldModifiers$createExtremeDifficultyButton();
        if (extremeDifficultyButton != null) {
            layout.setHeaderHeight((int) (layout.getHeaderHeight() * DifficultyHelper.OPTION_SCREEN_HEAD_HEIGHT_MULTIPLIER));
            LinearLayout modLayout = headerLayout.addChild(LinearLayout.horizontal().spacing(8));
            modLayout.addChild(extremeDifficultyButton);
        }
    }

    @ModifyArg(method = "createDifficultyButton", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/CycleButton$Builder;create(IIIILnet/minecraft/network/chat/Component;Lnet/minecraft/client/gui/components/CycleButton$OnValueChange;)Lnet/minecraft/client/gui/components/CycleButton;"))
    private static <T> CycleButton.OnValueChange<T> worldModifiers$updateExtremeDifficultyButton(CycleButton.OnValueChange<T> onValueChange) {
        return (button, value) -> {
            onValueChange.onValueChange(button, value);
            Minecraft minecraft = Minecraft.getInstance();
            OptionsScreen screen = worldModifiers$getOptionsScreen(minecraft);
            OptionsScreenMixin screenAccessor = (OptionsScreenMixin) (Object) screen;
            Objects.requireNonNull(screenAccessor.difficultyButton);
            Objects.requireNonNull(minecraft);
            Objects.requireNonNull(minecraft.level);
            screenAccessor.worldModifiers$resetExtremeDifficultyButtonStatus(screenAccessor.difficultyButton.getValue(), minecraft.level.getLevelData());
        };
    }

    @Unique
    private static OptionsScreen worldModifiers$getOptionsScreen(Minecraft minecraft) {
        if (minecraft.screen == null) {
            throw new NullPointerException("The screen is null");
        }
        if (!(minecraft.screen instanceof OptionsScreen screen)) {
            throw new IllegalStateException("Expected OptionsScreen but got " + minecraft.screen.getClass().getSimpleName());
        }
        return screen;
    }

    @Inject(method = "lockCallback", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/gui/components/LockIconButton;setLocked(Z)V",
            shift = At.Shift.AFTER))
    private void worldModifiers$lockButton(boolean lock, CallbackInfo ci) {
        Objects.requireNonNull(worldModifiers$extremeDifficultyButton);
        worldModifiers$extremeDifficultyButton.active = false;
    }

    @Nullable
    @Unique
    private LayoutElement worldModifiers$createExtremeDifficultyButton() {
        if (minecraft != null && minecraft.level != null && minecraft.hasSingleplayerServer() && difficultyButton != null) {
            ClientLevel.ClientLevelData levelData = minecraft.level.getLevelData();
            Difficulty difficulty = difficultyButton.getValue();
            worldModifiers$extremeDifficultyButton = worldModifiers$createExtremeDifficultyButton(0, 0, DifficultyHelper.isExtremeDifficultyInData(levelData), minecraft);
            worldModifiers$resetExtremeDifficultyButtonStatus(difficulty, levelData);
            return worldModifiers$extremeDifficultyButton;
        }
        return null;
    }

    @SuppressWarnings("DataFlowIssue")
    @Unique
    private void worldModifiers$resetExtremeDifficultyButtonStatus(Difficulty difficulty, ClientLevel.ClientLevelData levelData) {
        if (worldModifiers$extremeDifficultyButton == null) {
            return;
        }
        worldModifiers$extremeDifficultyButton.active = difficulty == Difficulty.HARD && !levelData.isHardcore() && lockButton != null && !lockButton.isLocked();
        if (!worldModifiers$extremeDifficultyButton.active) {
            worldModifiers$extremeDifficultyButton.setValue(worldModifiers$mayBeHardcore(levelData) || worldModifiers$difficultyLockedToExtreme(difficulty, levelData));
        } else {
            worldModifiers$extremeDifficultyButton.setValue(DifficultyHelper.isExtremeDifficultyInData(minecraft.level.getLevelData()));
        }
    }

    @Unique
    private boolean worldModifiers$mayBeHardcore(ClientLevel.ClientLevelData levelData) {
        return levelData.isHardcore() || lockButton == null;
    }

    @Unique
    private boolean worldModifiers$difficultyLockedToExtreme(Difficulty difficulty, ClientLevel.ClientLevelData levelData) {
        Objects.requireNonNull(lockButton);
        return difficulty == Difficulty.HARD && lockButton.isLocked() && DifficultyHelper.isExtremeDifficultyInData(levelData);
    }

    @Unique
    private static CycleButton<Boolean> worldModifiers$createExtremeDifficultyButton(int x, int y, boolean initialValue, Minecraft minecraft) {
        Objects.requireNonNull(minecraft);
        Objects.requireNonNull(minecraft.level);
        Objects.requireNonNull(minecraft.getConnection());
        return CycleButton.onOffBuilder()
                .withInitialValue(initialValue)
                .create(x, y, 150, 20, DifficultyHelper.EXTREME_DIFFICULTY, (button, value) -> DifficultyHelper.setExtremeDifficulty(minecraft.level, value));
    }
}
