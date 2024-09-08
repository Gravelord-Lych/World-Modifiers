package lych.worldmodifiers.modifier;

import lych.worldmodifiers.modifier.category.BaseModifierCategory;
import lych.worldmodifiers.modifier.category.Modifier;
import lych.worldmodifiers.modifier.category.ModifierCategory;
import lych.worldmodifiers.modifier.category.ModifierSetEntry;

import javax.annotation.Nullable;

public final class ModifierVisitor implements ModifierEntryVisitor<ModifierSetEntry> {
    private final ModifierEntryVisitor<? super ModifierCategory> categoryVisitor;
    private final ModifierEntryVisitor<? super Modifier<?>> modifierVisitor;

    private ModifierVisitor(ModifierEntryVisitor<? super ModifierCategory> categoryVisitor,
                            ModifierEntryVisitor<? super Modifier<?>> modifierVisitor) {
        this.categoryVisitor = categoryVisitor;
        this.modifierVisitor = modifierVisitor;
    }

    public static ModifierVisitor of(ModifierEntryVisitor<? super ModifierSetEntry> visitor) {
        return new ModifierVisitor(visitor, visitor);
    }


    public static ModifierVisitor of(ModifierEntryVisitor<? super ModifierCategory> categoryVisitor,
                                     ModifierEntryVisitor<? super Modifier<?>> modifierVisitor) {
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
    private void recursivelyVisit(int depth, @Nullable ModifierCategory parent, ModifierSetEntry entry) {
        boolean visitChildren = visitEntry(depth, parent, entry);
        if (!visitChildren) {
            return;
        }
        for (ModifierSetEntry child : entry.getChildren()) {
            if (!(entry instanceof ModifierCategory category)) {
                throw new AssertionError("Only modifier category can have children, found: " + entry.getClass().getSimpleName());
            }
            recursivelyVisit(depth + 1, category, child);
        }
    }

    @Override
    public boolean visitEntry(int depth, @Nullable ModifierCategory parent, ModifierSetEntry entry) {
        if (entry instanceof ModifierCategory category) {
            return categoryVisitor.visitEntry(depth, parent, category);
        } else if (entry instanceof Modifier<?> modifier) {
            return modifierVisitor.visitEntry(depth, parent, modifier);
        } else {
            throw new AssertionError("Invalid entry type: " + entry.getClass().getSimpleName());
        }
    }
}
