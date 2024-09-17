package lych.worldmodifiers.client.gui.widget;

import lych.worldmodifiers.util.MessageUtils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;

import static lych.worldmodifiers.api.client.screen.ModifierScreenConstants.*;

public class FoldButton extends Button {
    public static final Component FOLD = MessageUtils.prefixMsg("narrator.button.fold");
    public static final Component FOLDED = MessageUtils.prefixMsg("narrator.button.fold.folded");
    public static final Component UNFOLDED = MessageUtils.prefixMsg("narrator.button.fold.unfolded");
    private static final ResourceLocation FOLD_BUTTON = MessageUtils.prefixTex("gui/fold_button.png");
    private boolean folded;

    public FoldButton(int x, int y, OnPress onPress) {
        super(x, y, 24, 24, FOLD, onPress, DEFAULT_NARRATION);
    }

    @Override
    protected MutableComponent createNarrationMessage() {
        return CommonComponents.joinForNarration(super.createNarrationMessage(), isFolded() ? FOLDED : UNFOLDED);
    }

    public boolean isFolded() {
        return folded;
    }

    public void setFolded(boolean folded) {
        this.folded = folded;
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        Icon icon;
        if (isHoveredOrFocused()) {
            icon = isFolded() ? Icon.FOLDED_HOVER : Icon.UNFOLDED_HOVER;
        } else {
            icon = isFolded() ? Icon.FOLDED : Icon.UNFOLDED;
        }
        guiGraphics.blit(FOLD_BUTTON, getX() + icon.xOffs, getY() + icon.yOffs, icon.u, icon.v, width, height);
    }

    private enum Icon {
        FOLDED(0, 0, 0, 0),
        FOLDED_HOVER(0, FOLD_ICON_SIZE, 0, 0),
        UNFOLDED(FOLD_ICON_SIZE, 0, UNFOLDED_FOLD_ICON_X_OFFSET, UNFOLDED_FOLD_ICON_Y_OFFSET),
        UNFOLDED_HOVER(FOLD_ICON_SIZE, FOLD_ICON_SIZE, UNFOLDED_FOLD_ICON_X_OFFSET, UNFOLDED_FOLD_ICON_Y_OFFSET);

        private final int u;
        private final int v;
        private final int xOffs;
        private final int yOffs;

        Icon(int u, int v, int xOffs, int yOffs) {
            this.u = u;
            this.v = v;
            this.xOffs = xOffs;
            this.yOffs = yOffs;
        }
    }
}
