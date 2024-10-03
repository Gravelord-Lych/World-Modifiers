package lych.worldmodifiers.modifier;

import lych.worldmodifiers.api.APIUtils;
import lych.worldmodifiers.api.modifier.BaseModifier;
import lych.worldmodifiers.api.modifier.Modifier;
import lych.worldmodifiers.util.LCA;

import javax.annotation.Nullable;
import java.util.*;

public class ModifierConflictChecker {
    public static final String CONFLICT_MESSAGE = "conflictDetected";
    private final LCA<Modifier<?>> lcaHelper;
    private final Set<Modifier<?>> potentialConflictModifiers = new HashSet<>();
    private final Set<Modifier<?>> conflictModifiers = new HashSet<>();
    private final Map<Modifier<?>, NodeData> nodeData = new HashMap<>();

    public ModifierConflictChecker() {
        this.lcaHelper = new LCA<>(ModifierTree.viewTree(), ModifierTree.getRoot(), APIUtils.createDummyIntModifier());
    }

    public ModifierConflictChecker(ModifierMap modifierMap) {
        this();
        NameToModifierMap.viewAll().values().stream().filter(modifierMap::valueChanged).forEach(this::add);
    }

    public void add(Modifier<?> modifier) {
        for (Modifier<?> potentialConflictModifier : potentialConflictModifiers) {
            if (potentialConflictModifier == modifier) {
                continue;
            }
            Modifier<?> lca = lcaHelper.findLCA(modifier, potentialConflictModifier);
            if (lca == modifier || lca == potentialConflictModifier) {
                Modifier<?> child = lca == modifier ? potentialConflictModifier : modifier;
                conflictModifiers.add(lca);
                conflictModifiers.add(child);

                Modifier<?> childParent = data(child).parent;
                if (childParent != null) {
                    int deltaDepth0 = lcaHelper.getDepth(child) - lcaHelper.getDepth(childParent);
                    int deltaDepth = lcaHelper.getDepth(child) - lcaHelper.getDepth(lca);
                    if (deltaDepth0 > deltaDepth) {
                        data(childParent).removeChild(child);
                        data(childParent).addChild(lca);
                        data(lca).parent = childParent;
                        data(lca).addChild(child);
                        data(child).parent = lca;
                    }
                } else {
                    data(lca).addChild(child);
                    data(child).parent = lca;
                }
            }
        }
        potentialConflictModifiers.add(modifier);
    }

    public void remove(Modifier<?> modifier) {
        potentialConflictModifiers.remove(modifier);
        if (conflictModifiers.contains(modifier)) {
            Set<Modifier<?>> affectedModifiers = new HashSet<>();
            conflictModifiers.remove(modifier);
            @Nullable
            Modifier<?> parent = data(modifier).parent;
            for (Iterator<Modifier<?>> iterator = data(modifier).children.iterator(); iterator.hasNext(); ) {
                Modifier<?> child = iterator.next();
                iterator.remove();
                affectedModifiers.add(child);
                data(child).parent = parent;
                if (parent != null) {
                    data(parent).addChild(child);
                }
            }
            if (parent != null) {
                affectedModifiers.add(parent);
                data(parent).removeChild(modifier);
                data(modifier).parent = null;
            }
            for (Modifier<?> affectedModifier : affectedModifiers) {
                if (data(affectedModifier).canBeRemovedFromConflictSet()) {
                    conflictModifiers.remove(affectedModifier);
                }
            }
        }
    }

    public Set<Modifier<?>> getConflictModifiers() {
        return conflictModifiers;
    }

    public List<List<ConflictMessage>> getConflictMessageGroups() {
        Set<List<ConflictMessage>> conflictMessageGroups = new TreeSet<>(Comparator.comparing(List::getFirst));
        conflictModifiers.stream().sorted(BaseModifier.getComparator()).forEach(conflictModifier -> {
            List<ConflictMessage> conflictMessages = new ArrayList<>();
            Modifier<?> parent = data(conflictModifier).parent;
            while (parent != null) {
                conflictMessages.add(new ConflictMessage(conflictModifier, parent));
                parent = data(parent).parent;
            }
            if (!conflictMessages.isEmpty()) {
                conflictMessageGroups.add(List.copyOf(conflictMessages));
            }
        });
        return List.copyOf(conflictMessageGroups);
    }

    private NodeData data(Modifier<?> modifier) {
        return nodeData.computeIfAbsent(modifier, m -> new NodeData());
    }

    private static class NodeData {
        @Nullable
        private Modifier<?> parent;
        private final Set<Modifier<?>> children = new HashSet<>();

        private void addChild(Modifier<?> child) {
            children.add(child);
        }

        private void removeChild(Modifier<?> child) {
            children.remove(child);
        }

        private boolean canBeRemovedFromConflictSet() {
            return parent == null && children.isEmpty();
        }
    }
}
