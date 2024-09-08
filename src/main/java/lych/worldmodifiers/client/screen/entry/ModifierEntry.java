package lych.worldmodifiers.client.screen.entry;

import lych.worldmodifiers.client.screen.EditModifiersScreen;
import lych.worldmodifiers.modifier.category.Modifier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;

import javax.annotation.Nullable;
import java.util.List;

public abstract class ModifierEntry extends BaseEntry {
    protected static final int ICON_OFFSET = 20;
    private final EditModifiersScreen editModifiersScreen;
    private final Modifier<?> modifier;
    private final ResourceLocation textureLocation;
    private final List<FormattedCharSequence> label;

    protected ModifierEntry(EditModifiersScreen editModifiersScreen, @Nullable List<FormattedCharSequence> tooltip, int entryDepth, Modifier<?> modifier, Component label) {
        super(tooltip, entryDepth);
        this.editModifiersScreen = editModifiersScreen;
        this.modifier = modifier;
        this.textureLocation = modifier.getTextureLocation();
        this.label = editModifiersScreen.getMinecraft().font.split(label, 175);
    }

    protected void renderLabel(GuiGraphics guiGraphics, int x, int y, int width, int height) {
        Minecraft minecraft = editModifiersScreen.getMinecraft();
        guiGraphics.blit(textureLocation, y + entryDepth * DEPTH_OFFSET, x, 0, 0, 18, 18, 18, 18);
        if (label.size() == 1) {
            guiGraphics.drawString(minecraft.font, label.getFirst(), y + ICON_OFFSET + entryDepth * DEPTH_OFFSET, x + 5, -1, false);
        } else if (label.size() >= 2) {
            guiGraphics.drawString(minecraft.font, label.get(0), y + ICON_OFFSET + entryDepth * DEPTH_OFFSET, x, -1, false);
            guiGraphics.drawString(minecraft.font, label.get(1), y + ICON_OFFSET + entryDepth * DEPTH_OFFSET, x + 10, -1, false);
        }
    }

    public Modifier<?> getModifier() {
        return modifier;
    }
}
