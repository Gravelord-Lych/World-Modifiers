package lych.worldmodifiers.api.modifier;

/**
 * The sorting priorities of a modifier or modifier category. This is used for sorting
 * modifier entries in the edit modifiers screen. The higher the sorting priority a modifier
 * or a modifier category has, the closer it is to the top of the edit modifiers screen.
 */
public enum SortingPriority {
    /**
     * The highest sorting priority.
     */
    HIGHEST,
    /**
     * The second-highest sorting priority.
     */
    HIGH,
    /**
     * The normal sorting priority and also the default sorting priority.<br>
     * <strong>NOTE: Any base modifier categories except the default one should always
     * use this sorting priority</strong>, as the default modifier category should be
     * positioned at the top of the edit modifiers screen and other base modifier
     * categories should be sorted according to their names below the vanilla one.
     */
    NORMAL,
    /**
     * The second-lowest sorting priority.
     */
    LOW,
    /**
     * The lowest sorting priority.
     */
    LOWEST
}
