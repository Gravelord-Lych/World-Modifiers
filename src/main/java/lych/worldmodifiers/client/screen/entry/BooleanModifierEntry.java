package lych.worldmodifiers.client.screen.entry;

import lych.worldmodifiers.client.screen.EditModifiersScreen;
import lych.worldmodifiers.modifier.ModifierMap;
import lych.worldmodifiers.modifier.category.Modifier;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;

import java.util.List;

public class BooleanModifierEntry extends ModifierEntry {
    private final CycleButton<Boolean> checkbox;

    public BooleanModifierEntry(EditModifiersScreen editModifiersScreen, ModifierMap modifierMap, Component label, List<FormattedCharSequence> tooltip, int entryDepth, String name, Modifier<Boolean> modifier, boolean initialValue) {
        super(editModifiersScreen, tooltip, entryDepth, modifier, label);
        this.checkbox = CycleButton.onOffBuilder(initialValue)
                .displayOnlyValue()
                .withCustomNarration(button -> button.createDefaultNarrationMessage().append("\n").append(name))
                .create(10, 5, 44, 20, label, (button, value) -> modifierMap.setModifierValue(modifier, initialValue));
        this.children.add(checkbox);
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
        checkbox.setX(left + width - 45);
        checkbox.setY(top);
        checkbox.render(guiGraphics, mouseX, mouseY, partialTick);
    }
}
