package lych.worldmodifiers.client.gui.component;

import lych.worldmodifiers.WorldModifiersMod;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;

public class FoldButton extends Button {
    public static final Component FOLD = WorldModifiersMod.prefixMsg("narrator.button.fold");
    public static final Component FOLDED = WorldModifiersMod.prefixMsg("narrator.button.fold.folded");
    public static final Component UNFOLDED = WorldModifiersMod.prefixMsg("narrator.button.fold.unfolded");
    private static final ResourceLocation FOLD_BUTTON = WorldModifiersMod.prefixTex("gui/fold_button.png");
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
        FOLDED_HOVER(0, 24, 0, 0),
        UNFOLDED(24, 0, -6, 4),
        UNFOLDED_HOVER(24, 24, -6, 4);

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
