package lych.worldmodifiers.modifier.category;

import lych.worldmodifiers.modifier.SortingPriority;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;
import java.util.Set;

public interface ModifierSetEntry {
    ResourceLocation getName();

    MutableComponent getDisplayName();

    Set<ModifierSetEntry> getChildren();

    @Nullable
    ModifierCategory getParent();

    SortingPriority getPriority();

    default MutableComponent getDescription() {
        return Component.translatable(getDisplayName().getString() + ".description");
    }
}
