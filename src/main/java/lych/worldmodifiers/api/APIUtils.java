package lych.worldmodifiers.api;

import com.google.common.base.Suppliers;
import com.google.gson.JsonObject;
import lych.worldmodifiers.api.client.screen.entry.ModifierEntry;
import lych.worldmodifiers.api.modifier.BaseModifier;
import lych.worldmodifiers.api.modifier.Modifier;
import lych.worldmodifiers.api.modifier.SortingPriority;
import lych.worldmodifiers.api.modifier.category.ModifierCategory;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.Set;
import java.util.function.Supplier;

public final class APIUtils {
    /**
     * The dummy resource location.
     */
    public static final ResourceLocation DUMMY_RESOURCE_LOCATION = ResourceLocation.fromNamespaceAndPath(WorldModifiersAPI.MODID, "dummy");

    private static final Supplier<ModifierCategory> DUMMY_EMPTY_MODIFIER_CATEGORY = Suppliers.memoize(APIUtils::createDummyEmptyModifierCategory);
    static final Supplier<WorldModifiersAPI> INSTANCE_SUPPLIER = Suppliers.memoize(APIUtils::findInstance);

    private APIUtils() {}

    public static ModifierCategory getDummyEmptyModifierCategory() {
        return DUMMY_EMPTY_MODIFIER_CATEGORY.get();
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

    private static ModifierCategory createDummyEmptyModifierCategory() {
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

    public static Modifier<Integer> createDummyIntModifier() {
        return new Modifier<>() {
            @Override
            public void serializeToJson(Integer value, JsonObject data) {}

            @Override
            public Integer deserializeFromJson(JsonObject data) {
                return 0;
            }

            @Override
            public void serializeToNetwork(Integer value, FriendlyByteBuf buf) {}

            @Override
            public Integer deserializeFromNetwork(FriendlyByteBuf buf) {
                return 0;
            }

            @Override
            public Class<? extends ModifierEntry<Integer>> getEntryClass() {
                Modifier<Integer> self = this;
                class DummyEntry implements ModifierEntry<Integer> {
                    @Override
                    public Modifier<Integer> getModifier() {
                        return self;
                    }

                    @Override
                    public void highlight() {}

                    @Override
                    public void unhighlight() {}
                }
                return DummyEntry.class;
            }

            @Override
            public Class<Integer> getValueClass() {
                return Integer.class;
            }

            @Override
            public ResourceLocation getTextureLocation(Integer value) {
                return DUMMY_RESOURCE_LOCATION;
            }

            @Nonnull
            @Override
            public ModifierCategory getParent() {
                return createDummyModifierCategory(Set.of(this));
            }

            @Override
            public Set<Modifier<?>> getSpecificModifiers() {
                return Set.of();
            }

            @Nullable
            @Override
            public Modifier<?> getGenericModifier() {
                return null;
            }

            @Override
            public Integer getDefaultValue() {
                return 0;
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
