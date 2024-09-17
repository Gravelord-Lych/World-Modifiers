package lych.worldmodifiers.client.screen.entry;

import lych.worldmodifiers.api.modifier.Modifier;
import lych.worldmodifiers.client.gui.widget.IntModifierSliderButton;
import lych.worldmodifiers.client.gui.widget.ResetValueButton;
import lych.worldmodifiers.util.MessageUtils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.client.gui.widget.ExtendedSlider;

import static lych.worldmodifiers.api.client.screen.ModifierScreenConstants.*;

public class IntModifierEntry extends AbstractModifierEntry<Integer> {
    private final ExtendedSlider slider;
    private final ResetValueButton resetValueButton;

    public IntModifierEntry(ModifierEntryContext<Integer> context, Integer initialValue) {
        super(context);
        Modifier<Integer> modifier = context.modifier();
        if (!modifier.hasValueRange()) {
            throw new IllegalArgumentException("IntModifierEntry can only be used with modifiers that have a value range");
        }
        this.slider = new IntModifierSliderButton(10,
                5,
                100,
                SLIDER_HEIGHT,
                Component.empty(),
                MessageUtils.getPercentSignOrEmpty(modifier),
                context.modifierMap(),
                modifier,
                context.defaultValueText(),
                initialValue,
                modifier.getMinValue(),
                modifier.getMaxValue(),
                true
        );
        this.children.add(slider);
        this.resetValueButton = new ResetValueButton(10, 5, button -> {
            slider.setValue(modifier.getDefaultValue());
            context.modifierMap().setModifierValue(modifier, modifier.getDefaultValue());
        });
        this.children.add(resetValueButton);
    }

    @Override
    public void render(
            GuiGraphics guiGraphics,
            int index,
            int top,
            int left,
            int width,
            int height,
            int mouseX,
            int mouseY,
            boolean hovering,
            float partialTick
    ) {
        renderLabel(guiGraphics, top, left, slider.getValueInt());
        int otherObjectsWidth = getDepthOffset() + ICON_SPACING + MAX_TEXT_WIDTH + SLIDER_SPACING;
        slider.setWidth(width - otherObjectsWidth - RESET_VALUE_BUTTON_SIZE);
        slider.setX(left + otherObjectsWidth);
        slider.setY(top);
        slider.render(guiGraphics, mouseX, mouseY, partialTick);
        resetValueButton.setX(left + width - RESET_VALUE_BUTTON_SIZE);
        resetValueButton.setY(top);
        resetValueButton.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    @Override
    public boolean mouseHovered(int mouseX, int mouseY) {
        if (slider.isHovered() || resetValueButton.isHovered()) {
            return false;
        }
        return mouseX <= slider.getX() - SLIDER_SPACING;
    }
}
