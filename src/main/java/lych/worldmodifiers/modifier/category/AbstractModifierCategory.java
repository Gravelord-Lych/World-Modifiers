package lych.worldmodifiers.modifier.category;

import lych.worldmodifiers.modifier.SortingPriority;

import java.util.*;

public abstract class AbstractModifierCategory extends AbstractModifierSetEntry implements ModifierCategory {
    private final Set<ModifierSetEntry> children = new TreeSet<>(getComparator());

    protected AbstractModifierCategory(String id, String name, SortingPriority priority) {
        super(ModifierCategory.NAME, id, name, priority);
    }

    @Override
    public void addChildModifier(Modifier<?> modifier) {
        addChild(modifier);
    }

    @Override
    public ModifierCategory addSubCategory(String id, String name, SortingPriority priority) {
        SubModifierCategory sub = new SubModifierCategory(id, name, priority, this);
        addChild(sub);
        return sub;
    }

    private void addChild(ModifierSetEntry entry) {
        children.add(entry);
    }

    @Override
    public Set<ModifierSetEntry> getChildren() {
        return Collections.unmodifiableSet(children);
    }

    public Comparator<ModifierSetEntry> getComparator() {
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
                return e1.getName().compareTo(e2.getName());
            }
        };
    }
}
