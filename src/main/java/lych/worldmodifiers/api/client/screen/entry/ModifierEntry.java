package lych.worldmodifiers.api.client.screen.entry;

import lych.worldmodifiers.api.modifier.Modifier;

/**
 * Modifier Entries are entries that hold modifiers in the edit modifier screen.<br>
 * <strong>Do remember that the interface should not be used as a functional interface</strong>
 */
public interface ModifierEntry<T> {
    /**
     * Gets the modifier of the entry.
     * @return the modifier of the entry.
     */
    Modifier<T> getModifier();
}
