package lych.worldmodifiers.client.screen.entry;

import lych.worldmodifiers.api.client.screen.entry.ModifierEntry;
import lych.worldmodifiers.api.modifier.Modifier;
import lych.worldmodifiers.client.screen.EditModifiersScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;

import javax.annotation.Nullable;
import java.util.List;

import static lych.worldmodifiers.api.client.screen.ScreenConstants.*;

public abstract class AbstractModifierEntry<T> extends EditModifiersScreenEntry implements ModifierEntry<T> {
    protected final EditModifiersScreen editModifiersScreen;
    private final Modifier<T> modifier;
    private final List<FormattedCharSequence> label;

    protected AbstractModifierEntry(ModifierEntryContext<T> context) {
        this(context.editModifiersScreen(), context.tooltip(), context.depth(), context.modifier(), context.displayName());
    }

    private AbstractModifierEntry(EditModifiersScreen editModifiersScreen, @Nullable List<FormattedCharSequence> tooltip, int entryDepth, Modifier<T> modifier, Component label) {
        super(tooltip, entryDepth);
        this.editModifiersScreen = editModifiersScreen;
        this.modifier = modifier;
        this.label = editModifiersScreen.getMinecraft().font.split(label, MAX_MODIFIER_NAME_WIDTH);
    }

    protected void renderLabel(GuiGraphics guiGraphics, int top, int left, Object modifierValue) {
        Minecraft minecraft = editModifiersScreen.getMinecraft();
        renderTexture(modifier, guiGraphics, top, left, entryDepth, modifierValue);
        if (label.size() == 1) {
            guiGraphics.drawString(minecraft.font, label.getFirst(), left + getDepthOffset() + ICON_SPACING, top + MODIFIER_ENTRY_NAME_LINE_HEIGHT / 2, -1, false);
        } else if (label.size() == 2) {
            guiGraphics.drawString(minecraft.font, label.get(0), left + getDepthOffset() + ICON_SPACING, top, -1, false);
            guiGraphics.drawString(minecraft.font, label.get(1), left + getDepthOffset() + ICON_SPACING, top + MODIFIER_ENTRY_NAME_LINE_HEIGHT, -1, false);
        } else if (label.size() >= 3) {
            guiGraphics.drawString(minecraft.font, label.get(0), left + getDepthOffset() + ICON_SPACING, top - MODIFIER_ENTRY_NAME_LINE_HEIGHT / 2, -1, false);
            guiGraphics.drawString(minecraft.font, label.get(1), left + getDepthOffset() + ICON_SPACING, top + MODIFIER_ENTRY_NAME_LINE_HEIGHT / 2, -1, false);
            guiGraphics.drawString(minecraft.font, label.get(2), left + getDepthOffset() + ICON_SPACING, top + MODIFIER_ENTRY_NAME_LINE_HEIGHT * 3 / 2, -1, false);
        }
    }

    private static <T> void renderTexture(Modifier<T> modifier, GuiGraphics guiGraphics, int top, int left, int entryDepth, Object modifierValue) {
        if (!modifier.getValueClass().isInstance(modifierValue)) {
            throw new IllegalArgumentException("Invalid value type for modifier %s. Required: %s, Provided: %s".formatted(
                    modifier.getFullName(),
                    modifier.getValueClass().getSimpleName(),
                    modifierValue.getClass().getSimpleName()));
        }
        guiGraphics.blit(modifier.getTextureLocation(modifier.getValueClass().cast(modifierValue)),
                left + entryDepth * DEPTH_X_OFFSET,
                top,
                0,
                0,
                TEXTURE_SIZE,
                TEXTURE_SIZE,
                TEXTURE_SIZE,
                TEXTURE_SIZE);
    }

    @Override
    public boolean mouseHovered(int rowLeft, int mouseX, int mouseY) {
        return mouseX >= rowLeft + getDepthOffset();
    }

    @Override
    public Modifier<T> getModifier() {
        return modifier;
    }

    protected int getDepthOffset() {
        return entryDepth * DEPTH_X_OFFSET;
    }
}
