package lych.worldmodifiers.modifier;

import lych.worldmodifiers.api.modifier.BaseModifier;
import lych.worldmodifiers.api.modifier.Modifier;
import lych.worldmodifiers.modifier.category.BaseModifierCategory;
import lych.worldmodifiers.api.modifier.category.ModifierCategory;

import javax.annotation.Nullable;

public final class ModifierVisitor implements SingleModifierVisitor<BaseModifier> {
    private final SingleModifierVisitor<? super ModifierCategory> categoryVisitor;
    private final SingleModifierVisitor<? super Modifier<?>> modifierVisitor;

    private ModifierVisitor(SingleModifierVisitor<? super ModifierCategory> categoryVisitor,
                            SingleModifierVisitor<? super Modifier<?>> modifierVisitor) {
        this.categoryVisitor = categoryVisitor;
        this.modifierVisitor = modifierVisitor;
    }

    public static ModifierVisitor of(SingleModifierVisitor<? super BaseModifier> visitor) {
        return new ModifierVisitor(visitor, visitor);
    }


    public static ModifierVisitor of(SingleModifierVisitor<? super ModifierCategory> categoryVisitor,
                                     SingleModifierVisitor<? super Modifier<?>> modifierVisitor) {
        return new ModifierVisitor(categoryVisitor, modifierVisitor);
    }

    public void visitAll() {
        for (BaseModifierCategory baseCategory : BaseModifierCategory.viewValues()) {
            recursivelyVisit(0, null, baseCategory);
        }
    }

    /**
     * Recursively visits all modifier categories.
     */
    private void recursivelyVisit(int depth, @Nullable ModifierCategory parent, BaseModifier entry) {
        boolean visitChildren = visit(depth, parent, entry);
        if (!visitChildren) {
            return;
        }
        for (BaseModifier child : entry.getChildren()) {
            if (!(entry instanceof ModifierCategory category)) {
                throw new AssertionError("Only modifier category can have children, found: " + entry.getClass().getSimpleName());
            }
            recursivelyVisit(depth + 1, category, child);
        }
    }

    @Override
    public boolean visit(int depth, @Nullable ModifierCategory parent, BaseModifier baseModifier) {
        if (baseModifier instanceof ModifierCategory category) {
            return categoryVisitor.visit(depth, parent, category);
        } else if (baseModifier instanceof Modifier<?> modifier) {
            return modifierVisitor.visit(depth, parent, modifier);
        } else {
            throw new AssertionError("Invalid entry type: " + baseModifier.getClass().getSimpleName());
        }
    }
}
