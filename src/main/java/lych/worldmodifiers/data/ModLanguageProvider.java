package lych.worldmodifiers.data;

import lych.worldmodifiers.WorldModifiersMod;
import lych.worldmodifiers.util.DifficultyHelper;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.neoforged.neoforge.common.data.LanguageProvider;

public abstract class ModLanguageProvider extends LanguageProvider {
    public ModLanguageProvider(PackOutput output, String locale) {
        super(output, WorldModifiersMod.MODID, locale);
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
        }
    }
}
