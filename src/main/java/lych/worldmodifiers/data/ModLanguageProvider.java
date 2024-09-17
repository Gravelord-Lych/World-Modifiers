package lych.worldmodifiers.data;

import lych.worldmodifiers.WorldModifiersMod;
import lych.worldmodifiers.client.gui.widget.FoldButton;
import lych.worldmodifiers.client.gui.widget.ResetValueButton;
import lych.worldmodifiers.client.screen.EditModifiersScreen;
import lych.worldmodifiers.client.screen.entry.ModifierList;
import lych.worldmodifiers.modifier.Modifiers;
import lych.worldmodifiers.api.modifier.BaseModifier;
import lych.worldmodifiers.modifier.category.ModifierCategories;
import lych.worldmodifiers.util.DifficultyHelper;
import lych.worldmodifiers.util.ModifierHelper;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.neoforged.neoforge.common.data.LanguageProvider;

public abstract class ModLanguageProvider extends LanguageProvider {
    public ModLanguageProvider(PackOutput output, String locale) {
        super(output, WorldModifiersMod.MODID, locale);
    }

    public void add(BaseModifier key, String value) {
        add(key.getDisplayName(), value);
    }

    public void add(Component key, String value) {
        if (!(key.getContents() instanceof TranslatableContents contents)) {
            throw new IllegalArgumentException("Key must be translatable");
        }
        add(contents.getKey(), value);
    }

    public void addPrefixed(String key, String value) {
        add(WorldModifiersMod.MODID + "." + key, value);
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
            add(EditModifiersScreen.CONFIRMATION_SCREEN_TITLE, "Discard Changes");
            addPrefixed(EditModifiersScreen.CONFIRMATION_SCREEN_MESSAGE_KEY, "%d modifiers' values have been changed. Are you sure you want to discard these changes?");
            addPrefixed(ModifierList.EDIT_MODIFIER_MESSAGE_KEY, "Default: %s");
            add(ModifierHelper.MODIFIERS, "Modifiers");
            add(Modifiers.MAX_HEALTH, "Max Health");
            add(Modifiers.MAX_HEALTH.getDescription(), "Modifies the maximum health of all mobs (including players). The modified maximum health will not exceed vanilla's restriction (1024).");
            add(Modifiers.MOVEMENT_SPEED, "Movement Speed");
            add(Modifiers.MOVEMENT_SPEED.getDescription(), "Modifies the movement speed of all mobs (including players), but does not affect the flying speed.");
            add(Modifiers.MOVEMENT_SPEED.getWarning(),
                    "WARNING: Due to vanilla logic, when the modifier's value is very low, non-player mobs will be almost unable to move.");
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
            add(ResetValueButton.RESET_VALUE, "Reset Modifier's Value");
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
            add(EditModifiersScreen.CONFIRMATION_SCREEN_TITLE, "放弃更改");
            addPrefixed(EditModifiersScreen.CONFIRMATION_SCREEN_MESSAGE_KEY, "%d个调节器的数值已被更改。你确定要放弃这些更改吗？");
            addPrefixed(ModifierList.EDIT_MODIFIER_MESSAGE_KEY, "默认值: %s");
            add(ModifierHelper.MODIFIERS, "调节器");
            add(Modifiers.MAX_HEALTH, "最大生命值");
            add(Modifiers.MAX_HEALTH.getDescription(), "调整所有生物（包括玩家）的最大生命值。调整后的最大生命值不会超过原版的限制（1024）。");
            add(Modifiers.MOVEMENT_SPEED, "移动速度");
            add(Modifiers.MOVEMENT_SPEED.getDescription(), "调整所有生物（包括玩家）的移动速度，但不影响飞行速度。");
            add(Modifiers.MOVEMENT_SPEED.getWarning(), "警告：受到原版逻辑影响，当该调节器数值很低时，非玩家生物会几乎无法移动。");
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
            add(ResetValueButton.RESET_VALUE, "重置调节器数值");
        }
    }
}
