package lych.worldmodifiers.modifier;

import net.minecraft.nbt.CompoundTag;

import javax.annotation.concurrent.Immutable;

@Immutable
public interface Modifier<T> {
    String getName();

    T getDefaultValue();

    CompoundTag save(T value);

    T load(CompoundTag tag);
}
