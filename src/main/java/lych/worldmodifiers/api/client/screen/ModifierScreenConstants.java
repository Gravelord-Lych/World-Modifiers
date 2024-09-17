package lych.worldmodifiers.api.client.screen;

/**
 * Constants used in the edit modifiers screen.
 */
public final class ModifierScreenConstants {
    /**
     * The size of the modifiers' texture.
     */
    public static final int TEXTURE_SIZE = 18;
    /**
     * The spacing between the icon's left side and the text's left side.
     */
    public static final int ICON_SPACING = TEXTURE_SIZE + 4;
    /**
     * The maximum width of the name text of the modifiers.
     */
    public static final int MAX_TEXT_WIDTH = 100;
    /**
     * Used to calculate the x position offset of a modifier entry. For example, if a
     * modifier entry's depth is 2, the x position offset will be 20.
     */
    public static final int DEPTH_X_OFFSET = 10;
    /**
     * The extra x spacing between the slider and other widgets.
     */
    public static final int SLIDER_SPACING = 20;
    /**
     * The height of the text of the modifier entry.
     */
    public static final int CATEGORY_ENTRY_TOP_OFFSET = 8;
    /**
     * The x spacing between the fold icon and the text of the category entry.
     */
    public static final int CATEGORY_ENTRY_ICON_OFFSET = 18;
    /**
     * The maximum width of the tooltip of a modifier.
     */
    public static final int MODIFIER_TOOLTIP_MAX_WIDTH = 150;
    /**
     * The height of each line of a modifier entry's name.
     */
    public static final int MODIFIER_ENTRY_NAME_LINE_HEIGHT = 5;
    /**
     * The height of all the edit modifiers screen's entries.
     */
    public static final int ITEM_HEIGHT = 24;
    /**
     * The width of the entire edit modifiers screen.
     */
    public static final int ROW_WIDTH = 340;
    /**
     * The height of the slider.
     */
    public static final int SLIDER_HEIGHT = 20;
    /**
     * The size of the reset value button.
     */
    public static final int RESET_VALUE_BUTTON_SIZE = SLIDER_HEIGHT;
    /**
     * The size of the fold icons which are positioned at the left of the modifier categories' name.
     */
    public static final int FOLD_ICON_SIZE = 24;
    /**
     * The x coordinate offset of an unfolded fold icon.
     */
    public static final int UNFOLDED_FOLD_ICON_X_OFFSET = -6;
    /**
     * The y coordinate offset of an unfolded fold icon.
     */
    public static final int UNFOLDED_FOLD_ICON_Y_OFFSET = 4;
    /**
     * The spacing between the "proceed" button and the "cancel" button at the foot of
     * the edit modifiers screen.
     */
    public static final int SPACING = 8;
    /**
     * When the count of modified modifiers reaches this number, the confirm screen will be shown if
     * the player presses the "cancel" button.
     */
    public static final int SHOW_CONFIRM_SCREEN_REQUIRED_MODIFICATION_COUNT = 2;

    private ModifierScreenConstants() {}
}
