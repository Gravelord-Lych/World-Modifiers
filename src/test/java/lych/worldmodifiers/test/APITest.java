package lych.worldmodifiers.test;

import lych.worldmodifiers.WorldModifiersMod;
import lych.worldmodifiers.api.APIUtils;
import lych.worldmodifiers.api.WorldModifiersAPI;
import lych.worldmodifiers.api.modifier.Modifier;
import lych.worldmodifiers.api.modifier.ModifierNames;
import lych.worldmodifiers.api.modifier.category.ModifierCategory;
import lych.worldmodifiers.modifier.Modifiers;
import lych.worldmodifiers.modifier.NameToModifierMap;
import lych.worldmodifiers.util.WorldModifiersAPIImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Optional;

@Tag("api")
public class APITest {
    @BeforeAll
    public static void init() {
        Modifiers.bootstrap();
    }

    @Test
    public void testModid() {
        Assertions.assertEquals(WorldModifiersMod.MODID, WorldModifiersAPI.MODID);
    }

    @Test
    public void testAPIInstance() {
        Assertions.assertSame(WorldModifiersAPIImpl.INSTANCE, WorldModifiersAPI.getInstance());
    }

    @Test
    public void testExistence() {
        Assertions.assertFalse(WorldModifiersAPI.getInstance().isDummy());
        Assertions.assertTrue(WorldModifiersAPI.isWorldModifierModPresent());
    }

    @Test
    public void testDummyValues() {
        try {
            ModifierCategory dummyModifierCategory = APIUtils.getDummyEmptyModifierCategory();
            Modifier<Integer> dummyIntModifier = new WorldModifiersAPI() {}.getIntModifierBuilder("dummy", "dummy")
                    .setValueRange(0, 100)
                    .setDefaultValue(50)
                    .build();
            testDummyValue(dummyModifierCategory);
            testDummyValue(dummyIntModifier);
        } catch (Exception e) {
            Assertions.fail(e);
        }
    }

    private static void testDummyValue(ModifierCategory dummy) {
        Assertions.assertNull(dummy.getParent());
    }

    private static void testDummyValue(Modifier<?> dummy) {
        Assertions.assertNotNull(dummy.getParent());
        Assertions.assertNotNull(dummy.getParent().getChildren());
        Assertions.assertFalse(dummy.getParent().getChildren().isEmpty());
        Assertions.assertSame(dummy.getParent().getChildren().toArray()[0], dummy);
        Assertions.assertTrue(dummy.getChildren().isEmpty());
    }

    @Test
    public void testModifierGetter() {
        Optional<Modifier<?>> modifierByName1 = WorldModifiersAPI.getInstance().getModifierByName(WorldModifiersMod.MODID, ModifierNames.MAX_HEALTH_GENERIC);
        Optional<Modifier<?>> modifierByName2 = NameToModifierMap.byFullName(WorldModifiersMod.MODID + ":" + ModifierNames.MAX_HEALTH_GENERIC);
        Assertions.assertTrue(modifierByName1.isPresent());
        Assertions.assertTrue(modifierByName2.isPresent());
        Assertions.assertSame(modifierByName2.get(), modifierByName1.get());
    }
}
