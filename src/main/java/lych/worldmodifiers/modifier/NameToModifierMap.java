package lych.worldmodifiers.modifier;

import lych.worldmodifiers.api.modifier.Modifier;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class NameToModifierMap {
    private static final Map<String, Modifier<?>> NAME_TO_MODIFIER_MAP = new HashMap<>();

    private NameToModifierMap() {}

    public static void put(Modifier<?> modifier) {
        NAME_TO_MODIFIER_MAP.put(modifier.getFullName().toString(), modifier);
    }

    public static Optional<Modifier<?>> byFullName(String name) {
        return Optional.ofNullable(NAME_TO_MODIFIER_MAP.get(name));
    }

    public static Map<String, Modifier<?>> viewAll() {
        return Collections.unmodifiableMap(NAME_TO_MODIFIER_MAP);
    }
}
