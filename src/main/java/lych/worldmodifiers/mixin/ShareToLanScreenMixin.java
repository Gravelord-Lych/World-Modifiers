package lych.worldmodifiers.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.screens.ShareToLanScreen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.GameType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(ShareToLanScreen.class)
public class ShareToLanScreenMixin {
    @Shadow private GameType gameMode;

    @Shadow private boolean commands;

    @WrapOperation(method = "init",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/components/CycleButton$Builder;create(IIIILnet/minecraft/network/chat/Component;Lnet/minecraft/client/gui/components/CycleButton$OnValueChange;)Lnet/minecraft/client/gui/components/CycleButton;"),
            slice = @Slice(to = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/CycleButton;onOffBuilder(Z)Lnet/minecraft/client/gui/components/CycleButton$Builder;")))
    private CycleButton<GameType> worldModifiers$lockGameTypeToSurvivalInHardcoreMode(CycleButton.Builder<GameType> instance, int x, int y, int width, int height, Component name, CycleButton.OnValueChange<GameType> onValueChange, Operation<CycleButton<GameType>> operation) {
        CycleButton<GameType> button = operation.call(instance, x, y, width, height, name, onValueChange);
        ClientLevel level = Minecraft.getInstance().level;
        if (level != null && level.getLevelData().isHardcore()) {
            button.active = false;
            button.setValue(GameType.SURVIVAL);
            gameMode = GameType.SURVIVAL;
        }
        return button;
    }

    @WrapOperation(method = "init",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/components/CycleButton$Builder;create(IIIILnet/minecraft/network/chat/Component;Lnet/minecraft/client/gui/components/CycleButton$OnValueChange;)Lnet/minecraft/client/gui/components/CycleButton;"),
            slice = @Slice(from = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/CycleButton;onOffBuilder(Z)Lnet/minecraft/client/gui/components/CycleButton$Builder;")))
    private CycleButton<Boolean> worldModifiers$disableCommandsInHardcoreMode(CycleButton.Builder<Boolean> instance, int x, int y, int width, int height, Component name, CycleButton.OnValueChange<Boolean> onValueChange, Operation<CycleButton<Boolean>> operation) {
        CycleButton<Boolean> button = operation.call(instance, x, y, width, height, name, onValueChange);
        ClientLevel level = Minecraft.getInstance().level;
        if (level != null && level.getLevelData().isHardcore()) {
            button.active = false;
            button.setValue(false);
            commands = false;
        }
        return button;
    }
}
