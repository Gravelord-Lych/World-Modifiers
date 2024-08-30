package lych.worldmodifiers.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.datafixers.util.Unit;
import com.mojang.serialization.Codec;
import lych.worldmodifiers.util.DifficultyHelper;
import net.minecraft.Optionull;
import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.Options;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.options.OnlineOptionsScreen;
import net.minecraft.client.gui.screens.options.OptionsSubScreen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;
import java.util.List;

@Mixin(OnlineOptionsScreen.class)
public abstract class OnlineOptionsScreenMixin extends OptionsSubScreen {
    @Unique
    @Nullable
    private OptionInstance<Unit> worldModifiers$extremeDifficultyDisplay;

    private OnlineOptionsScreenMixin(Screen pLastScreen, Options pOptions, Component pTitle) {
        super(pLastScreen, pOptions, pTitle);
    }

    @Inject(method = "init", at = @At("TAIL"))
    private void worldModifiers$init(CallbackInfo ci) {
        if (worldModifiers$extremeDifficultyDisplay != null && list != null) {
            AbstractWidget widget = list.findOption(worldModifiers$extremeDifficultyDisplay);
            if (widget != null) {
                widget.active = false;
            }
        }
    }

    @Inject(method = "options", at = @At(value = "INVOKE", target = "Ljava/util/List;toArray([Ljava/lang/Object;)[Ljava/lang/Object;", remap = false))
    private void worldModifiers$addMoreOptions(Options options, Minecraft minecraft, CallbackInfoReturnable<OptionInstance<?>[]> cir, @Local List<OptionInstance<?>> optionInstances) {
        OptionInstance<Unit> extremeDifficultyOptionInstance = Optionull.map(
                minecraft.level,
                clientLevel -> {
                    boolean extremeDifficulty = DifficultyHelper.isExtremeDifficulty(clientLevel);
                    return new OptionInstance<>(
                            DifficultyHelper.EXTREME_DIFFICULTY.getString(),
                            OptionInstance.noTooltip(),
                            (component, unit) -> extremeDifficulty ? CommonComponents.OPTION_ON : CommonComponents.OPTION_OFF,
                            new OptionInstance.Enum<>(List.of(Unit.INSTANCE), Codec.EMPTY.codec()),
                            Unit.INSTANCE,
                            unit -> {}
                    );
                }
        );
        if (extremeDifficultyOptionInstance != null) {
            worldModifiers$extremeDifficultyDisplay = extremeDifficultyOptionInstance;
            optionInstances.add(extremeDifficultyOptionInstance);
        }
    }
}
