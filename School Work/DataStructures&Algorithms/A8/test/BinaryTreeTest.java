import com.sun.source.tree.Tree;
import org.junit.jupiter.api.Test;
import org.junit.platform.engine.support.hierarchical.Node;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

class BinaryTreeTest {

    @Test
    void insertBST() {
        Collection<Integer> elems;
        BinaryTree<Integer> btree, otree;

        elems = Arrays.asList(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15);
        btree = BinaryTree.mkBalanced(elems);
        assertTrue(btree.isBalanced());
        assertEquals(4, btree.height());

        TreePrinter.print(btree);

        elems = Arrays.asList(1,2,3,4,5);
        btree = BinaryTree.mkBalanced(elems);
        otree = BinaryTree.mkBST(elems);

        TreePrinter.print(btree);
        TreePrinter.print(otree);
    }

    @Test
    void iter () {
        Collection<Integer> elems;
        BinaryTree<Integer> btree;

        elems = Arrays.asList(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15);
        btree = BinaryTree.mkBalanced(elems);

        TreePrinter.print(btree);
        for (Iterator<Integer> iter = btree.preOrder(); iter.hasNext(); )
            System.out.print(iter.next() + " ");
        System.out.println();
        for (Iterator<Integer> iter = btree.inOrder(); iter.hasNext(); )
            System.out.print(iter.next() + " ");
        System.out.println();
        for (Iterator<Integer> iter = btree.postOrder(); iter.hasNext(); )
            System.out.print(iter.next() + " ");
        System.out.println();
        for (Iterator<Integer> iter = btree.breadthFirst(); iter.hasNext(); )
            System.out.print(iter.next() + " ");
    }

    @Test
    void deleteBST () {
        Collection<Integer> elems;
        BinaryTree<Integer> btree, stree;

        elems = Arrays.asList(8,2,6,4,5,7,12,11,9,10,1,14,13,3,15);
        btree = BinaryTree.mkBST(elems);
        TreePrinter.print(btree);

        stree = btree.deleteBST(6);
        TreePrinter.print(stree);

        stree = btree.deleteBST(11);
        TreePrinter.print(stree);

        stree = btree.deleteBST(8);
        TreePrinter.print(stree);
    }

    @Test
    void insertAVL () {
        Collection<Integer> elems;
        BinaryTree<Integer> btree;

        elems = Arrays.asList(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15);
        btree = BinaryTree.mkAVL(elems);

        TreePrinter.print(btree);
    }

    @Test
    void deleteAVL () {
        Collection<Integer> elems;
        BinaryTree<Integer> btree;

        elems = Arrays.asList(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15);
        btree = BinaryTree.mkAVL(elems);

        TreePrinter.print(btree);

        btree = btree.deleteAVL(1);
        TreePrinter.print(btree);

        btree = btree.deleteAVL(3);
        TreePrinter.print(btree);

        btree = btree.deleteAVL(2);
        TreePrinter.print(btree);

        btree = btree.deleteAVL(7);
        TreePrinter.print(btree);

        btree = btree.deleteAVL(4);
        TreePrinter.print(btree);

        btree = btree.deleteAVL(6);
        TreePrinter.print(btree);

        btree = btree.deleteAVL(13);
        TreePrinter.print(btree);

        btree = btree.deleteAVL(15);
        TreePrinter.print(btree);

    }

    @Test
    void todoTest() throws EmptyTreeE {
        
        // Insert and delete use all the functions we had to create so these test should hopefully satisfy the testing requirements

        NodeBT tester = new NodeBT(1, new NodeBT(2, new EmptyBT(), new EmptyBT()), new NodeBT(3, new EmptyBT(), new EmptyBT()));

        // the original tree
        System.out.println("---- BEFORE INSERTION ----");
        TreePrinter.print(tester);

        System.out.println("---- AFTER INSERTION ----");
        BinaryTree tree = tester.insertAVL(17);
        tree = tree.insertAVL(5);
        TreePrinter.print(tree);

        tree = tree.insertAVL(6);
        TreePrinter.print(tree);

        System.out.println("---- DELETING WHAT WE INSERTED ----");
        tree = tree.deleteAVL(6);
        TreePrinter.print(tree);

        tree = tree.deleteAVL(5);
        TreePrinter.print(tree);

        // This returns our original tree after deleting everything
        tree = tree.deleteAVL(17);
        TreePrinter.print(tree);

    }
}