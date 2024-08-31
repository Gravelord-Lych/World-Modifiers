package lych.worldmodifiers.modifier;

public final class Modifiers {
    public static final IntModifier MAX_HEALTH = new IntModifier("max_health", 100, 1, 500);
    public static final IntModifier MOVEMENT_SPEED = new IntModifier("movement_speed", 100, 10, 1500);

    private Modifiers() {}

    public static void bootstrap() {}
}
