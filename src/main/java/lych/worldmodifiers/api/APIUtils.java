package lych.worldmodifiers.api;

import com.google.common.base.Suppliers;
import lych.worldmodifiers.api.modifier.BaseModifier;
import lych.worldmodifiers.api.modifier.Modifier;
import lych.worldmodifiers.api.modifier.SortingPriority;
import lych.worldmodifiers.api.modifier.category.ModifierCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;

import java.util.Set;
import java.util.function.Supplier;

public final class APIUtils {
    /**
     * The dummy resource location.
     */
    public static final ResourceLocation DUMMY_RESOURCE_LOCATION = ResourceLocation.fromNamespaceAndPath(WorldModifiersAPI.MODID, "dummy");

    private static final Supplier<ModifierCategory> DUMMY_MODIFIER_CATEGORY = Suppliers.memoize(APIUtils::createDummyModifierCategory);
    static final Supplier<WorldModifiersAPI> INSTANCE_SUPPLIER = Suppliers.memoize(APIUtils::findInstance);

    private APIUtils() {}

    public static ModifierCategory getDummyModifierCategory() {
        return DUMMY_MODIFIER_CATEGORY.get();
    }

    private static WorldModifiersAPI findInstance() {
        try {
            Class<?> clazz = Class.forName("lych.worldmodifiers.util.WorldModifiersAPIImpl");
            WorldModifiersAPI instance = (WorldModifiersAPI) clazz.getField("INSTANCE").get(null);
            instance.init();
            return instance;
        } catch (ClassNotFoundException e) {
            return new WorldModifiersAPI() {};
        } catch (ReflectiveOperationException e) {
            throw new Error(e);
        }
    }

    private static ModifierCategory createDummyModifierCategory() {
        return createDummyModifierCategory(Set.of());
    }

    public static ModifierCategory createDummyModifierCategory(Set<BaseModifier> children) {
        return new ModifierCategory() {
            @Override
            public void addChildModifier(Modifier<?> modifier) {}

            @Override
            public ModifierCategory addSubCategory(String namespace, String name, SortingPriority priority) {
                return this;
            }

            @Override
            public ResourceLocation getFullName() {
                return DUMMY_RESOURCE_LOCATION;
            }

            @Override
            public MutableComponent getDisplayName() {
                return Component.empty();
            }

            @Override
            public Set<BaseModifier> getChildren() {
                return Set.copyOf(children);
            }

            @Override
            public ModifierCategory getParent() {
                return null;
            }

            @Override
            public SortingPriority getPriority() {
                return SortingPriority.NORMAL;
            }

            @Override
            public MutableComponent getDescription() {
                return Component.empty();
            }

            @Override
            public MutableComponent getWarning() {
                return Component.empty();
            }
        };
    }
}
