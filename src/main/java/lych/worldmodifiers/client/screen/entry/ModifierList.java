package lych.worldmodifiers.client.screen.entry;

import com.google.common.collect.ImmutableList;
import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import lych.worldmodifiers.WorldModifiersMod;
import lych.worldmodifiers.client.screen.EditModifiersScreen;
import lych.worldmodifiers.modifier.ModifierMap;
import lych.worldmodifiers.modifier.ModifierVisitor;
import lych.worldmodifiers.modifier.category.Modifier;
import lych.worldmodifiers.modifier.category.ModifierCategory;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;

import javax.annotation.Nullable;
import java.util.*;

public class ModifierList extends ContainerObjectSelectionList<BaseEntry> {
    public static final String EDIT_MODIFIER = "editModifier.default";
    private static final int ITEM_HEIGHT = 24;
    private final EditModifiersScreen editModifiersScreen;
    private final ModifierMap modifierMap;
    private final Map<ModifierCategory, ModifierCategoryEntry> entryMap = new HashMap<>();

    public ModifierList(EditModifiersScreen editModifiersScreen, ModifierMap modifierMap, Object2BooleanMap<ModifierCategory> foldedCategoryRecorder) {
        super(
                Minecraft.getInstance(),
                editModifiersScreen.width,
                editModifiersScreen.getLayout().getContentHeight(),
                editModifiersScreen.getLayout().getHeaderHeight(),
                ITEM_HEIGHT
        );
        this.editModifiersScreen = editModifiersScreen;
        this.modifierMap = modifierMap;
        addAllEntries();
        for (Iterator<BaseEntry> iterator = children().iterator(); iterator.hasNext(); ) {
            BaseEntry entry = iterator.next();
            if (entry instanceof ModifierCategoryEntry categoryEntry) {
                boolean folded = foldedCategoryRecorder.getOrDefault(categoryEntry.getCategory(), false);
                categoryEntry.setFolded(folded, false);
                if (categoryEntry.getCategory().getParent() != null && !allParentsAreNotFolded(categoryEntry.getCategory().getParent())) {
                    iterator.remove();
                }
            } else if (entry instanceof ModifierEntry modifierEntry) {
                if (!allParentsAreNotFolded(modifierEntry.getModifier().getParent())) {
                    iterator.remove();
                }
            } else {
                throw new AssertionError("Invalid entry type: " + entry.getClass().getSimpleName());
            }
        }
    }

    private void addAllEntries() {
        ModifierVisitor.of(this::addCategoryEntry, this::addModifierEntry).visitAll();
    }

    private boolean addCategoryEntry(int depth, @Nullable ModifierCategory parent, ModifierCategory category) {
        ModifierCategoryEntry entry = new ModifierCategoryEntry(editModifiersScreen, this, category, depth, category.getDisplayName().withStyle(ChatFormatting.BOLD, ChatFormatting.YELLOW));
        entryMap.put(category, entry);
        if (parent != null) {
            ModifierCategoryEntry entryForParentCategory = getEntryForCategory(parent);
            add2(parent, Objects.requireNonNull(entryForParentCategory), entry);
        } else {
            addEntry(entry);
        }
        return true;
    }

    private <T> boolean addModifierEntry(int depth, @Nullable ModifierCategory parent, Modifier<T> modifier) {
        Objects.requireNonNull(parent, "Modifier must have a parent category");
        Component displayName = modifier.getDisplayName();
        T value = modifierMap.getModifierValue(modifier);
        String defaultValue = modifier.getDefaultValue().toString();
        Component defaultValueText = WorldModifiersMod.prefixMsg(EDIT_MODIFIER, Component.literal(defaultValue)).withStyle(ChatFormatting.GRAY);
        String description = modifier.getDescription().getString();
        List<FormattedCharSequence> tooltip;
        String name;
        if (I18n.exists(description)) {
            ImmutableList.Builder<FormattedCharSequence> builder = ImmutableList.builder();
            Component translation = Component.translatable(description);
            editModifiersScreen.getMinecraft().font.split(translation, 150).forEach(builder::add);
            tooltip = builder.add(defaultValueText.getVisualOrderText()).build();
            name = translation.getString() + "\n" + defaultValueText.getString();
        } else {
            tooltip = List.of();
            name = defaultValueText.getString();
        }
        ModifierCategoryEntry entryForParentCategory = getEntryForCategory(parent);
        if (entryForParentCategory != null) {
            ModifierEntry entry = modifier.createEntry(editModifiersScreen, modifierMap, displayName, tooltip, depth, name, value);
            add2(parent, entryForParentCategory, entry);
        }
        return false;
    }

    private void add2(ModifierCategory parent, ModifierCategoryEntry entryForParentCategory, BaseEntry entry) {
        if (allParentsAreNotFolded(parent)) {
            addEntry(entry);
        }
        entryForParentCategory.addSubEntry(entry);
    }

    private boolean allParentsAreNotFolded(ModifierCategory parent) {
        ModifierCategory current = parent;
        while (current != null) {
            ModifierCategoryEntry entry = getEntryForCategory(current);
            if (entry != null && entry.isFolded()) {
                return false;
            }
            current = current.getParent();
        }
        return true;
    }

    @Nullable
    public ModifierCategoryEntry getEntryForCategory(@Nullable ModifierCategory category) {
        if (category == null) {
            return null;
        }
        return entryMap.get(category);
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.renderWidget(guiGraphics, mouseX, mouseY, partialTick);
        BaseEntry entry = getHovered();
        if (entry != null && entry.getTooltip() != null) {
            editModifiersScreen.setTooltipForNextRenderPass(entry.getTooltip());
        }
    }

    @Override
    public int getRowWidth() {
        return 310;
    }
}
