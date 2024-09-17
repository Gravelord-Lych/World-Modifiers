package lych.worldmodifiers.test;

import lych.worldmodifiers.modifier.ModifierMap;
import lych.worldmodifiers.modifier.Modifiers;
import lych.worldmodifiers.modifier.NameToModifierMap;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("modifiers")
public class ModifierTest {
    @BeforeAll
    public static void init() {
        Modifiers.bootstrap();
    }

    @Test
    public void testModifierMapSize() {
        ModifierMap map = new ModifierMap();
        Assertions.assertEquals(NameToModifierMap.viewAll().size(), map.size());
    }

    @Test
    public void testDifference() {
        ModifierMap map1 = new ModifierMap();
        ModifierMap map2 = new ModifierMap();
        assertDifferenceCount(0, map1, map2);
        map1.setModifierValue(Modifiers.MAX_HEALTH, Modifiers.MAX_HEALTH.getDefaultValue() + 1);
        map1.setModifierValue(Modifiers.MOVEMENT_SPEED, Modifiers.MOVEMENT_SPEED.getDefaultValue() - 1);
        assertDifferenceCount(2, map1, map2);
        map2.setModifierValue(Modifiers.MAX_HEALTH, Modifiers.MAX_HEALTH.getDefaultValue() + 1);
        assertDifferenceCount(1, map1, map2);
        map1.resetAllModifiers();
        assertDifferenceCount(1, map1, map2);
    }

    private static void assertDifferenceCount(int expected, ModifierMap map1, ModifierMap map2) {
        Assertions.assertEquals(expected, map1.difference(map2));
        Assertions.assertEquals(expected, map2.difference(map1));
    }
}
