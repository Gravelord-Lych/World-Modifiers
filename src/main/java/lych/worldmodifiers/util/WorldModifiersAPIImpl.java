package lych.worldmodifiers.util;

import lych.worldmodifiers.api.WorldModifiersAPI;
import lych.worldmodifiers.api.modifier.IntModifierBuilder;
import lych.worldmodifiers.api.modifier.Modifier;
import lych.worldmodifiers.api.modifier.category.ModifierCategory;
import lych.worldmodifiers.api.modifier.texture.DynamicModifierTextureProviderBuilder;
import lych.worldmodifiers.api.modifier.texture.RangedModifierTextureProviderBuilder;
import lych.worldmodifiers.modifier.IntModifier;
import lych.worldmodifiers.modifier.Modifiers;
import lych.worldmodifiers.modifier.NameToModifierMap;
import lych.worldmodifiers.modifier.category.BaseModifierCategory;
import lych.worldmodifiers.modifier.texture.ModifierTextureProviders;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public enum WorldModifiersAPIImpl implements WorldModifiersAPI {
    INSTANCE;

    @Override
    public boolean isDummy() {
        return false;
    }

    @Override
    public int getAPIVersion() {
        return 1;
    }

    @Override
    public void init() {
        Modifiers.bootstrap();
    }

    @Override
    public Optional<Modifier<?>> getModifierByName(String id, String name) {
        return NameToModifierMap.byFullName(id + ":" + name);
    }

    @Override
    public <T> T getModifierValue(LevelAccessor level, Modifier<T> modifier) {
        return ModifierHelper.getModifierValue(level, modifier);
    }

    @Override
    public <T> T getModifierValue(MinecraftServer server, Modifier<T> modifier) {
        return ModifierHelper.getModifierValue(server, modifier);
    }

    @Override
    public <T> void setModifierValue(Level level, Modifier<T> modifier, T value) {
        ModifierHelper.setModifierValue(level, modifier, value);
    }

    @Override
    public <T> void setModifierValue(MinecraftServer server, Modifier<T> modifier, T value) {
        ModifierHelper.setModifierValue(server, modifier, value);
    }

    @Override
    public ModifierCategory createBaseCategoryFor(String id) {
        return BaseModifierCategory.createBaseCategoryFor(id);
    }

    @Override
    public IntModifierBuilder getIntModifierBuilder(String id, String name) {
        return IntModifier.builder(id, name);
    }

    @Override
    public <T> DynamicModifierTextureProviderBuilder<T> getDynamicModifierTextureProviderBuilder(@Nullable ResourceLocation defaultTextureLocation) {
        return ModifierTextureProviders.dynamic(defaultTextureLocation);
    }

    @Override
    public <T extends Comparable<? super T>> RangedModifierTextureProviderBuilder<T> getRangedModifierTextureProviderBuilder(@Nullable ResourceLocation defaultTextureLocation) {
        return ModifierTextureProviders.ranged(defaultTextureLocation);
    }
}
