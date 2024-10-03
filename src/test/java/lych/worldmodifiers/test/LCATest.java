package lych.worldmodifiers.test;

import com.google.common.graph.GraphBuilder;
import com.google.common.graph.ImmutableGraph;
import lych.worldmodifiers.util.LCA;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@SuppressWarnings("UnstableApiUsage")
@Tag("lca")
public class LCATest {
    @Test
    public void testLCA() {
        ImmutableGraph.Builder<Integer> builder = GraphBuilder.undirected().immutable();
        builder.putEdge(1, 2);
        builder.putEdge(1, 3);
        builder.putEdge(1, 4);
        builder.putEdge(2, 5);
        builder.putEdge(2, 6);
        builder.putEdge(6, 7);
        builder.putEdge(6, 8);
        builder.putEdge(4, 9);
        builder.putEdge(4, 10);
        builder.putEdge(4, 11);
        builder.putEdge(11, 12);
        builder.putEdge(11, 13);
        builder.putEdge(13, 14);
        LCA<Integer> lca = new LCA<>(builder.build(), 1, 0);
        Assertions.assertEquals(9, lca.findLCA(9, 9));
        Assertions.assertEquals(1, lca.findLCA(1, 4));
        Assertions.assertEquals(1, lca.findLCA(7, 1));
        Assertions.assertEquals(4, lca.findLCA(4, 14));
        Assertions.assertEquals(2, lca.findLCA(5, 2));
        Assertions.assertEquals(1, lca.findLCA(7, 10));
        Assertions.assertEquals(1, lca.findLCA(3, 12));
        Assertions.assertEquals(2, lca.findLCA(7, 5));
        Assertions.assertEquals(11, lca.findLCA(12, 13));
        Assertions.assertEquals(1, lca.getDepth(1));
        Assertions.assertEquals(3, lca.getDepth(6));
        Assertions.assertEquals(4, lca.getDepth(8));
        Assertions.assertEquals(3, lca.getDepth(11));
    }
}
