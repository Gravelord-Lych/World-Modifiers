package lych.worldmodifiers.modifier;

import com.google.common.base.MoreObjects;
import net.minecraft.nbt.CompoundTag;

import java.util.Objects;

public final class BooleanModifier implements Modifier<Boolean> {
    private final String name;
    private final boolean defaultValue;

    public BooleanModifier(String name, boolean defaultValue) {
        this.name = name;
        this.defaultValue = defaultValue;
        NameToModifierMap.put(this);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Boolean getDefaultValue() {
        return defaultValue;
    }

    @Override
    public CompoundTag save(Boolean value) {
        CompoundTag tag = new CompoundTag();
        tag.putBoolean(name, value);
        return tag;
    }

    @Override
    public Boolean load(CompoundTag tag) {
        return tag.getBoolean(name);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("name", name)
                .add("defaultValue", defaultValue)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BooleanModifier that = (BooleanModifier) o;
        return getDefaultValue() == that.getDefaultValue() && Objects.equals(getName(), that.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getDefaultValue());
    }
}
