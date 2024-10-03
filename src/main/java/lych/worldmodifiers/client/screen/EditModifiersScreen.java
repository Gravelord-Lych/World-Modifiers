package lych.worldmodifiers.client.screen;

import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import lych.worldmodifiers.api.client.screen.ScreenConstants;
import lych.worldmodifiers.api.modifier.Modifier;
import lych.worldmodifiers.api.modifier.category.ModifierCategory;
import lych.worldmodifiers.client.screen.entry.ModifierList;
import lych.worldmodifiers.modifier.ModifierConflictChecker;
import lych.worldmodifiers.modifier.ModifierMap;
import lych.worldmodifiers.util.MessageUtils;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.tabs.GridLayoutTab;
import net.minecraft.client.gui.components.tabs.TabManager;
import net.minecraft.client.gui.components.tabs.TabNavigationBar;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class EditModifiersScreen extends Screen {
    public static final Component TITLE = MessageUtils.prefixMsg("editModifier.title");
    public static final Component DISCARD_CHANGES_TITLE = MessageUtils.prefixMsg("editModifier.discardChanges.title");
    public static final Component CONFLICT_MODIFIERS_DETECTED_TITLE = MessageUtils.prefixMsg("editModifier.conflictModifiersDetected.title");
    public static final String DISCARD_CHANGES_MESSAGE_KEY = "editModifier.discardChanges.message";
    private final HeaderAndFooterLayout layout = new HeaderAndFooterLayout(this);
    private final Object2BooleanMap<ModifierCategory> foldedStateRecorder;
    private final CallbackOperation exitCallback;
    private final Set<Modifier<?>> warningModifiers = new HashSet<>();
    private final ModifierMap modifierMap;
    private final ModifierMap oldModifierMap;
    private final ModifierConflictChecker conflictChecker;
    private final TabManager tabManager = new TabManager(this::addRenderableWidget, this::removeWidget);
    @Nullable
    private ModifierList modifierList;
    @Nullable
    private TabNavigationBar tabNavigationBar;

    public EditModifiersScreen(ModifierMap modifierMap,
                               Object2BooleanMap<ModifierCategory> foldedStateRecorder,
                               CallbackOperation exitCallback) {
        super(TITLE);
        this.modifierMap = modifierMap;
        this.oldModifierMap = modifierMap.copy();
        this.conflictChecker = new ModifierConflictChecker(modifierMap);
        this.foldedStateRecorder = foldedStateRecorder;
        this.exitCallback = exitCallback;
    }

    @Override
    protected void init() {
        tabNavigationBar = TabNavigationBar.builder(tabManager, width)
                .addTabs(new EditModifiersTab(), new DummyTab(), new DummyTab())
                .build();
        addRenderableWidget(tabNavigationBar);
        LinearLayout footer = layout.addToFooter(LinearLayout.horizontal().spacing(ScreenConstants.SPACING));
        footer.addChild(Button.builder(CommonComponents.GUI_DONE, this::onDone).build());
        footer.addChild(Button.builder(CommonComponents.GUI_CANCEL, button -> onClose()).build());
        layout.visitWidgets(widget -> {
            widget.setTabOrderGroup(1);
            addRenderableWidget(widget);
        });
        tabNavigationBar.selectTab(ScreenConstants.EDIT_MODIFIER_TAB_INDEX, false);
        conflictChecker.getConflictModifiers().forEach(this::warn);
        repositionElements();
    }

    @Override
    protected void repositionElements() {
        if (tabNavigationBar != null) {
            tabNavigationBar.setWidth(this.width);
            tabNavigationBar.arrangeElements();
            int bottom = this.tabNavigationBar.getRectangle().bottom();
            ScreenRectangle rectangle = new ScreenRectangle(0, bottom, width, height - layout.getFooterHeight() - bottom);
            tabManager.setTabArea(rectangle);
            layout.setHeaderHeight(bottom);
            layout.arrangeElements();
            if (modifierList != null) {
                modifierList.updateSize(width, layout);
            }
        }
    }

    public HeaderAndFooterLayout getLayout() {
        return layout;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (tabNavigationBar != null && tabNavigationBar.keyPressed(keyCode)) {
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    private void onDone(Button button) {
        if (warningModifiers.isEmpty()) {
            exitCallback.accept(Optional.of(modifierMap), Optional.of(foldedStateRecorder));
            return;
        }
        getMinecraft().setScreen(new ConfirmConflictsScreen(yes -> {
            if (yes) {
                exitCallback.accept(Optional.of(modifierMap), Optional.of(foldedStateRecorder));
            } else {
                getMinecraft().setScreen(this);
            }
        },
                CONFLICT_MODIFIERS_DETECTED_TITLE,
                conflictChecker.getConflictMessageGroups()
        ));
    }

    @Override
    public void onClose() {
        int modificationCount = oldModifierMap.difference(modifierMap);
        if (modificationCount >= ScreenConstants.SHOW_CONFIRM_SCREEN_REQUIRED_MODIFICATION_COUNT) {
            getMinecraft().setScreen(new ConfirmScreen(yes -> {
                if (yes) {
                    cancel();
                } else {
                    getMinecraft().setScreen(this);
                }
            }, DISCARD_CHANGES_TITLE, MessageUtils.prefixMsg(DISCARD_CHANGES_MESSAGE_KEY, modificationCount)));
        } else {
            cancel();
        }
    }

    private void cancel() {
        exitCallback.accept(Optional.empty(), Optional.empty());
    }

    public void setFolded(ModifierCategory category, boolean folded) {
        foldedStateRecorder.put(category, folded);
    }

    public void warn(Modifier<?> modifier) {
        warningModifiers.add(modifier);
        if (modifierList != null) {
            modifierList.getEntryForModifier(modifier).highlight();
        }
    }

    public void removeWarn(Modifier<?> modifier) {
        warningModifiers.remove(modifier);
        if (modifierList != null) {
            modifierList.getEntryForModifier(modifier).unhighlight();
        }
    }

    public ModifierConflictChecker getConflictChecker() {
        return conflictChecker;
    }

    public class EditModifiersTab extends GridLayoutTab {
        public EditModifiersTab() {
            super(TITLE);
            GridLayout.RowHelper rowHelper = layout.createRowHelper(1);
            modifierList = rowHelper.addChild(
                    new ModifierList(EditModifiersScreen.this, modifierMap, foldedStateRecorder));
        }
    }

    public static class DummyTab extends GridLayoutTab {
        public DummyTab() {
            super(Component.literal("Dummy"));
            GridLayout.RowHelper rowHelper = this.layout.rowSpacing(8).createRowHelper(1);
            rowHelper.addChild(Button.builder(Component.literal("foo"), a -> {}).width(210).build());
            rowHelper.addChild(Button.builder(Component.literal("bar"), b -> {}).width(210).build());
        }
    }

    @FunctionalInterface
    public interface CallbackOperation {
        @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
        void accept(Optional<ModifierMap> modifierMapOptional,
                    Optional<Object2BooleanMap<ModifierCategory>> foldedStateRecorderOptional);
    }
}
