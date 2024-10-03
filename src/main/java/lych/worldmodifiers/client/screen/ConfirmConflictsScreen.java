package lych.worldmodifiers.client.screen;

import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import lych.worldmodifiers.api.client.screen.ScreenConstants;
import lych.worldmodifiers.client.screen.entry.ConflictMessageList;
import lych.worldmodifiers.modifier.ConflictMessage;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;

import javax.annotation.Nullable;
import java.util.List;

public class ConfirmConflictsScreen extends Screen {
    private final List<List<ConflictMessage>> conflictMessageGroups;
    @Nullable
    private ConflictMessageList conflictMessageList;
    private final HeaderAndFooterLayout layout = new HeaderAndFooterLayout(this);
    protected final BooleanConsumer callback;

    public ConfirmConflictsScreen(BooleanConsumer callback,
                         Component title,
                         List<List<ConflictMessage>> conflictMessageGroups) {
        super(title);
        this.callback = callback;
        this.conflictMessageGroups = conflictMessageGroups;
    }

    @Override
    protected void init() {
        super.init();
        layout.addTitleHeader(title, font);
        conflictMessageList = layout.addToContents(
                new ConflictMessageList(width, layout.getContentHeight(), layout.getHeaderHeight(), conflictMessageGroups)
        );
        LinearLayout footer = layout.addToFooter(LinearLayout.horizontal().spacing(ScreenConstants.SPACING));
        footer.addChild(Button.builder(CommonComponents.GUI_PROCEED, button -> callback.accept(true)).build());
        footer.addChild(Button.builder(CommonComponents.GUI_CANCEL, button -> callback.accept(false)).build());
        layout.visitWidgets(widget -> {
            widget.setTabOrderGroup(1);
            addRenderableWidget(widget);
        });
        repositionElements();
    }

    @Override
    protected void repositionElements() {
        layout.arrangeElements();
        if (conflictMessageList != null) {
            conflictMessageList.updateSize(width, layout);
        }
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
            callback.accept(false);
            return true;
        } else {
            return super.keyPressed(keyCode, scanCode, modifiers);
        }
    }
}
