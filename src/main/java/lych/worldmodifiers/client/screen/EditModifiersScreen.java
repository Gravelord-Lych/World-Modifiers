package lych.worldmodifiers.client.screen;

import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import lych.worldmodifiers.api.client.screen.ModifierScreenConstants;
import lych.worldmodifiers.api.modifier.category.ModifierCategory;
import lych.worldmodifiers.client.screen.entry.EditModifiersScreenEntry;
import lych.worldmodifiers.client.screen.entry.ModifierList;
import lych.worldmodifiers.modifier.ModifierMap;
import lych.worldmodifiers.util.MessageUtils;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;

public class EditModifiersScreen extends Screen {
    public static final Component TITLE = MessageUtils.prefixMsg("editModifier.title");
    public static final Component CONFIRMATION_SCREEN_TITLE = MessageUtils.prefixMsg("editModifier.confirmationScreen.title");
    public static final String CONFIRMATION_SCREEN_MESSAGE_KEY = "editModifier.confirmationScreen.message";
    private final HeaderAndFooterLayout layout = new HeaderAndFooterLayout(this);
    private final Object2BooleanMap<ModifierCategory> foldedStateRecorder;
    private final BiConsumer<? super Optional<ModifierMap>, ? super Optional<Object2BooleanMap<ModifierCategory>>> exitCallback;
    private final Set<EditModifiersScreenEntry> invalidEntries = new HashSet<>();
    private final ModifierMap modifierMap;
    private final ModifierMap oldModifierMap;
    @Nullable
    private ModifierList modifierList;
    @Nullable
    private Button doneButton;

    public EditModifiersScreen(ModifierMap modifierMap,
                               Object2BooleanMap<ModifierCategory> foldedStateRecorder,
                               BiConsumer<? super Optional<ModifierMap>, ? super Optional<Object2BooleanMap<ModifierCategory>>> exitCallback) {
        super(TITLE);
        this.modifierMap = modifierMap;
        this.oldModifierMap = modifierMap.copy();
        this.foldedStateRecorder = foldedStateRecorder;
        this.exitCallback = exitCallback;
    }

    @Override
    protected void init() {
        layout.addTitleHeader(TITLE, font);
        modifierList = layout.addToContents(new ModifierList(this, modifierMap, foldedStateRecorder));
        LinearLayout footer = layout.addToFooter(LinearLayout.horizontal().spacing(ModifierScreenConstants.SPACING));
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
        int modificationCount = oldModifierMap.difference(modifierMap);
        if (modificationCount >= ModifierScreenConstants.SHOW_CONFIRM_SCREEN_REQUIRED_MODIFICATION_COUNT) {
            getMinecraft().setScreen(new ConfirmScreen(yes -> {
                if (yes) {
                    cancel();
                } else {
                    getMinecraft().setScreen(this);
                }
            }, CONFIRMATION_SCREEN_TITLE, MessageUtils.prefixMsg(CONFIRMATION_SCREEN_MESSAGE_KEY, modificationCount)));
        } else {
            cancel();
        }
    }

    private void cancel() {
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

    public void markInvalid(EditModifiersScreenEntry ruleEntry) {
        invalidEntries.add(ruleEntry);
        updateDoneButton();
    }

    public void clearInvalid(EditModifiersScreenEntry ruleEntry) {
        invalidEntries.remove(ruleEntry);
        updateDoneButton();
    }
}
