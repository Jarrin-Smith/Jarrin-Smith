import java.util.*;

public class Heap {
    /**
     * The heap is represented as an array as explained in class.
     * We want to get a node from its index and vice-versa. The
     * HashMap indices maps each node to its index. It must
     * be maintained consistently: in other words if the nodes
     * move in the array, their index must be updated.
     */
    private final List<Node> nodes;
    private final HashMap<Node, Integer> indices;
    private int size;

    Heap(Set<Node> nodes) {
        this.nodes = new ArrayList<>(nodes);
        this.size = nodes.size();
        this.indices = new HashMap<>();
        for (int i = 0; i < size; i++) indices.put(this.nodes.get(i), i);
        heapify();
    }

    /**
     * Assumes the array of nodes is initialized but the nodes are in no
     * particular order.
     * <p>
     * As explained in class, the goal is to re-arrange the nodes so that
     * they form a proper min-heap where each is less than its children.
     */
    void heapify() {
        for (int i = nodes.size()/2; i >=0 ; i--) {
            moveDown(nodes.get(i));
        }
    }

    boolean isEmpty() {
        return size == 0;
    }

    /**
     * The next few methods return the appropriate node if it is within bounds.
     * Otherwise, they return an Optional.empty
     */
    Optional<Node> getParent(Node n) {
        if (indices.get(n).equals(0)) {
            return Optional.empty();
        }
        int index = (indices.get(n)-1)/2;
        if(index >= 0 && index < nodes.size()) {
            return Optional.of(nodes.get(index));
        }
        return Optional.empty();
    }

    Optional<Node> getLeftChild(Node n) {
        int index = (nodes.indexOf(n) * 2) + 1;
        if (index < nodes.size() && index >= 0) { return Optional.of(nodes.get(index)); }
        return Optional.empty();
    }

    Optional<Node> getRightChild(Node n) {
        int index = (nodes.indexOf(n) * 2) + 2;
        if (index < nodes.size() && index >= 0) { return Optional.of(nodes.get(index)); }
        return Optional.empty();
    }

    Optional<Node> getMinChild(Node n) {
        if (getLeftChild(n).equals(Optional.empty()) && getRightChild(n).equals(Optional.empty())) { return Optional.empty(); }

        else if (getLeftChild(n).equals(Optional.empty()) && !getRightChild(n).equals(Optional.empty())) { return getRightChild(n); }

        else if (!getLeftChild(n).equals(Optional.empty()) && getRightChild(n).equals(Optional.empty())) { return getLeftChild(n); }

        else if(getLeftChild(n).get().compareTo(getRightChild(n).get()) > 0) { return getRightChild(n); }

        else { return getLeftChild(n); }
    }

    /**
     * Swaps the two given nodes in the array
     * of nodes making sure we also update their
     * indices.
     */
    void swap(Node n1, Node n2) {
        int index1 = indices.get(n1);
        int index2 = indices.get(n2);

        Node temp = nodes.get(index1);
        nodes.set(index1, nodes.get(index2));
        nodes.set(index2, temp);

        indices.put(nodes.get(index1), index1);
        indices.put(nodes.get(index2), index2);
    }

    /**
     * Recursively move this node down until the heap property
     * is established
     */
    void moveDown(Node n) {
        if(getMinChild(n).isPresent()){
            if(n.compareTo(getMinChild(n).get()) > 0){
                swap(n, getMinChild(n).get());
                moveDown(n);
            }
        }
    }

    /**
     * Recursively move this node up until the heap property
     * is established
     */
    void moveUp(Node n) {
        if(getParent(n).isPresent()){
            if(n.compareTo(getParent(n).get()) < 0){
                swap(n, getParent(n).get());
                moveUp(n);
            }
        }
    }

    void insert(Node n) {
        indices.put(n, size);
        size++;
        nodes.add(n);
        moveUp(n);
    }

    /**
     * Return the minimum node in the heap (which is at the root).
     * Re-arrange the heap appropriately
     */
    Node extractMin() {
        if(size == 1){
            size = 0;
            return nodes.get(0);
        }
        Node min = nodes.get(0);
        swap(min, nodes.get(nodes.size() - 1));
        indices.remove(min);
        nodes.remove(nodes.size()-1);
        moveDown(nodes.get(0));
        size--;
        return min;
    }
}

