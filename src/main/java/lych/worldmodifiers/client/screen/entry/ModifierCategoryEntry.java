package lych.worldmodifiers.client.screen.entry;

import com.google.common.collect.ImmutableList;
import lych.worldmodifiers.api.modifier.category.ModifierCategory;
import lych.worldmodifiers.client.gui.widget.FoldButton;
import lych.worldmodifiers.client.screen.EditModifiersScreen;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

import static lych.worldmodifiers.api.client.screen.ModifierScreenConstants.*;

public class ModifierCategoryEntry extends EditModifiersScreenEntry {
    private final List<EditModifiersScreenEntry> subEntries = new ArrayList<>();
    private final EditModifiersScreen editModifiersScreen;
    private final ModifierList modifierList;
    private final ModifierCategory category;
    private final Component label;
    private final FoldButton foldButton;
    private boolean folded;

    public ModifierCategoryEntry(EditModifiersScreen editModifiersScreen,
                                 ModifierList modifierList,
                                 ModifierCategory category,
                                 int entryDepth,
                                 Component label) {
        super(null, entryDepth);
        this.editModifiersScreen = editModifiersScreen;
        this.modifierList = modifierList;
        this.category = category;
        this.label = label;
        this.foldButton = new FoldButton(10, 5, button -> {
            FoldButton fb = (FoldButton) button;
            fb.setFolded(!fb.isFolded());
            setFolded(fb.isFolded());
        });
        this.foldButton.setFolded(isFolded());
        this.children.add(foldButton);
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
        foldButton.setX(left + entryDepth * DEPTH_X_OFFSET);
        foldButton.setY(top);
        foldButton.render(guiGraphics, mouseX, mouseY, partialTick);
        guiGraphics.drawString(editModifiersScreen.getMinecraft().font,
                label,
                left + CATEGORY_ENTRY_ICON_OFFSET + entryDepth * DEPTH_X_OFFSET,
                top + CATEGORY_ENTRY_TOP_OFFSET,
                -1);
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
                output.add(NarratedElementType.TITLE, label);
            }
        }).addAll(super.narratables()).build();
    }

    public ModifierCategory getCategory() {
        return category;
    }

    public void addSubEntry(EditModifiersScreenEntry entry) {
        subEntries.add(entry);
    }

    public boolean isFolded() {
        return folded;
    }

    public void setFolded(boolean folded) {
        setFolded(folded, true);
    }

    public void setFolded(boolean folded, boolean updateChildren) {
        this.folded = folded;
        foldButton.setFolded(folded);
        editModifiersScreen.setFolded(getCategory(), folded);
        if (updateChildren) {
            if (folded) {
                recursivelyRemoveChildren();
            } else {
                recursivelyAddChildren(modifierList.children().indexOf(this) + 1);
            }
        }
    }

    private void recursivelyRemoveChildren() {
        for (EditModifiersScreenEntry entry : subEntries) {
            if (entry instanceof ModifierCategoryEntry) {
                ((ModifierCategoryEntry) entry).recursivelyRemoveChildren();
            }
            modifierList.children().remove(entry);
        }
    }

    private int recursivelyAddChildren(int index) {
        for (EditModifiersScreenEntry entry : subEntries) {
            modifierList.children().add(index, entry);
            if (entry instanceof ModifierCategoryEntry categoryEntry && !categoryEntry.isFolded()) {
                index = categoryEntry.recursivelyAddChildren(modifierList.children().indexOf(entry) + 1);
            }
            index++;
        }
        return index - 1;
    }
}
