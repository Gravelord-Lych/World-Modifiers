package lych.worldmodifiers.data;

import lych.worldmodifiers.WorldModifiersMod;
import lych.worldmodifiers.client.gui.component.FoldButton;
import lych.worldmodifiers.client.screen.EditModifiersScreen;
import lych.worldmodifiers.client.screen.entry.ModifierList;
import lych.worldmodifiers.modifier.Modifiers;
import lych.worldmodifiers.modifier.category.ModifierCategories;
import lych.worldmodifiers.modifier.category.ModifierSetEntry;
import lych.worldmodifiers.util.DifficultyHelper;
import lych.worldmodifiers.util.ModifiersHelper;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.neoforged.neoforge.common.data.LanguageProvider;

public abstract class ModLanguageProvider extends LanguageProvider {
    public ModLanguageProvider(PackOutput output, String locale) {
        super(output, WorldModifiersMod.MODID, locale);
    }

    public void add(ModifierSetEntry key, String value) {
        add(key.getDisplayName(), value);
    }

    public void add(Component key, String value) {
        if (!(key.getContents() instanceof TranslatableContents contents)) {
            throw new IllegalArgumentException("Key must be translatable");
        }
        add(contents.getKey(), value);
    }

    public static class EnUs extends ModLanguageProvider {
        public EnUs(PackOutput output) {
            super(output, "en_us");
        }

        @Override
        protected void addTranslations() {
            add(DifficultyHelper.EXTREME_DIFFICULTY, "Extreme Difficulty");
            add(DifficultyHelper.EXTREME_DIFFICULTY_INFO, "Hostile mobs spawn and deal even more damage. The depletion of the hunger bar brings deadlier effects.");
            add(EditModifiersScreen.TITLE, "Edit Modifiers");
            add(WorldModifiersMod.MODID + "." + ModifierList.EDIT_MODIFIER, "Default: %s");
            add(ModifiersHelper.MODIFIERS, "Modifiers");
            add(Modifiers.MAX_HEALTH, "Max Health");
            add(Modifiers.MOVEMENT_SPEED, "Movement Speed");
            add(ModifierCategories.VANILLA, "Default Modifiers");
            add(ModifierCategories.ENTITY, "Entity");
            add(ModifierCategories.LIVING_ENTITY, "Mob");
            add(ModifierCategories.MOB, "Non-player Mob");
            add(ModifierCategories.MONSTER, "Hostile Mob");
            add(ModifierCategories.CREATURE, "Friendly Mob");
            add(ModifierCategories.PLAYER, "Player");
            add(FoldButton.FOLD, "Fold Modifiers");
            add(FoldButton.FOLDED, "Folded");
            add(FoldButton.UNFOLDED, "Unfolded");
        }
    }

    public static class ZhCn extends ModLanguageProvider {
        public ZhCn(PackOutput output) {
            super(output, "zh_cn");
        }

        @Override
        protected void addTranslations() {
            add(DifficultyHelper.EXTREME_DIFFICULTY, "极难");
            add(DifficultyHelper.EXTREME_DIFFICULTY_INFO, "敌对生物会生成，且伤害更高。饥饿值耗尽会带来更加致命的影响。");
            add(EditModifiersScreen.TITLE, "编辑调节器");
            add(WorldModifiersMod.MODID + "." + ModifierList.EDIT_MODIFIER, "默认值: %s");
            add(ModifiersHelper.MODIFIERS, "调节器");
            add(Modifiers.MAX_HEALTH, "最大生命值");
            add(Modifiers.MOVEMENT_SPEED, "移动速度");
            add(ModifierCategories.VANILLA, "默认调节器");
            add(ModifierCategories.ENTITY, "实体");
            add(ModifierCategories.LIVING_ENTITY, "生物");
            add(ModifierCategories.MOB, "非玩家生物");
            add(ModifierCategories.MONSTER, "敌对生物");
            add(ModifierCategories.CREATURE, "友好生物");
            add(ModifierCategories.PLAYER, "玩家");
            add(FoldButton.FOLD, "折叠调节器");
            add(FoldButton.FOLDED, "已折叠");
            add(FoldButton.UNFOLDED, "未折叠");
        }
    }
}
