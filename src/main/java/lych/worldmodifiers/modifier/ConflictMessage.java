package lych.worldmodifiers.modifier;

import lych.worldmodifiers.api.client.screen.ColorConstants;
import lych.worldmodifiers.api.modifier.BaseModifier;
import lych.worldmodifiers.api.modifier.Modifier;
import lych.worldmodifiers.util.MessageUtils;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public record ConflictMessage(Modifier<?> specificModifier, Modifier<?> genericModifier) implements Comparable<ConflictMessage> {
    public Component getMessage() {
        return MessageUtils.prefixMsg(ModifierConflictChecker.CONFLICT_MESSAGE,
                specificModifier.getDisplayName().withStyle(style -> style.withItalic(true).withColor(ColorConstants.MODIFIER_NAME_COLOR)),
                genericModifier.getDisplayName().withStyle(style -> style.withItalic(true).withColor(ColorConstants.MODIFIER_NAME_COLOR)));
    }

    @Override
    public int compareTo(@NotNull ConflictMessage o) {
        int s = BaseModifier.getComparator().compare(specificModifier, o.specificModifier);
        return s != 0 ? s : BaseModifier.getComparator().compare(genericModifier, o.genericModifier);
    }
}
