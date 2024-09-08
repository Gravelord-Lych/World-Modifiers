package lych.worldmodifiers.client.screen.entry;

import lych.worldmodifiers.client.screen.EditModifiersScreen;
import lych.worldmodifiers.modifier.ModifierMap;
import lych.worldmodifiers.modifier.category.Modifier;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;

import java.util.List;

public class IntModifierEntry extends ModifierEntry {
    private final EditBox input;

    public IntModifierEntry(EditModifiersScreen editModifiersScreen, ModifierMap modifierMap, Component label, List<FormattedCharSequence> tooltip, int entryDepth, String name, Modifier<Integer> modifier, int initialValue) {
        super(editModifiersScreen, tooltip, entryDepth, modifier, label);
        this.input = new EditBox(editModifiersScreen.getMinecraft().font, 10, 5, 44, 20, label.copy().append("\n").append(name).append("\n"));
        this.input.setValue(Integer.toString(initialValue));
        this.input.setResponder(text -> {
            try {
                int intValue = Integer.parseInt(text);
                int sanitizedValue = modifier.sanitizeValue(intValue);
                if (sanitizedValue != intValue) {
                    editModifiersScreen.markInvalid(this);
                    input.setTextColor(0xFFFF00);
                    return;
                }
                modifierMap.setModifierValue(modifier, sanitizedValue);
                editModifiersScreen.clearInvalid(this);
                input.setTextColor(0xE0E0E0);
            } catch (NumberFormatException e) {
                editModifiersScreen.markInvalid(this);
                input.setTextColor(0xFF0000);
            }
        });
        this.children.add(input);
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
        renderLabel(guiGraphics, top, left, width, height);
        input.setX(left + width - 45);
        input.setY(top);
        input.render(guiGraphics, mouseX, mouseY, partialTick);
    }
}
