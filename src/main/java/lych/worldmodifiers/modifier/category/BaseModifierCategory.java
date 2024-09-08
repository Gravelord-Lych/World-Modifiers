package lych.worldmodifiers.modifier.category;

import lych.worldmodifiers.modifier.SortingPriority;

import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

public final class BaseModifierCategory extends AbstractModifierCategory implements Comparable<BaseModifierCategory> {
    private static final Set<BaseModifierCategory> VALUES = new TreeSet<>();

    private BaseModifierCategory(String id, SortingPriority priority) {
        super(id, "base", priority);
    }

    public static BaseModifierCategory createBaseCategoryOf(String id) {
        return createBaseCategoryOf(id, SortingPriority.NORMAL);
    }

    public static BaseModifierCategory createBaseCategoryOf(String id, SortingPriority priority) {
        BaseModifierCategory baseModifierCategory = new BaseModifierCategory(id, priority);
        VALUES.add(baseModifierCategory);
        return baseModifierCategory;
    }

    @Override
    public ModifierCategory getParent() {
        return null;
    }

    @Override
    public String toString() {
        return "BaseModifierCategory[" + getName().getNamespace() + "]";
    }

    public static Set<BaseModifierCategory> viewValues() {
        return Collections.unmodifiableSet(VALUES);
    }

    @Override
    public int compareTo(BaseModifierCategory o) {
        int priorityComparisonResult = getPriority().compareTo(o.getPriority());
        if (priorityComparisonResult != 0) {
            return priorityComparisonResult;
        }
        return getName().compareTo(o.getName());
    }
}
