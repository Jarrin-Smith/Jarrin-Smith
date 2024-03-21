import java.util.*;
import java.util.function.Consumer;

class Reachability extends GenericDFS {
    Hashtable<Node, Set<Node>> table;

    /**
     *
     * This is the most complex traversal. So do this one last.
     *
     * The goal here is to collect all the nodes that can reach
     * a particular node. Say we have an edge
     *
     *   X -> Y
     *
     * When we visit X, we may have accumulated some information about all the nodes
     * known to reach X so far, and all the nodes known to reach Y so far. The
     * existence of the edge X -> Y says that X can reach Y and
     * everything that is known to reach X can now also reach Y.
     *
     *
     */
    Reachability(Hashtable<Node, List<Node>> neighbors, Node start) {
        super(neighbors);
        table = new Hashtable<>();
        for (Node n: neighbors.keySet()) {
            table.put(n, new HashSet<>());
        }

        Consumer consum = n -> neighbors.get(n).forEach(neighbor -> {table.get(neighbor).addAll(table.get(n)); table.get(neighbor).add((Node) n);});
        setEnterConsumer(consum);
        setTouchConsumer(consum);
        setExitConsumer(consum);
        traverse(start);
    }
}
