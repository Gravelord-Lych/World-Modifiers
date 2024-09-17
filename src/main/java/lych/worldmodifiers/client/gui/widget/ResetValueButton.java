package lych.worldmodifiers.client.gui.widget;

import lych.worldmodifiers.util.MessageUtils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import static lych.worldmodifiers.api.client.screen.ModifierScreenConstants.RESET_VALUE_BUTTON_SIZE;

public class ResetValueButton extends Button {
    public static final Component RESET_VALUE = MessageUtils.prefixMsg("narrator.button.resetValue");
    private static final ResourceLocation RESET_VALUE_BUTTON = MessageUtils.prefixTex("gui/reset_value_button.png");

    public ResetValueButton(int x, int y, OnPress onPress) {
        super(x,
                y,
                RESET_VALUE_BUTTON_SIZE,
                RESET_VALUE_BUTTON_SIZE,
                RESET_VALUE,
                onPress,
                DEFAULT_NARRATION);
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        guiGraphics.blit(RESET_VALUE_BUTTON, getX(), getY(), 0, isFocused() ? RESET_VALUE_BUTTON_SIZE : 0, width, height);
    }
}
