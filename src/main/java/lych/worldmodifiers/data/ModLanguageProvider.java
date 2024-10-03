package lych.worldmodifiers.data;

import lych.worldmodifiers.WorldModifiersMod;
import lych.worldmodifiers.api.modifier.BaseModifier;
import lych.worldmodifiers.client.gui.widget.FoldButton;
import lych.worldmodifiers.client.gui.widget.ResetValueButton;
import lych.worldmodifiers.client.screen.EditModifiersScreen;
import lych.worldmodifiers.client.screen.entry.ModifierList;
import lych.worldmodifiers.modifier.ModifierConflictChecker;
import lych.worldmodifiers.modifier.Modifiers;
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
            add(EditModifiersScreen.DISCARD_CHANGES_TITLE, "Discard Changes");
            add(EditModifiersScreen.CONFLICT_MODIFIERS_DETECTED_TITLE, "Conflict Modifiers Detected");
            addPrefixed(EditModifiersScreen.DISCARD_CHANGES_MESSAGE_KEY, "%d modifiers' values have been changed. Are you sure you want to discard these changes?");
            addPrefixed(ModifierList.EDIT_MODIFIER_MESSAGE_KEY, "Default: %s");
            addPrefixed(ModifierConflictChecker.CONFLICT_MESSAGE, "\"%1$s\"  ->  \"%2$s\"");
            add(ModifierHelper.MODIFIERS, "Modifiers");
            add(Modifiers.ARMOR_GENERIC, "Armor");
            add(Modifiers.ARMOR_GENERIC.getDescription(), "Modifies the base armor value of all non-player mobs. The modified armor value will not exceed vanilla's restriction (30).");
            add(Modifiers.ARMOR_MONSTERS, "Armor (Hostile Mobs)");
            add(Modifiers.ARMOR_MONSTERS.getDescription(), "Modifies the base armor value of all hostile mobs. The modified armor value will not exceed vanilla's restriction (30).");
            add(Modifiers.ARMOR_CREATURES, "Armor (Friendly Mobs)");
            add(Modifiers.ARMOR_CREATURES.getDescription(), "Modifies the base armor value of all friendly mobs. The modified armor value will not exceed vanilla's restriction (30).");
            String armorAdditionNote = "NOTE: This modifier applies before the armor modification modifiers.";
            add(Modifiers.ARMOR_ADDITION_GENERIC, "Armor Addition");
            add(Modifiers.ARMOR_ADDITION_GENERIC.getDescription(), "Adds the additional armor value to all non-player mobs. Negative value means reducing the armor value. The modified armor value will not exceed vanilla's restriction (30).");
            add(Modifiers.ARMOR_ADDITION_GENERIC.getWarning(), armorAdditionNote);
            add(Modifiers.ARMOR_ADDITION_MONSTERS, "Armor Addition (Hostile Mobs)");
            add(Modifiers.ARMOR_ADDITION_MONSTERS.getDescription(), "Adds the additional armor value to all hostile mobs. Negative value means reducing the armor value. The modified armor value will not exceed vanilla's restriction (30).");
            add(Modifiers.ARMOR_ADDITION_MONSTERS.getWarning(), armorAdditionNote);
            add(Modifiers.ARMOR_ADDITION_CREATURES, "Armor Addition (Friendly Mobs)");
            add(Modifiers.ARMOR_ADDITION_CREATURES.getDescription(), "Adds the additional armor value to all friendly mobs. Negative value means reducing the armor value. The modified armor value will not exceed vanilla's restriction (30).");
            add(Modifiers.ARMOR_ADDITION_CREATURES.getWarning(), armorAdditionNote);
            add(Modifiers.ARMOR_TOUGHNESS_GENERIC, "Armor Toughness");
            add(Modifiers.ARMOR_TOUGHNESS_GENERIC.getDescription(), "Modifies the base armor toughness value of all non-player mobs. The modified armor toughness value will not exceed vanilla's restriction (20).");
            add(Modifiers.ARMOR_TOUGHNESS_MONSTERS, "Armor Toughness (Hostile Mobs)");
            add(Modifiers.ARMOR_TOUGHNESS_MONSTERS.getDescription(), "Modifies the base armor toughness value of all hostile mobs. The modified armor toughness value will not exceed vanilla's restriction (20).");
            add(Modifiers.ARMOR_TOUGHNESS_CREATURES, "Armor Toughness (Friendly Mobs)");
            add(Modifiers.ARMOR_TOUGHNESS_CREATURES.getDescription(), "Modifies the base armor toughness value of all friendly mobs. The modified armor toughness value will not exceed vanilla's restriction (20).");
            String armorToughnessAdditionNote = "NOTE: This modifier applies after the armor toughness modification modifiers.";
            add(Modifiers.ARMOR_TOUGHNESS_ADDITION_GENERIC, "Armor Toughness Addition");
            add(Modifiers.ARMOR_TOUGHNESS_ADDITION_GENERIC.getDescription(), "Adds the additional armor toughness value to all non-player mobs. Negative value means reducing the armor toughness value. The modified armor toughness value will not exceed vanilla's restriction (20).");
            add(Modifiers.ARMOR_TOUGHNESS_ADDITION_GENERIC.getWarning(), armorToughnessAdditionNote);
            add(Modifiers.ARMOR_TOUGHNESS_ADDITION_MONSTERS, "Armor Toughness Addition (Hostile Mobs)");
            add(Modifiers.ARMOR_TOUGHNESS_ADDITION_MONSTERS.getDescription(), "Adds the additional armor toughness value to all hostile mobs. Negative value means reducing the armor toughness value. The modified armor toughness value will not exceed vanilla's restriction (20).");
            add(Modifiers.ARMOR_TOUGHNESS_ADDITION_MONSTERS.getWarning(), armorToughnessAdditionNote);
            add(Modifiers.ARMOR_TOUGHNESS_ADDITION_CREATURES, "Armor Toughness Addition (Friendly Mobs)");
            add(Modifiers.ARMOR_TOUGHNESS_ADDITION_CREATURES.getDescription(), "Adds the additional armor toughness value to all friendly mobs. Negative value means reducing the armor toughness value. The modified armor toughness value will not exceed vanilla's restriction (20).");
            add(Modifiers.ARMOR_TOUGHNESS_ADDITION_CREATURES.getWarning(), armorToughnessAdditionNote);
            add(Modifiers.MAX_HEALTH_GENERIC, "Max Health");
            add(Modifiers.MAX_HEALTH_GENERIC.getDescription(), "Modifies the base maximum health of all non-player mobs. The modified maximum health will not exceed vanilla's restriction (1024).");
            add(Modifiers.MAX_HEALTH_MONSTERS, "Max Health (Hostile Mobs)");
            add(Modifiers.MAX_HEALTH_MONSTERS.getDescription(), "Modifies the base maximum health of all hostile mobs. The modified maximum health will not exceed vanilla's restriction (1024).");
            add(Modifiers.MAX_HEALTH_CREATURES, "Max Health (Friendly Mobs)");
            add(Modifiers.MAX_HEALTH_CREATURES.getDescription(), "Modifies the base maximum health of all friendly mobs. The modified maximum health will not exceed vanilla's restriction (1024).");
            add(Modifiers.MAX_HEALTH_PLAYER, "Max Health (Player)");
            add(Modifiers.MAX_HEALTH_PLAYER.getDescription(), "Modifies the base maximum health of all players. The modified maximum health will not exceed vanilla's restriction (1024).");
            String speedModifierWarning = "WARNING: Due to vanilla logic of calculating non-player mobs' movement speed, when the modifier's value is very low, " +
                    "non-player mobs may move slower than expected, and when the modifier's value is very high, non-player mobs may behave abnormally while moving.";
            add(Modifiers.MOVEMENT_SPEED_GENERIC, "Movement Speed");
            add(Modifiers.MOVEMENT_SPEED_GENERIC.getDescription(), "Modifies the base movement speed of all non-player mobs, but does not affect the flying speed.");
            add(Modifiers.MOVEMENT_SPEED_GENERIC.getWarning(), speedModifierWarning);
            add(Modifiers.MOVEMENT_SPEED_MONSTERS, "Movement Speed (Hostile Mobs)");
            add(Modifiers.MOVEMENT_SPEED_MONSTERS.getDescription(), "Modifies the base movement speed of all hostile mobs, but does not affect the flying speed.");
            add(Modifiers.MOVEMENT_SPEED_MONSTERS.getWarning(), speedModifierWarning);
            add(Modifiers.MOVEMENT_SPEED_CREATURES, "Movement Speed (Friendly Mobs)");
            add(Modifiers.MOVEMENT_SPEED_CREATURES.getDescription(), "Modifies the base movement speed of all friendly mobs, but does not affect the flying speed.");
            add(Modifiers.MOVEMENT_SPEED_CREATURES.getWarning(), speedModifierWarning);
            add(Modifiers.MOVEMENT_SPEED_PLAYER, "Movement Speed (Player)");
            add(Modifiers.MOVEMENT_SPEED_PLAYER.getDescription(), "Modifies the base movement speed of all players, but does not affect the flying speed.");
            String generic = "Generic";
            add(ModifierCategories.VANILLA, "Default Modifiers");
            add(ModifierCategories.ENTITY, "Entity");
            add(ModifierCategories.LIVING_ENTITY, "Mob");
            add(ModifierCategories.LIVING_ENTITY_GENERIC, generic);
            add(ModifierCategories.MOB, "Non-player Mob");
            add(ModifierCategories.MOB_GENERIC, generic);
            add(ModifierCategories.MONSTER, "Hostile Mob");
            add(ModifierCategories.CREATURE, "Friendly Mob");
            add(ModifierCategories.CREATURE_GENERIC, generic);
            add(ModifierCategories.ANIMAL, "Animal");
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
            add(EditModifiersScreen.DISCARD_CHANGES_TITLE, "放弃更改");
            add(EditModifiersScreen.CONFLICT_MODIFIERS_DETECTED_TITLE, "检测到冲突的调节器");
            addPrefixed(EditModifiersScreen.DISCARD_CHANGES_MESSAGE_KEY, "%d个调节器的数值已被更改。你确定要放弃这些更改吗？");
            addPrefixed(ModifierList.EDIT_MODIFIER_MESSAGE_KEY, "默认值：%s");
            addPrefixed(ModifierConflictChecker.CONFLICT_MESSAGE, "“%1$s”  ->  “%2$s”");
            add(ModifierHelper.MODIFIERS, "调节器");
            add(Modifiers.ARMOR_GENERIC, "护甲值");
            add(Modifiers.ARMOR_GENERIC.getDescription(), "调节所有非玩家生物的基础护甲值。调节后的护甲值不会超过原版的限制（30）。");
            add(Modifiers.ARMOR_MONSTERS, "护甲值（敌对生物）");
            add(Modifiers.ARMOR_MONSTERS.getDescription(), "调节所有敌对生物的基础护甲值。调节后的护甲值不会超过原版的限制（30）。");
            add(Modifiers.ARMOR_CREATURES, "护甲值（友好生物）");
            add(Modifiers.ARMOR_CREATURES.getDescription(), "调节所有友好生物的基础护甲值。调节后的护甲值不会超过原版的限制（30）。");
            String armorAdditionNote = "注意：该调节器先于护甲值调节器生效。";
            add(Modifiers.ARMOR_ADDITION_GENERIC, "额外护甲值");
            add(Modifiers.ARMOR_ADDITION_GENERIC.getDescription(), "为所有非玩家生物添加额外的护甲值，负值表示降低护甲值。调节后的护甲值不会超过原版的限制（30）。");
            add(Modifiers.ARMOR_ADDITION_GENERIC.getWarning(), armorAdditionNote);
            add(Modifiers.ARMOR_ADDITION_MONSTERS, "额外护甲值（敌对生物）");
            add(Modifiers.ARMOR_ADDITION_MONSTERS.getDescription(), "为所有敌对生物添加额外的护甲值，负值表示降低护甲值。调节后的护甲值不会超过原版的限制（30）。");
            add(Modifiers.ARMOR_ADDITION_MONSTERS.getWarning(), armorAdditionNote);
            add(Modifiers.ARMOR_ADDITION_CREATURES, "额外护甲值（友好生物）");
            add(Modifiers.ARMOR_ADDITION_CREATURES.getDescription(), "为所有友好生物添加额外的护甲值，负值表示降低护甲值。调节后的护甲值不会超过原版的限制（30）。");
            add(Modifiers.ARMOR_ADDITION_CREATURES.getWarning(), armorAdditionNote);
            add(Modifiers.ARMOR_TOUGHNESS_GENERIC, "盔甲韧性");
            add(Modifiers.ARMOR_TOUGHNESS_GENERIC.getDescription(), "调节所有非玩家生物的基础盔甲韧性。调节后的盔甲韧性不会超过原版的限制（20）。");
            add(Modifiers.ARMOR_TOUGHNESS_MONSTERS, "盔甲韧性（敌对生物）");
            add(Modifiers.ARMOR_TOUGHNESS_MONSTERS.getDescription(), "调节所有敌对生物的基础盔甲韧性。调节后的盔甲韧性不会超过原版的限制（20）。");
            add(Modifiers.ARMOR_TOUGHNESS_CREATURES, "盔甲韧性（友好生物）");
            add(Modifiers.ARMOR_TOUGHNESS_CREATURES.getDescription(), "调节所有友好生物的基础盔甲韧性。调节后的盔甲韧性不会超过原版的限制（20）。");
            String armorToughnessAdditionNote = "注意：该调节器在盔甲韧性调节器之后生效。";
            add(Modifiers.ARMOR_TOUGHNESS_ADDITION_GENERIC, "额外盔甲韧性");
            add(Modifiers.ARMOR_TOUGHNESS_ADDITION_GENERIC.getDescription(), "为所有非玩家生物添加额外的盔甲韧性，负值表示降低盔甲韧性。调节后的盔甲韧性不会超过原版的限制（20）。");
            add(Modifiers.ARMOR_TOUGHNESS_ADDITION_GENERIC.getWarning(), armorToughnessAdditionNote);
            add(Modifiers.ARMOR_TOUGHNESS_ADDITION_MONSTERS, "额外盔甲韧性（敌对生物）");
            add(Modifiers.ARMOR_TOUGHNESS_ADDITION_MONSTERS.getDescription(), "为所有敌对生物添加额外的盔甲韧性，负值表示降低盔甲韧性。调节后的盔甲韧性不会超过原版的限制（20）。");
            add(Modifiers.ARMOR_TOUGHNESS_ADDITION_MONSTERS.getWarning(), armorToughnessAdditionNote);
            add(Modifiers.ARMOR_TOUGHNESS_ADDITION_CREATURES, "额外盔甲韧性（友好生物）");
            add(Modifiers.ARMOR_TOUGHNESS_ADDITION_CREATURES.getDescription(), "为所有友好生物添加额外的盔甲韧性，负值表示降低盔甲韧性。调节后的盔甲韧性不会超过原版的限制（20）。");
            add(Modifiers.ARMOR_TOUGHNESS_ADDITION_CREATURES.getWarning(), armorToughnessAdditionNote);
            add(Modifiers.MAX_HEALTH_GENERIC, "最大生命值");
            add(Modifiers.MAX_HEALTH_GENERIC.getDescription(), "调节所有非玩家生物的基础最大生命值。调节后的最大生命值不会超过原版的限制（1024）。");
            add(Modifiers.MAX_HEALTH_MONSTERS, "最大生命值（敌对生物）");
            add(Modifiers.MAX_HEALTH_MONSTERS.getDescription(), "调节所有敌对生物的基础最大生命值。调节后的最大生命值不会超过原版的限制（1024）。");
            add(Modifiers.MAX_HEALTH_CREATURES, "最大生命值（友好生物）");
            add(Modifiers.MAX_HEALTH_CREATURES.getDescription(), "调节所有友好生物的基础最大生命值。调节后的最大生命值不会超过原版的限制（1024）。");
            add(Modifiers.MAX_HEALTH_PLAYER, "最大生命值（玩家）");
            add(Modifiers.MAX_HEALTH_PLAYER.getDescription(), "调节所有玩家的基础最大生命值。调节后的最大生命值不会超过原版的限制（1024）。");
            String speedModifierWarning = "警告：受到原版计算非玩家生物移动速度的逻辑影响，当该调节器的数值很低时，非玩家生物可能会移动地比期望的移动速度更加缓慢，而当该调节器的数值很高时，非玩家生物可能在移动时行为异常。";
            add(Modifiers.MOVEMENT_SPEED_GENERIC, "移动速度");
            add(Modifiers.MOVEMENT_SPEED_GENERIC.getDescription(), "调节所有非玩家生物的基础移动速度，但不影响飞行速度。");
            add(Modifiers.MOVEMENT_SPEED_GENERIC.getWarning(), speedModifierWarning);
            add(Modifiers.MOVEMENT_SPEED_MONSTERS, "移动速度（敌对生物）");
            add(Modifiers.MOVEMENT_SPEED_MONSTERS.getDescription(), "调节所有敌对生物的基础移动速度，但不影响飞行速度。");
            add(Modifiers.MOVEMENT_SPEED_MONSTERS.getWarning(), speedModifierWarning);
            add(Modifiers.MOVEMENT_SPEED_CREATURES, "移动速度（友好生物）");
            add(Modifiers.MOVEMENT_SPEED_CREATURES.getDescription(), "调节所有友好生物的基础移动速度，但不影响飞行速度。");
            add(Modifiers.MOVEMENT_SPEED_CREATURES.getWarning(), speedModifierWarning);
            add(Modifiers.MOVEMENT_SPEED_PLAYER, "移动速度（玩家）");
            add(Modifiers.MOVEMENT_SPEED_PLAYER.getDescription(), "调节所有玩家的基础移动速度，但不影响飞行速度。");
            String generic = "通用";
            add(ModifierCategories.VANILLA, "默认调节器");
            add(ModifierCategories.ENTITY, "实体");
            add(ModifierCategories.LIVING_ENTITY, "生物");
            add(ModifierCategories.LIVING_ENTITY_GENERIC, generic);
            add(ModifierCategories.MOB, "非玩家生物");
            add(ModifierCategories.MOB_GENERIC, generic);
            add(ModifierCategories.MONSTER, "敌对生物");
            add(ModifierCategories.CREATURE, "友好生物");
            add(ModifierCategories.CREATURE_GENERIC, generic);
            add(ModifierCategories.ANIMAL, "动物");
            add(ModifierCategories.PLAYER, "玩家");
            add(FoldButton.FOLD, "折叠调节器");
            add(FoldButton.FOLDED, "已折叠");
            add(FoldButton.UNFOLDED, "未折叠");
            add(ResetValueButton.RESET_VALUE, "重置调节器数值");
        }
    }
}
