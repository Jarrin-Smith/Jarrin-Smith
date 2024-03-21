import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class GraphTraversalTest {

    @Test
    void shortestPath1() {
        Node s = new Node("s");
        Node a = new Node("a");
        Node b = new Node("b");
        Node c = new Node("c");
        Node f = new Node("f");
        Node t = new Node("t");

        Edge sa = new Edge(s, a, new Finite(3));
        Edge sb = new Edge(s, b, new Finite(4));
        Edge ab = new Edge(a, b, new Finite(6));
        Edge bf = new Edge(b, f, new Finite(5));
        Edge af = new Edge(a, f, new Finite(7));
        Edge ac = new Edge(a, c, new Finite(2));
        Edge cf = new Edge(c, f, new Finite(1));
        Edge ct = new Edge(c, t, new Finite(8));
        Edge ft = new Edge(f, t, new Finite(4));

        Hashtable<Node, List<Edge>> neighbors = new Hashtable<>();
        neighbors.put(s, List.of(sa, sb));
        neighbors.put(a, List.of(ab, af, ac));
        neighbors.put(b, List.of(bf));
        neighbors.put(c, List.of(cf, ct));
        neighbors.put(f, List.of(ft));
        neighbors.put(t, List.of());

        AllShortestPaths g = new AllShortestPaths(neighbors);
        g.fromSource(s);
        assertEquals(new Finite(0), s.getDistance());
        assertEquals(new Finite(3), a.getDistance());
        assertEquals(new Finite(4), b.getDistance());
        assertEquals(new Finite(5), c.getDistance());
        assertEquals(new Finite(6), f.getDistance());
        assertEquals(new Finite(10), t.getDistance());
    }

    @Test
    public void shortestPath2() {
        Node a0 = new Node("0");
        Node a1 = new Node("1");
        Node a2 = new Node("2");
        Node a3 = new Node("3");
        Node a4 = new Node("4");
        Node a5 = new Node("5");
        Node a6 = new Node("6");
        Node a7 = new Node("7");

        Edge e04 = new Edge(a0, a4, new Finite(6));
        Edge e13 = new Edge(a1, a3, new Finite(5));
        Edge e16 = new Edge(a1, a6, new Finite(2));
        Edge e24 = new Edge(a2, a4, new Finite(7));
        Edge e25 = new Edge(a2, a5, new Finite(7));
        Edge e35 = new Edge(a3, a5, new Finite(2));
        Edge e37 = new Edge(a3, a7, new Finite(1));
        Edge e46 = new Edge(a4, a6, new Finite(3));
        Edge e57 = new Edge(a5, a7, new Finite(7));
        Edge e62 = new Edge(a6, a2, new Finite(7));
        Edge e67 = new Edge(a6, a7, new Finite(6));

        Hashtable<Node, List<Edge>> neighbors = new Hashtable<>();
        neighbors.put(a0, List.of(e04));
        neighbors.put(a1, List.of(e13, e16));
        neighbors.put(a2, List.of(e24, e25));
        neighbors.put(a3, List.of(e35, e37));
        neighbors.put(a4, List.of(e46));
        neighbors.put(a5, List.of(e57));
        neighbors.put(a6, List.of(e62, e67));
        neighbors.put(a7, List.of());

        AllShortestPaths g = new AllShortestPaths(neighbors);
        g.fromSource(a0);
        assertEquals(new Finite(0), a0.getDistance());
        assertEquals(Infinity.getInstance(), a1.getDistance());
        assertEquals(new Finite(16), a2.getDistance());
        assertEquals(Infinity.getInstance(), a3.getDistance());
        assertEquals(new Finite(6), a4.getDistance());
        assertEquals(new Finite(23), a5.getDistance());
        assertEquals(new Finite(9), a6.getDistance());
        assertEquals(new Finite(15), a7.getDistance());
    }

    @Test
    public void spanningTree1() {
        Node s = new Node("s");
        Node a = new Node("a");
        Node b = new Node("b");
        Node c = new Node("c");
        Node d = new Node("d");
        Node t = new Node("t");

        Edge sa = new Edge(s, a, new Finite(7));
        Edge sc = new Edge(s, c, new Finite(8));
        Edge ab = new Edge(a, b, new Finite(6));
        Edge ac = new Edge(a, c, new Finite(3));
        Edge cb = new Edge(c, b, new Finite(4));
        Edge cd = new Edge(c, d, new Finite(3));
        Edge bd = new Edge(b, d, new Finite(2));
        Edge bt = new Edge(b, t, new Finite(5));
        Edge dt = new Edge(d, t, new Finite(2));

        Hashtable<Node, List<Edge>> neighbors = new Hashtable<>();
        neighbors.put(s, List.of(sa, sc));
        neighbors.put(a, List.of(ac, ab, sa.flip()));
        neighbors.put(b, List.of(bd, bt, ab.flip(), cb.flip()));
        neighbors.put(c, List.of(cb, cd, ac.flip(), sc.flip()));
        neighbors.put(d, List.of(dt, bd.flip(), cd.flip()));
        neighbors.put(t, List.of(bt.flip(), dt.flip()));

        MinSpanningTree graph = new MinSpanningTree(neighbors);

        Set<Edge> tree = graph.fromSource(s);
        assertEquals(5, tree.size());
        assertTrue(tree.contains(sa));
        assertTrue(tree.contains(ac));
        assertTrue(tree.contains(cd));
        assertTrue(tree.contains(bd.flip()));
        assertTrue(tree.contains(dt));
    }

    @Test
    void myTest1() {
        Node s = new Node("s");
        Node a = new Node("a");
        Node b = new Node("b");
        Node c = new Node("c");
        Node f = new Node("f");
        Node t = new Node("t");

        Edge sa = new Edge(s, a, new Finite(5));
        Edge sb = new Edge(s, b, new Finite(7));
        Edge ab = new Edge(a, b, new Finite(8));
        Edge bf = new Edge(b, f, new Finite(2));
        Edge af = new Edge(a, f, new Finite(9));
        Edge ac = new Edge(a, c, new Finite(1));
        Edge cf = new Edge(c, f, new Finite(4));
        Edge ct = new Edge(c, t, new Finite(3));
        Edge ft = new Edge(f, t, new Finite(9));

        Hashtable<Node, List<Edge>> neighbors = new Hashtable<>();
        neighbors.put(s, List.of(sa, sb));
        neighbors.put(a, List.of(ab, af, ac));
        neighbors.put(b, List.of(bf));
        neighbors.put(c, List.of(cf, ct));
        neighbors.put(f, List.of(ft));
        neighbors.put(t, List.of());

        AllShortestPaths g = new AllShortestPaths(neighbors);
        g.fromSource(s);
        assertEquals(new Finite(0), s.getDistance());
        assertEquals(new Finite(5), a.getDistance());
        assertEquals(new Finite(7), b.getDistance());
        assertEquals(new Finite(6), c.getDistance());
        assertEquals(new Finite(9), f.getDistance());
        assertEquals(new Finite(9), t.getDistance());
    }

    @Test
    public void myTest2() {
        Node s = new Node("s");
        Node a = new Node("a");
        Node b = new Node("b");
        Node c = new Node("c");
        Node d = new Node("d");
        Node t = new Node("t");

        Edge sa = new Edge(s, a, new Finite(9));
        Edge sc = new Edge(s, c, new Finite(10));
        Edge ab = new Edge(a, b, new Finite(2));
        Edge ac = new Edge(a, c, new Finite(6));
        Edge cb = new Edge(c, b, new Finite(7));
        Edge cd = new Edge(c, d, new Finite(9));
        Edge bd = new Edge(b, d, new Finite(1));
        Edge bt = new Edge(b, t, new Finite(7));
        Edge dt = new Edge(d, t, new Finite(8));

        Hashtable<Node, List<Edge>> neighbors = new Hashtable<>();
        neighbors.put(s, List.of(sa, sc));
        neighbors.put(a, List.of(ac, ab, sa.flip()));
        neighbors.put(b, List.of(bd, bt, ab.flip(), cb.flip()));
        neighbors.put(c, List.of(cb, cd, ac.flip(), sc.flip()));
        neighbors.put(d, List.of(dt, bd.flip(), cd.flip()));
        neighbors.put(t, List.of(bt.flip(), dt.flip()));

        MinSpanningTree graph = new MinSpanningTree(neighbors);

        Set<Edge> tree = graph.fromSource(s);
        assertEquals(5, tree.size());
        assertTrue(tree.contains(sa));
        assertTrue(tree.contains(ac));
        assertFalse(tree.contains(cd));
        assertFalse(tree.contains(bd.flip()));
        assertFalse(tree.contains(dt));
    }

}
