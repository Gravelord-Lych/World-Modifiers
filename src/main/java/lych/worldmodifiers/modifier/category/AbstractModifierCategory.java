package lych.worldmodifiers.modifier.category;

import lych.worldmodifiers.api.modifier.category.ModifierCategory;
import lych.worldmodifiers.modifier.AbstractBaseModifier;
import lych.worldmodifiers.api.modifier.BaseModifier;
import lych.worldmodifiers.api.modifier.Modifier;
import lych.worldmodifiers.api.modifier.SortingPriority;

import java.util.*;

public abstract class AbstractModifierCategory extends AbstractBaseModifier implements ModifierCategory {
    private final Set<BaseModifier> children = new TreeSet<>(getComparator());

    protected AbstractModifierCategory(String id, String name, SortingPriority priority) {
        super(ModifierCategory.NAME, id, name, priority);
    }

    @Override
    public void addChildModifier(Modifier<?> modifier) {
        addChild(modifier);
    }

    @Override
    public ModifierCategory addSubCategory(String namespace, String name, SortingPriority priority) {
        ModifierCategory sub = new SubModifierCategory(namespace, name, priority, this);
        addChild(sub);
        return sub;
    }

    private void addChild(BaseModifier entry) {
        if (entry.getParent() == null) {
            throw new IllegalArgumentException("Modifier or category has no parent.");
        }
        if (entry.getParent() != null && entry.getParent() != this) {
            throw new IllegalArgumentException("Modifier or category already has a different parent.");
        }
        children.add(entry);
    }

    @Override
    public Set<BaseModifier> getChildren() {
        return Collections.unmodifiableSet(children);
    }

    public Comparator<BaseModifier> getComparator() {
        return (e1, e2) -> {
            if (e1 instanceof ModifierCategory && e2 instanceof Modifier) {
                return 1;
            } else if (e1 instanceof Modifier && e2 instanceof ModifierCategory) {
                return -1;
            } else {
                int priorityComparisonResult = e1.getPriority().compareTo(e2.getPriority());
                if (priorityComparisonResult != 0) {
                    return priorityComparisonResult;
                }
                return e1.getFullName().compareTo(e2.getFullName());
            }
        };
    }
}
