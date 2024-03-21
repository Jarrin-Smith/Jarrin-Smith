import java.util.Hashtable;
import java.util.List;

public class AllShortestPaths extends GraphTraversal {

    AllShortestPaths(Hashtable<Node, List<Edge>> neighbors) {
        super(neighbors);
    }

    /**
     * This is the core of the all shortest paths algorithm. Given an edge e
     * from A -> B with weight w and a fresh destination B, we do the following:
     *   - let the current distance to B be db
     *   - the current edge provides another way to reach B via a cost
     *     that is the current distance to A + the weight w
     *   - if the new distance is not better, do nothing
     *   - otherwise, update B to record the new improved distance
     *     and the fact that this distance is via the edge e
     */
    void relax(Edge e) {
        if(!e.destination().isFresh()) return;
        Distance d = e.weight().add(e.source().getDistance());
        if(d.compareTo(e.destination().getDistance()) < 0){
            e.destination().setDistance(d);
            e.destination().setPreviousEdge(e);
            heap.moveUp(e.destination());
        }
    }

    void fromSource(Node source) {
        source.setDistance(new Finite(0));
        heap.moveUp(source);
        traverse();
    }

}
