package lych.worldmodifiers.client.screen;

import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import lych.worldmodifiers.WorldModifiersMod;
import lych.worldmodifiers.client.screen.entry.BaseEntry;
import lych.worldmodifiers.client.screen.entry.ModifierList;
import lych.worldmodifiers.modifier.ModifierMap;
import lych.worldmodifiers.modifier.category.ModifierCategory;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;

public class EditModifiersScreen extends Screen {
    public static final Component TITLE = WorldModifiersMod.prefixMsg("editModifier.title");
    private static final int SPACING = 8;
    private final HeaderAndFooterLayout layout = new HeaderAndFooterLayout(this);
    private final Object2BooleanMap<ModifierCategory> foldedStateRecorder;
    private final BiConsumer<? super Optional<ModifierMap>, ? super Optional<Object2BooleanMap<ModifierCategory>>> exitCallback;
    private final Set<BaseEntry> invalidEntries = new HashSet<>();
    private final ModifierMap modifierMap;
    @Nullable
    private ModifierList modifierList;
    @Nullable
    private Button doneButton;

    public EditModifiersScreen(ModifierMap modifierMap,
                               Object2BooleanMap<ModifierCategory> foldedStateRecorder,
                               BiConsumer<? super Optional<ModifierMap>, ? super Optional<Object2BooleanMap<ModifierCategory>>> exitCallback) {
        super(TITLE);
        this.modifierMap = modifierMap;
        this.foldedStateRecorder = foldedStateRecorder;
        this.exitCallback = exitCallback;
    }

    @Override
    protected void init() {
        layout.addTitleHeader(TITLE, font);
        modifierList = layout.addToContents(new ModifierList(this, modifierMap, foldedStateRecorder));
        LinearLayout footer = layout.addToFooter(LinearLayout.horizontal().spacing(SPACING));
        doneButton = footer.addChild(
                Button.builder(CommonComponents.GUI_DONE, button -> exitCallback.accept(Optional.of(modifierMap), Optional.of(foldedStateRecorder))).build()
        );
        footer.addChild(Button.builder(CommonComponents.GUI_CANCEL, button -> onClose()).build());
        layout.visitWidgets(this::addRenderableWidget);
        repositionElements();
    }

    @Override
    protected void repositionElements() {
        layout.arrangeElements();
        if (modifierList != null) {
            modifierList.updateSize(width, layout);
        }
    }

    public HeaderAndFooterLayout getLayout() {
        return layout;
    }

    @Override
    public void onClose() {
        exitCallback.accept(Optional.empty(), Optional.empty());
    }

    private void updateDoneButton() {
        if (doneButton != null) {
            doneButton.active = invalidEntries.isEmpty();
        }
    }

    public void setFolded(ModifierCategory category, boolean folded) {
        foldedStateRecorder.put(category, folded);
    }

    public void markInvalid(BaseEntry ruleEntry) {
        invalidEntries.add(ruleEntry);
        updateDoneButton();
    }

    public void clearInvalid(BaseEntry ruleEntry) {
        invalidEntries.remove(ruleEntry);
        updateDoneButton();
    }
}
