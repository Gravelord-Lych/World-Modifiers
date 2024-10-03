package lych.worldmodifiers.util;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.google.common.graph.Graph;
import com.google.common.graph.Graphs;
import com.google.common.math.IntMath;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;

import java.math.RoundingMode;
import java.util.Objects;

@SuppressWarnings("UnstableApiUsage")
public class LCA<T> {
    private final Graph<T> graph;
    private final Table<T, Integer, T> parent = HashBasedTable.create();
    private final Object2IntMap<T> depth = new Object2IntOpenHashMap<>();

    public LCA(Graph<T> graph, T root, T defaultNodeValue) {
        this.graph = graph;
        depth.defaultReturnValue(0);
        depth.put(defaultNodeValue, 0);
        if (graph.isDirected() || graph.edges().size() != graph.nodes().size() - 1 || Graphs.hasCycle(graph)) {
            throw new IllegalArgumentException("Graph must be a tree");
        }
        init(root, defaultNodeValue);
    }

    private void init(T x, T f) {
        parent.put(x, 0, f);
        depth.put(x, depth.getInt(f) + 1);

        for (int i = 1; i <= log2(depth.getInt(x)); i++) {
            parent.put(x, i, parent.get(parent.get(x, i - 1), i - 1));
        }

        for (T node : graph.adjacentNodes(x)) {
            if (node != f) {
                init(node, x);
            }
        }
    }

    public T findLCA(T a, T b) {
        if (depth.getInt(a) < depth.getInt(b)) {
            T temp = a;
            a = b;
            b = temp;
        }
        while (depth.getInt(a) > depth.getInt(b)) {
            a = parent.get(a, log2(depth.getInt(a) - depth.getInt(b)));
        }

        if (a == b) {
            return a;
        }

        for (int i = log2(depth.getInt(a)); i >= 0; i--) {
            if (parent.get(a, i) != parent.get(b, i)) {
                a = parent.get(a, i);
                b = parent.get(b, i);
            }
        }

        return Objects.requireNonNull(parent.get(a, 0));
    }

    public int getDepth(T node) {
        return depth.getInt(node);
    }

    private static int log2(int num) {
        return IntMath.log2(num, RoundingMode.DOWN);
    }
}
