package lych.worldmodifiers.client.screen.entry;

import com.google.common.collect.ImmutableList;
import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import lych.worldmodifiers.WorldModifiersMod;
import lych.worldmodifiers.api.client.screen.ScreenConstants;
import lych.worldmodifiers.api.client.screen.entry.ModifierEntry;
import lych.worldmodifiers.api.modifier.Modifier;
import lych.worldmodifiers.api.modifier.category.ModifierCategory;
import lych.worldmodifiers.client.screen.EditModifiersScreen;
import lych.worldmodifiers.modifier.ModifierMap;
import lych.worldmodifiers.modifier.ModifierVisitor;
import lych.worldmodifiers.util.MessageUtils;
import lych.worldmodifiers.util.ModifierHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;

import javax.annotation.Nullable;
import java.util.*;

public class ModifierList extends ContainerObjectSelectionList<EditModifiersScreenEntry> {
    public static final String EDIT_MODIFIER_MESSAGE_KEY = "editModifier.default";
    private final EditModifiersScreen editModifiersScreen;
    private final ModifierMap modifierMap;
    private final Map<ModifierCategory, ModifierCategoryEntry> categoryEntryMap = new HashMap<>();
    private final Map<Modifier<?>, ModifierEntry<?>> modifierEntryMap = new HashMap<>();

    public ModifierList(EditModifiersScreen editModifiersScreen, ModifierMap modifierMap, Object2BooleanMap<ModifierCategory> foldedCategoryRecorder) {
        super(
                Minecraft.getInstance(),
                editModifiersScreen.width,
                editModifiersScreen.getLayout().getContentHeight(),
                editModifiersScreen.getLayout().getHeaderHeight(),
                ScreenConstants.ITEM_HEIGHT
        );
        this.editModifiersScreen = editModifiersScreen;
        this.modifierMap = modifierMap;
        addAllEntries();
        for (Iterator<EditModifiersScreenEntry> iterator = children().iterator(); iterator.hasNext(); ) {
            EditModifiersScreenEntry entry = iterator.next();
            if (entry instanceof ModifierCategoryEntry categoryEntry) {
                boolean folded = foldedCategoryRecorder.getOrDefault(categoryEntry.getCategory(), false);
                categoryEntry.setFolded(folded, false);
                if (categoryEntry.getCategory().getParent() != null && !allParentsAreNotFolded(categoryEntry.getCategory().getParent())) {
                    iterator.remove();
                }
            } else if (entry instanceof ModifierEntry<?> modifierEntry) {
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
        try {
            ModifierCategoryEntry entry = new ModifierCategoryEntry(editModifiersScreen, this, category, depth, category.getDisplayName().withStyle(ChatFormatting.BOLD, ChatFormatting.YELLOW));
            categoryEntryMap.put(category, entry);
            if (parent != null) {
                ModifierCategoryEntry entryForParentCategory = getEntryForCategory(parent);
                add2(parent, Objects.requireNonNull(entryForParentCategory), entry);
            } else {
                addEntry(entry);
            }
        } catch (Exception e) {
            WorldModifiersMod.LOGGER.error("Failed to add category entry", e);
        }
        return true;
    }

    private <T> boolean addModifierEntry(int depth, @Nullable ModifierCategory parent, Modifier<T> modifier) {
        try {
            Objects.requireNonNull(parent, "Modifier must have a parent category");
            Component displayName = modifier.getDisplayName();
            T value = modifierMap.getModifierValue(modifier);
            String defaultValue = modifier.getDefaultValue().toString();
            Component defaultValueComponent = MessageUtils.prefixMsg(EDIT_MODIFIER_MESSAGE_KEY,
                    Component.literal(defaultValue).append(MessageUtils.getPercentSignOrEmpty(modifier)))
                    .withStyle(ChatFormatting.GRAY);
            ModifierDescriptions descriptions = createDescriptions(modifier, defaultValueComponent);
            ModifierCategoryEntry entryForParentCategory = getEntryForCategory(parent);
            if (entryForParentCategory != null) {
                ModifierEntry<T> entry = ModifierHelper.createEntry(modifier.getEntryClass(),
                        modifier.getValueClass(),
                        editModifiersScreen,
                        modifierMap,
                        displayName,
                        descriptions.tooltip(),
                        depth,
                        descriptions.defaultValueText(),
                        modifier,
                        value);
                modifierEntryMap.put(modifier, entry);
                add2(parent, entryForParentCategory, entry);
            }
        } catch (Exception e) {
            WorldModifiersMod.LOGGER.error("Failed to add modifier entry", e);
        }
        return false;
    }

    private <T> ModifierDescriptions createDescriptions(Modifier<T> modifier, Component defaultValueComponent) {
        List<FormattedCharSequence> tooltip;
        String defaultValueText;
        if (I18n.exists(MessageUtils.getTranslationKey(modifier.getDescription()))) {
            String description = modifier.getDescription().getString();
            ImmutableList.Builder<FormattedCharSequence> builder = ImmutableList.builder();
            Component descriptionTranslation = Component.translatable(description);
            editModifiersScreen.getMinecraft().font.split(descriptionTranslation, ScreenConstants.MODIFIER_TOOLTIP_MAX_WIDTH).forEach(builder::add);
            if (I18n.exists(MessageUtils.getTranslationKey(modifier.getWarning()))) {
                String warning = modifier.getWarning().getString();
                Component warningTranslation = Component.translatable(warning).withStyle(ChatFormatting.GOLD);
                editModifiersScreen.getMinecraft().font.split(warningTranslation, ScreenConstants.MODIFIER_TOOLTIP_MAX_WIDTH).forEach(builder::add);
            }
            tooltip = builder.add(defaultValueComponent.getVisualOrderText()).build();
            defaultValueText = descriptionTranslation.getString() + "\n" + defaultValueComponent.getString();
        } else {
            tooltip = List.of();
            defaultValueText = defaultValueComponent.getString();
        }
        return new ModifierDescriptions(tooltip, defaultValueText);
    }

    private void add2(ModifierCategory parent, ModifierCategoryEntry entryForParentCategory, ModifierEntry<?> entry) {
        if (!(entry instanceof EditModifiersScreenEntry)) {
            throw new AssertionError("Invalid entry type: " + entry.getClass().getSimpleName());
        }
        add2(parent, entryForParentCategory, (EditModifiersScreenEntry) entry);
    }

    private void add2(ModifierCategory parent, ModifierCategoryEntry entryForParentCategory, EditModifiersScreenEntry entry) {
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
        return categoryEntryMap.get(category);
    }

    @SuppressWarnings("unchecked")
    public <T> ModifierEntry<T> getEntryForModifier(Modifier<T> modifier) {
        return (ModifierEntry<T>) modifierEntryMap.get(modifier);
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.renderWidget(guiGraphics, mouseX, mouseY, partialTick);
        EditModifiersScreenEntry entry = getHovered();
        if (entry != null && entry.getTooltip() != null && entry.mouseHovered(getRowLeft(), mouseX, mouseY)) {
            editModifiersScreen.setTooltipForNextRenderPass(entry.getTooltip());
        }
    }

    @Override
    public int getRowWidth() {
        return ScreenConstants.ROW_WIDTH;
    }

    private record ModifierDescriptions(List<FormattedCharSequence> tooltip, String defaultValueText) {}
}
