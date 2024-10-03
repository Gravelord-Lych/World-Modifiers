package lych.worldmodifiers.modifier.selector;

import lych.worldmodifiers.api.modifier.Modifier;
import lych.worldmodifiers.modifier.ModifierMap;

import java.util.List;
import java.util.function.Predicate;

public class ModifierSelector<T, R> {
    private final List<Entry<? super T, R>> entries;
    private final Modifier<R> mostGenericModifier;

    public ModifierSelector(List<Entry<? super T, R>> entries, Modifier<R> mostGenericModifier) {
        this.entries = entries;
        this.mostGenericModifier = mostGenericModifier;
    }

    public Modifier<R> selectModifier(T t, ModifierMap modifierMap) {
        for (Entry<? super T, R> entry : entries) {
            if (modifierMap.valueChanged(entry.modifier) && entry.predicate.test(t)) {
                return entry.modifier;
            }
        }
        return mostGenericModifier;
    }

    public record Entry<T, R>(Predicate<T> predicate, Modifier<R> modifier) {}
}
