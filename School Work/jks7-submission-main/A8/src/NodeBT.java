import com.sun.java.accessibility.util.EventQueueMonitor;
import com.sun.source.tree.Tree;
import org.w3c.dom.Node;

import java.util.*;

public class NodeBT<E extends Comparable<E>> extends BinaryTree<E> {
    private final E data;
    private final BinaryTree<E> left, right;
    private final int height;

    NodeBT (E data, BinaryTree<E> left, BinaryTree<E> right) {
        this.data = data;
        this.left = left;
        this.right = right;
        this.height = Math.max(left.height(), right.height()) + 1;
    }

    /**
     * This method is used as a helper for the AVL methods. It is like a constructor
     * but it applies the necessary rotations if needed to make sure the tree
     * is balanced.
     *
     * There are three cases to consider:
     *
     * left.height > right.height + 1
     *
     *   the situation looks like this:
     *
     *                 data
     *              /        \
     *        left          right
     *       /     \
     *   left2     right2
     *
     *   We definitely must rotate the entire tree to the right
     *   We might have to rotate left to the left
     *
     * right.height > left.height + 1, which is symmetric
     *
     * otherwise we just build the tree without any rotations
     */
    static <E extends Comparable<E>> NodeBT<E> mkBalancedNodeBT (E data, BinaryTree<E> left, BinaryTree<E> right) {
        NodeBT node = new NodeBT(data, left, right);
            if (left.height() > right.height() + 1) {
                if (left instanceof NodeBT<E> leftNode) {
                    return new NodeBT<>(data, leftNode.mayBeRotateLeft(), right).rotateRight();
                } else {
                    throw new Error();
                }
            }
            else if(right.height() > left.height() + 1){
                if(right instanceof NodeBT<E> rightNode){
                    return new NodeBT<>(data, left, rightNode.mayBeRotateRight()).rotateLeft();
                }
                else{
                    throw new Error();
                }
            }
            else{
                return node;
            }
    }

    // Access fields

    E getData () { return data; }
    BinaryTree<E> getLeftBT () { return left; }
    BinaryTree<E> getRightBT () { return right; }

    // Basic properties

    boolean isEmpty () { return false; }
    int height() { return height; }
    boolean isBalanced() { return Math.abs(left.height() - right.height()) < 2; }

    // Traversals that return lists

    /**
     * The next three methods return a list of the data at each node in preorder,
     * inorder, or postorder
     */
    List<E> preOrderList() {
        List<E> list = new ArrayList<>();
        list.add(data);
        list.addAll(this.left.preOrderList());
        list.addAll(this.right.preOrderList());
        return list;
    }

    List<E> inOrderList() {
        List<E> list = new ArrayList<>();
        list.addAll(this.left.inOrderList());
        list.add(data);
        list.addAll(this.right.inOrderList());
        return list;
    }

    List<E> postOrderList() {
        List<E> list = new ArrayList<>();
        list.addAll(this.left.postOrderList());
        list.addAll(this.right.postOrderList());
        list.add(data);
        return list;
    }

    // Basic insert: always insert to the left but swaps the tree after every insert
    // to make sure the tree is balanced

    BinaryTree<E> insert (E elem) {
        return new NodeBT<>(data, right, left.insert(elem));
    }

    // Helpers for BST/AVL methods

    /**
     * Here is an example. Let the current tree be;
     *
     *              3
     *            /   \
     *           2     5
     *         /      /  \
     *        1      4    6
     *
     * the method returns a record with two components: the first component is the tree below
     *
     *              3
     *            /   \
     *           2     5
     *         /      /
     *        1      4
     *
     *
     * and the second component is the left 6
     *
     */
    TreeAndLeaf<E> extractRightMost () {
        try{
            right.extractRightMost();
            BinaryTree<E> btree;
            btree = new NodeBT(data, this.left, right.extractRightMost().tree());
            return new TreeAndLeaf<>(btree, right.extractRightMost().leaf());
        } catch (EmptyTreeE e) {
            return new TreeAndLeaf(this.left, data);
        }
    }

    /**
     * Exactly like the method above but ensure the tree is balanced
     */
    TreeAndLeaf<E> balancedExtractRightMost () {
        TreeAndLeaf extracted = this.extractRightMost();
        try {
            NodeBT finalData = mkBalancedNodeBT(extracted.tree().getData(), extracted.tree().getLeftBT(), extracted.tree().getRightBT());
            return new TreeAndLeaf<>(finalData, this.extractRightMost().leaf());
        } catch (EmptyTreeE e) {
            e.printStackTrace();
        }
        return null;
    }

        NodeBT<E> mayBeRotateLeft () {
        if (left.height() < right.height()) return rotateLeft();
        else return this;
    }

    NodeBT<E> mayBeRotateRight () {
        if (right.height() < left.height()) return rotateRight();
        else return this;
    }

    /**
     * Here is an example. If the current tree is:
     *
     *                5
     *             /    \
     *           2       8
     *                 /  \
     *                6    9
     *
     * we return
     *
     *                8
     *             /    \
     *           5       9
     *         /  \
     *       2     6
     *
     */
    NodeBT<E> rotateLeft () {
        try {
            return new NodeBT<>(right.getData(), new NodeBT<>(data, left, right.getLeftBT()),  right.getRightBT());
        } catch (EmptyTreeE e) {
            throw new Error();
        }
    }

    /**
     * Symmetric to the method above
     */
    NodeBT<E> rotateRight () {
        try {
            return new NodeBT<>(left.getData(), left.getLeftBT(), new NodeBT<>(data, left.getRightBT(), right));
        } catch (EmptyTreeE e) {
            throw new Error();
        }
    }

    // BST insertions, lookups, and deletions

    public BinaryTree<E> insertBST(E elem) {
        if (elem.compareTo(data) < 0)
            return new NodeBT<>(data, left.insertBST(elem), right);
        else return new NodeBT<>(data, left, right.insertBST(elem));
    }

    public boolean findBST(E elem) {
        int comp = elem.compareTo(data);
        if (comp < 0) return left.findBST(elem);
        else if (comp > 0) return right.findBST(elem);
        else return true;
    }

    public BinaryTree<E> deleteBST (E elem) {
        int comp = elem.compareTo(data);
        if (comp < 0) return new NodeBT<>(data,left.deleteBST(elem),right);
        else if (comp > 0) return new NodeBT<>(data,left,right.deleteBST(elem));
        else {
            try {
                TreeAndLeaf<E> treeLeaf = left.extractRightMost();
                return new NodeBT<>(treeLeaf.leaf(), treeLeaf.tree(), right);
            }
            catch (EmptyTreeE e) { return right; }
        }
    }

    // AVL insertions, lookups, and deletions

    /**
     * The following methods are similar to the BST variants but must
     * ensure that the trees are always balanced
     *
     */
    public BinaryTree<E> insertAVL(E elem) {
        if (elem.compareTo(data) < 0) {
            NodeBT node = mkBalancedNodeBT(data, left.insertAVL(elem), right);
            return node;
        }
        else{
            NodeBT node = mkBalancedNodeBT(data, left, right.insertAVL(elem));
            return node;
        }
    }

    public boolean findAVL (E elem) {
        int comp = elem.compareTo(data);
        if (comp < 0) return left.findAVL(elem);
        else if (comp > 0) return right.findAVL(elem);
        else return true;
    }

    public BinaryTree<E> deleteAVL (E elem) {
        int comp = elem.compareTo(data);
        if (comp < 0) {
            NodeBT node = mkBalancedNodeBT(data, left.deleteAVL(elem), right);
            return node;
        }
        else if (comp > 0) {
            NodeBT node = mkBalancedNodeBT(data, left, right.deleteAVL(elem));
            return node;
        }
        else {
            try {
                TreeAndLeaf<E> treeLeaf = left.extractRightMost();
                NodeBT node = mkBalancedNodeBT(treeLeaf.leaf(), treeLeaf.tree(), right);
                return node;
            }
            catch (EmptyTreeE e) { return right; }
        }
    }

    // Printable interface

    public TreePrinter.PrintableNode getLeft() { return left.isEmpty() ? null : left; }
    public TreePrinter.PrintableNode getRight() { return right.isEmpty() ? null : right; }
    public String getText() { return String.valueOf(data); }

}

