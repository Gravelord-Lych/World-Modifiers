package lych.worldmodifiers.client.gui.widget;

import lych.worldmodifiers.api.modifier.Modifier;
import lych.worldmodifiers.modifier.ModifierMap;
import net.minecraft.client.gui.navigation.CommonInputs;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.client.gui.widget.ExtendedSlider;

public class IntModifierSliderButton extends ExtendedSlider {
    private final ModifierMap modifierMap;
    private final Modifier<Integer> modifier;

    public IntModifierSliderButton(int x,
                                   int y,
                                   int width,
                                   int height,
                                   Component prefix,
                                   Component suffix,
                                   ModifierMap modifierMap,
                                   Modifier<Integer> modifier,
                                   String defaultValueText,
                                   double currentValue,
                                   double stepSize,
                                   int precision,
                                   boolean drawString,
                                   double minValue,
                                   double maxValue) {
        super(x, y, width, height, prefix, suffix, minValue, maxValue, currentValue, stepSize, precision, drawString);
        this.modifierMap = modifierMap;
        this.modifier = modifier;
        modifierMap.setModifierValue(modifier, (int) currentValue);
        setMessage(getMessage().copy().append("\n").append(defaultValueText));
    }

    public IntModifierSliderButton(int x,
                                   int y,
                                   int width,
                                   int height,
                                   Component prefix,
                                   Component suffix,
                                   ModifierMap modifierMap,
                                   Modifier<Integer> modifier,
                                   String defaultValueText,
                                   double currentValue,
                                   double minValue,
                                   double maxValue,
                                   boolean drawString) {
        super(x, y, width, height, prefix, suffix, minValue, maxValue, currentValue, drawString);
        this.modifierMap = modifierMap;
        this.modifier = modifier;
        modifierMap.setModifierValue(modifier, (int) currentValue);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (CommonInputs.selected(keyCode)) {
            canChangeValue = !canChangeValue;
            return true;
        }
        if (canChangeValue) {
            super.keyPressed(keyCode, scanCode, modifiers);
        }
        return canChangeValue;
    }

    @SuppressWarnings("ConstantValue")
    @Override
    protected void updateMessage() {
        super.updateMessage();
        if (modifierMap != null) {
            modifierMap.setModifierValue(modifier, getValueInt());
        }
    }
}
