package lych.worldmodifiers.client.screen.entry;

import com.google.common.collect.ImmutableList;
import lych.worldmodifiers.api.client.screen.ScreenConstants;
import lych.worldmodifiers.modifier.ConflictMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.util.FormattedCharSequence;

import java.util.List;

import static lych.worldmodifiers.api.client.screen.ScreenConstants.CONFLICT_MESSAGE_LINE_HEIGHT;
import static lych.worldmodifiers.api.client.screen.ScreenConstants.MODIFIER_ENTRY_NAME_LINE_HEIGHT;

public class ConflictMessageList extends ContainerObjectSelectionList<ConflictMessageList.ConflictMessageEntry> {
    public ConflictMessageList(int width, int height, int y, List<List<ConflictMessage>> conflictMessageGroups) {
        super(Minecraft.getInstance(), width, height, y, ScreenConstants.CONFLICT_MESSAGE_SPACING);
        for (List<ConflictMessage> conflictMessages : conflictMessageGroups) {
            for (ConflictMessage conflictMessage : conflictMessages) {
                addEntry(new ConflictMessageEntry(conflictMessage));
            }
        }
    }

    @Override
    public int getRowWidth() {
        return ScreenConstants.MAX_CONFLICT_MESSAGE_WIDTH;
    }

    public static class ConflictMessageEntry extends Entry<ConflictMessageEntry> {
        private final ConflictMessage conflictMessage;
        private final List<FormattedCharSequence> label;

        public ConflictMessageEntry(ConflictMessage conflictMessage) {
            this.conflictMessage = conflictMessage;
            label = Minecraft.getInstance().font.split(conflictMessage.getMessage(), ScreenConstants.MAX_CONFLICT_MESSAGE_WIDTH);
        }

        @Override
        public void render(GuiGraphics guiGraphics, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean hovering, float partialTick) {
            Minecraft minecraft = Minecraft.getInstance();
            if (label.size() == 1) {
                guiGraphics.drawCenteredString(minecraft.font, label.getFirst(), left + width / 2, top + CONFLICT_MESSAGE_LINE_HEIGHT / 2, -1);
            } else if (label.size() >= 2) {
                guiGraphics.drawCenteredString(minecraft.font, label.get(0), left + width / 2, top, -1);
                guiGraphics.drawCenteredString(minecraft.font, label.get(1), left + width / 2, top + MODIFIER_ENTRY_NAME_LINE_HEIGHT, -1);
            }
        }

        @Override
        public List<? extends NarratableEntry> narratables() {
            return ImmutableList.<NarratableEntry>builder().add(new NarratableEntry() {
                @Override
                public NarrationPriority narrationPriority() {
                    return NarrationPriority.HOVERED;
                }

                @Override
                public void updateNarration(NarrationElementOutput output) {
                    output.add(NarratedElementType.TITLE, conflictMessage.getMessage());
                }
            }).build();
        }

        @Override
        public List<? extends GuiEventListener> children() {
            return List.of();
        }
    }
}
