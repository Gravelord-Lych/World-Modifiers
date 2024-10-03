package lych.worldmodifiers.api.client.screen.entry;

import lych.worldmodifiers.api.modifier.Modifier;

/**
 * Modifier Entries are entries that hold modifiers in the edit modifier screen.<br>
 */
public interface ModifierEntry<T> {
    /**
     * Gets the modifier of the entry.
     * @return the modifier of the entry.
     */
    Modifier<T> getModifier();

    /**
     * Highlights the value of the entry.
     */
    void highlight();

    /**
     * Unhighlights the value of the entry.
     */
    void unhighlight();
}
