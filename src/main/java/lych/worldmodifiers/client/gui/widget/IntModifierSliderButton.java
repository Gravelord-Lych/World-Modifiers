package lych.worldmodifiers.client.gui.widget;

import lych.worldmodifiers.api.client.screen.ColorConstants;
import lych.worldmodifiers.api.modifier.Modifier;
import lych.worldmodifiers.client.screen.EditModifiersScreen;
import lych.worldmodifiers.modifier.ModifierConflictChecker;
import lych.worldmodifiers.modifier.ModifierMap;
import net.minecraft.client.gui.navigation.CommonInputs;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.client.gui.widget.ExtendedSlider;

import java.util.Set;

public class IntModifierSliderButton extends ExtendedSlider {
    private final EditModifiersScreen screen;
    private final ModifierMap modifierMap;
    private final ModifierConflictChecker conflictChecker;
    private final Modifier<Integer> modifier;
    private boolean highlighted;

    public IntModifierSliderButton(int x,
                                   int y,
                                   int width,
                                   int height,
                                   Component prefix,
                                   Component suffix,
                                   EditModifiersScreen screen,
                                   ModifierMap modifierMap,
                                   Modifier<Integer> modifier,
                                   String defaultValueText,
                                   double currentValue,
                                   double minValue,
                                   double maxValue,
                                   boolean drawString) {
        super(x, y, width, height, prefix, suffix, minValue, maxValue, currentValue, drawString);
        this.screen = screen;
        this.modifierMap = modifierMap;
        this.conflictChecker = screen.getConflictChecker();
        this.modifier = modifier;
        modifierMap.setModifierValue(modifier, (int) currentValue);
        adjustMessage();
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

    @Override
    protected void updateMessage() {
        super.updateMessage();
        adjustMessage();
        // The null-checks are necessary because this method is called in the super constructor
        // noinspection ConstantValue
        if (screen != null && modifierMap != null && conflictChecker != null) {
            Set<Modifier<?>> oldConflictModifiers = Set.copyOf(conflictChecker.getConflictModifiers());
            modifierMap.setModifierValue(modifier, getValueInt());
            if (modifierMap.valueChanged(modifier)) {
                conflictChecker.add(modifier);
            } else {
                conflictChecker.remove(modifier);
            }
            Set<Modifier<?>> newConflictModifiers = conflictChecker.getConflictModifiers();
            for (Modifier<?> previouslyConflictModifier : oldConflictModifiers) {
                if (!newConflictModifiers.contains(previouslyConflictModifier)) {
                    screen.removeWarn(previouslyConflictModifier);
                }
            }
            for (Modifier<?> conflictModifier : newConflictModifiers) {
                if (!oldConflictModifiers.contains(conflictModifier)) {
                    screen.warn(conflictModifier);
                }
            }
        }
    }

    public void highlight() {
        highlighted = true;
        adjustMessage();
    }

    public void unhighlight() {
        highlighted = false;
        adjustMessage();
    }

    private void adjustMessage() {
        if (highlighted) {
            setMessage(getMessage().copy().withStyle(style -> style.withItalic(true)));
        } else {
            setMessage(getMessage().copy().withStyle(style -> style.withItalic(false)));
        }
        // The null-check is necessary because this method is called indirectly in the super constructor
        // noinspection ConstantValue
        if (modifier != null) {
            int color;
            if (getValueInt() > modifier.getDefaultValue()) {
                color = ColorConstants.MODIFIER_VALUE_INCREASED_COLOR;
            } else if (getValueInt() < modifier.getDefaultValue()) {
                color = ColorConstants.MODIFIER_VALUE_DECREASED_COLOR;
            } else {
                color = -1;
            }
            if (color > 0) {
                setMessage(getMessage().copy().withStyle(style -> style.withColor(color)));
            }
        }
    }
}
