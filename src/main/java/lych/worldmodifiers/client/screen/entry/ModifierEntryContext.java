package lych.worldmodifiers.client.screen.entry;

import lych.worldmodifiers.api.modifier.Modifier;
import lych.worldmodifiers.client.screen.EditModifiersScreen;
import lych.worldmodifiers.modifier.ModifierMap;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;

import java.util.List;

public record ModifierEntryContext<T>(
        EditModifiersScreen editModifiersScreen,
        ModifierMap modifierMap,
        Component displayName,
        List<FormattedCharSequence> tooltip,
        int depth,
        String defaultValueText,
        Modifier<T> modifier
) {
}
