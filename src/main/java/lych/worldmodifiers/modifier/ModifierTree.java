package lych.worldmodifiers.modifier;

import com.google.common.graph.Graph;
import com.google.common.graph.GraphBuilder;
import com.google.common.graph.ImmutableGraph;
import com.google.common.graph.MutableGraph;
import lych.worldmodifiers.api.APIUtils;
import lych.worldmodifiers.api.modifier.Modifier;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("UnstableApiUsage")
final class ModifierTree {
    private static final MutableGraph<Modifier<?>> TREE = GraphBuilder.undirected().build();
    private static final Map<Modifier<?>, Modifier<?>> PARENT = new HashMap<>();
    /**
     * The dummy root node of the modifier tree.
     */
    private static final Modifier<?> ROOT = APIUtils.createDummyIntModifier();

    static {
        TREE.addNode(ROOT);
    }

    private ModifierTree() {}

    static void add(Modifier<?> modifier) {
        add(ROOT, modifier);
    }

    static void add(Modifier<?> parent, Modifier<?> child) {
        TREE.putEdge(parent, child);
        PARENT.put(child, parent);
    }

    static Modifier<?> getRoot() {
        return ROOT;
    }

    static Modifier<?> getParent(Modifier<?> modifier) {
        return PARENT.get(modifier);
    }

    static Graph<Modifier<?>> viewTree() {
        return ImmutableGraph.copyOf(TREE);
    }
}
