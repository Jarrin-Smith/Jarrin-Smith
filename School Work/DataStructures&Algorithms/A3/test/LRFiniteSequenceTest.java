import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class LRFiniteSequenceTest {
    private LRFiniteSequence<Integer> seq;
    private LRFiniteSequence<Integer> seq1;
    private LRFiniteSequence<Integer> seq2;
    private LRFiniteSequence<Integer> iterSeq;
    private LRFiniteSequence<Integer> seq3;

    @BeforeEach
    void setUp() {
        seq = new LRFiniteSequence<>(5);
        seq1 = new LRFiniteSequence<>(5);
        seq2 = new LRFiniteSequence<>(5);
        iterSeq = new LRFiniteSequence<>(5);
        seq3 = new LRFiniteSequence<>(5);
    }

    @Test
    void simpleLeft() throws SeqAccessE {
        assertEquals(0, seq.size());

        seq.insertLeft(10);
        seq.insertLeft(20);
        seq.insertLeft(30);
        seq.insertLeft(40);
        seq.insertLeft(50);
        assertEquals(5, seq.size());

        assertEquals(50, seq.removeLeft());
        assertEquals(40, seq.removeLeft());
        assertEquals(30, seq.removeLeft());
        assertEquals(2, seq.size());

        assertEquals(20, seq.get(1));
        assertEquals(10, seq.get(0));

        assertDoesNotThrow(SeqAccessE::new);

        assertThrows(SeqAccessE.class, () -> seq.get(4));
        assertThrows(SeqAccessE.class, () -> seq.get(100));
    }

    @Test
    void simpleRight() throws SeqAccessE {
        assertEquals(0, seq.size());

        seq.insertRight(10);
        seq.insertRight(20);
        seq.insertRight(30);
        seq.insertRight(40);
        seq.insertRight(50);
        assertEquals(5, seq.size());

        assertEquals(50, seq.removeRight());
        assertEquals(40, seq.removeRight());
        assertEquals(30, seq.removeRight());
        assertEquals(2, seq.size());

        assertEquals(20, seq.get(3));
        assertEquals(10, seq.get(4));

        assertDoesNotThrow(SeqAccessE::new);

        assertThrows(SeqAccessE.class, () -> seq.get(0));
        assertThrows(SeqAccessE.class, () -> seq.get(100));
    }

    @Test
    void simpleLeftRight() throws SeqAccessE {
        seq.insertLeft(10);
        seq.insertRight(20);
        seq.insertLeft(30);
        seq.insertRight(40);
        seq.insertLeft(50);

        assertEquals(10, seq.get(0));
        assertEquals(30, seq.get(1));
        assertEquals(50, seq.get(2));
        assertEquals(40, seq.get(3));
        assertEquals(20, seq.get(4));

        assertEquals(50, seq.removeLeft());
        assertEquals(40, seq.removeRight());
        assertEquals(3, seq.size());

    }

    @Test
    public void stringComp () {
        seq.insertRight(10);
        seq.insertRight(20);
        seq.insertRight(30);
        seq.insertLeft(40);
        seq.insertLeft(50);

        assertEquals(seq.toString(),
                "[Optional[40], Optional[50], Optional[30], Optional[20], Optional[10]]");
    }

    @Test
    void stringComp2() {
        seq = new LRFiniteSequence<>(2);
        seq.insertRight(12);
        seq.insertLeft(10); // 10 12
        seq.insertLeft(15); // 12 10 15 .
        seq.insertRight(7); // 12 10 15 7
        seq.insertLeft(3);
        seq.insertLeft(9);
        seq.insertRight(5);

        assertEquals(seq.toString(),
                "[Optional[7], Optional[12], Optional[10], Optional[15], Optional[3], Optional[9], Optional.empty, Optional[5]]");
    }

    @Test
    public void noResize() throws SeqAccessE {
        assertEquals(0, seq.size());
        seq.insertRight(1);
        seq.insertRight(2);
        seq.insertRight(3);
        assertEquals(3, seq.removeRight());
        assertEquals(1, seq.removeLeft());
        assertEquals(2, seq.removeLeft());
        assertEquals(0, seq.size());
        seq.insertLeft(1);
        seq.insertLeft(2);
        seq.insertRight(3);
        seq.insertLeft(4);
        seq.insertRight(5);
        assertEquals(5, seq.removeRight());
        assertEquals(3, seq.removeRight());
        assertEquals(1, seq.removeRight());
        assertEquals(2, seq.removeRight());
        assertEquals(4, seq.removeRight());
    }

    @Test
    public void resize() throws SeqAccessE {
        seq.insertLeft(1);
        assertEquals(seq.toString(),
                "[Optional[1], Optional.empty, Optional.empty, Optional.empty, Optional.empty]");

        seq.insertLeft(2);
        assertEquals(seq.toString(),
                "[Optional[1], Optional[2], Optional.empty, Optional.empty, Optional.empty]");

        seq.insertRight(3);
        assertEquals(seq.toString(),
                "[Optional[1], Optional[2], Optional.empty, Optional.empty, Optional[3]]");

        seq.insertLeft(4);
        assertEquals(seq.toString(),
                "[Optional[1], Optional[2], Optional[4], Optional.empty, Optional[3]]");

        seq.insertRight(5);
        assertEquals(seq.toString(),
                "[Optional[1], Optional[2], Optional[4], Optional[5], Optional[3]]");

        seq.insertRight(6);
        assertEquals(seq.toString(),
                "[Optional[5], Optional[3], Optional[1], Optional[2], Optional[4], Optional.empty, Optional.empty, Optional.empty, Optional.empty, Optional[6]]");

        assertEquals(10, seq.getCapacity());
        assertEquals(6, seq.removeRight());
        assertEquals(5, seq.removeRight());
        assertEquals(3, seq.removeRight());
        assertEquals(1, seq.removeRight());
        assertEquals(2, seq.removeRight());
        assertEquals(4, seq.removeRight());
    }

    @Test
    void testClear(){
        seq1.insertLeft((1));
        seq1.insertLeft(2);
        seq1.insertRight(3);
        seq1.insertLeft(4);
        seq1.insertRight(5);

        assertEquals(5, seq1.getCapacity());

        // After resize
        seq1.insertRight(6);
        assertEquals(10, seq1.getCapacity());

        seq1.clear();

        assertEquals(16, seq1.getCapacity());
        assertEquals(seq1.toString(), "[Optional.empty, Optional.empty, Optional.empty, Optional.empty, Optional.empty, Optional.empty, Optional.empty, Optional.empty, Optional.empty, Optional.empty, Optional.empty, Optional.empty, Optional.empty, Optional.empty, Optional.empty, Optional.empty]");
    }

    @Test
    void testContains() throws SeqAccessE {
        seq2.insertLeft(1);
        seq2.insertRight(2);
        seq2.insertLeft(3);
        seq2.insertLeft(4);
        seq2.insertRight(5);

        assertEquals(true, seq2.contains(2));
        assertEquals(false, seq2.contains(6));

        seq2.insertRight(6);
        assertEquals(true, seq2.contains(6));

        seq2.removeRight();
        assertEquals(false, seq2.contains(6));
    }

    @Test
    void testIterator() throws SeqAccessE {
        iterSeq.insertRight(1);
        iterSeq.insertRight(2);
        iterSeq.insertRight(3);
        iterSeq.insertRight(4);
        iterSeq.insertRight(5);

        Iterator iter = iterSeq.iterator();
        assertEquals(true, iter.hasNext());
        assertEquals(5, iter.next());

        iter.next();
        iter.next();
        assertEquals(2, iter.next());

        iter.next();
        assertThrows(NoSuchElementException.class, () -> iter.next());

        iterSeq.removeRight();
        iterSeq.removeRight();
        iterSeq.removeRight();
        iterSeq.removeRight();
        iterSeq.removeRight();

        Iterator iter2 = iterSeq.iterator();
        assertThrows(NoSuchElementException.class, () -> iter2.next());
    }

    @Test
    void testIterator2() throws SeqAccessE {
        iterSeq.insertLeft(1);
        iterSeq.insertLeft(2);
        iterSeq.insertLeft(3);
        iterSeq.insertLeft(4);
        iterSeq.insertLeft(5);

        Iterator iter = iterSeq.descendingIterator();
        assertEquals(true, iter.hasNext());
        assertEquals(5, iter.next());

        iter.next();
        iter.next();
        assertEquals(2, iter.next());

        iter.next();
        assertThrows(NoSuchElementException.class, () -> iter.next());

        iterSeq.removeRight();
        iterSeq.removeRight();
        iterSeq.removeRight();
        iterSeq.removeRight();
        iterSeq.removeRight();

        Iterator iter2 = iterSeq.descendingIterator();
        assertThrows(NoSuchElementException.class, () -> iter2.next());
    }

    /**
     * This also test the contains all method
     */
    @Test
    void testInsertLeftAll(){
        ArrayList<Integer> numbers = new ArrayList<>();
        numbers.add(1);
        numbers.add(2);
        numbers.add(3);
        numbers.add(4);
        numbers.add(5);

        assertEquals(0, seq3.size());

        seq3.insertLeftAll(numbers);
        assertEquals(5, seq3.size());

        assertEquals("[Optional[1], Optional[2], Optional[3], Optional[4], Optional[5]]", seq3.toString());

        assertEquals(true, seq3.containsAll(numbers));

        numbers.add(6);
        assertEquals(false, seq3.containsAll(numbers));
    }

}