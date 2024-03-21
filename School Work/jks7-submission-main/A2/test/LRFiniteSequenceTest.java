import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class LRFiniteSequenceTest {
    private LRFiniteSequence<Integer> seq;
    private LRFiniteSequence<Integer> seq2;
    private LRFiniteSequence<Integer> roundSeq;
    private LRFiniteSequence<Integer> additionSeq;

    @BeforeEach
    void setUp() {
        seq = new LRFiniteSequence<>(5);
        seq2 = new LRFiniteSequence<>(2);
        roundSeq = new LRFiniteSequence<>(2, n -> Math.round(1.5f*n));
        additionSeq = new LRFiniteSequence<>(2, n -> n+1);
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
        seq.insertLeft(10);
        seq.insertLeft(15);
        seq.insertRight(7);
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
    void testingDefaultGrowth(){
        seq2.insertRight(2);
        seq2.insertLeft(3);
        seq2.insertLeft(4);

        // resize gets called
        assertEquals(4, seq2.getCapacity());

        seq2.insertRight(7);
        seq2.insertLeft(8);

        // resize gets called
        assertEquals(8, seq2.getCapacity());

    }

    @Test
    void testingDifferentGrowth(){

        // Testing with Math.round
        roundSeq.insertLeft(2);
        roundSeq.insertRight(3);
        roundSeq.insertLeft(5);
        roundSeq.insertLeft(3);

        assertEquals(5, roundSeq.getCapacity());

        // Testing growth strategy with addition
        additionSeq.insertLeft(2);
        additionSeq.insertRight(3);
        additionSeq.insertRight(6);

        assertEquals(3, additionSeq.getCapacity());
    }

    // This is my time test
    Duration timeSeq (Random r, int bound, LRFiniteSequence<Integer> seq) {
        Instant start = Instant.now();

        for (int i=0; i<seq.getCapacity(); i++) {
            int e = r.nextInt(bound);
            switch (r.nextInt(4)) {
                case 0 -> seq.insertLeft(e);
                case 1 -> seq.insertRight(e);
            }
        }

        Instant end = Instant.now();
        return Duration.between(start, end);
    }

    @Test
    void timeTest () {
        Random r = new Random(1234567L);
        int bound = 1000;

        Duration d;

        d = timeSeq(r, bound, new LRFiniteSequence<>(10000));
        assertTrue((d.toMillis() < 10));

        d = timeSeq(r, bound, new LRFiniteSequence<>(100000));
        assertTrue((d.toMillis() < 20));

        d = timeSeq(r, bound, new LRFiniteSequence<>(1000000));
        assertTrue((d.toMillis() < 100));

        d = timeSeq(r, bound, new LRFiniteSequence<>(10000000));
        assertTrue((d.toMillis() < 1000));
    }
}