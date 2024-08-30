package lych.worldmodifiers.modifier;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class NameToModifierMap {
    private static final Map<String, Modifier<?>> NAME_TO_MODIFIER_MAP = new HashMap<>();

    private NameToModifierMap() {}

    public static void put(Modifier<?> modifier) {
        NAME_TO_MODIFIER_MAP.put(modifier.getName(), modifier);
    }

    public static Optional<Modifier<?>> byName(String name) {
        return Optional.ofNullable(NAME_TO_MODIFIER_MAP.get(name));
    }

    public static Map<String, Modifier<?>> viewAll() {
        return Map.copyOf(NAME_TO_MODIFIER_MAP);
    }
}
