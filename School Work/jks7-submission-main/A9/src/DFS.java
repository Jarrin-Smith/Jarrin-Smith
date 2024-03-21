import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

class DFS extends GenericDFS {
    List<Node> traversal;

    /**
     *
     * This is a simple depth first traversal. Every time you visit a node
     * for the first time, you add it to the result.
     *
     */

    DFS(Hashtable<Node, List<Node>> neighbors, Node start) {
        super(neighbors);
        traversal = new ArrayList<>();
        setEnterConsumer(traversal::add);
        traverse(start);
        }
    }
