import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class LRFiniteSequenceTest {
    private LRFiniteSequence<Integer> seq;
    private LRFiniteSequence<String> seq2;
    private LRFiniteSequence<Double> seq3;
    @BeforeEach
    void setUp() {
        seq = new LRFiniteSequence<>(5);
        seq2 = new LRFiniteSequence<>(5);
        seq3 = new LRFiniteSequence<>(5);
    }

    @Test
    void simpleMap() {
        seq.insertRight(1);
        seq.insertRight(2);
        SmallStream<Integer> res = seq.map(n -> n * 10);
        assertEquals(seq.size(), res.size());
        Iterator<Integer> seqIter = seq.iterator();
        Iterator<Integer> resIter = res.iterator();
        while (seqIter.hasNext() && resIter.hasNext()) {
            assertEquals(seqIter.next() * 10, resIter.next());
        }
    }

    @Test
    void emptyContains() throws SeqAccessE {
        assertFalse(seq.contains(1));
        seq.insertRight(1);
        assertTrue(seq.contains(1));
        assertFalse(seq.contains(100));
        seq.clear();
        seq.insertRight(100);
        seq.insertRight(200);
        seq.insertRight(300);
        seq.insertLeft(400);
        Collection<Integer> c = Arrays.asList(1, 2, 3, 4, 5, 6, 7);
        seq.insertLeftAll(c);
        assertTrue(seq.containsAll(c));
        assertTrue(seq.contains(1));
        assertTrue(seq.contains(2));
        assertTrue(seq.contains(3));
        assertTrue(seq.contains(4));
        assertTrue(seq.contains(5));
        assertTrue(seq.contains(6));
        assertTrue(seq.contains(7));
        assertTrue(seq.contains(100));
        assertTrue(seq.contains(200));
        assertTrue(seq.contains(300));
        assertTrue(seq.contains(400));
        assertEquals(7, seq.peekLeft());
        seq.removeLeft();
        assertFalse(seq.containsAll(c));
    }

    @Test
    void iterFull() throws SeqAccessE {
        seq.insertLeft(1);
        seq.insertLeft(2);
        seq.insertLeft(3);
        seq.insertLeft(4);
        seq.insertLeft(5);
        int iterCount = 0;
        for (int i : seq) {
            iterCount++;
        }
        assertEquals(5, iterCount);
        seq.removeRight();
        Optional<Integer>[] arr = seq.toArray();
        assertEquals(5, arr.length);
        int left = seq.getLeft();
        int right = seq.getRight();
        assertEquals(0, left);
        assertEquals(0, right);
        assertEquals(Optional.empty(), arr[0]);
        assertEquals(Optional.of(2), arr[1]);
        assertEquals(Optional.of(3), arr[2]);
        assertEquals(Optional.of(4), arr[3]);
        assertEquals(Optional.of(5), arr[4]);
        seq.clear();
        assertEquals(0, seq.size());
        assertTrue(seq.isEmpty());
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

        assertEquals(50, seq.peekLeft());
        assertEquals(10, seq.peekRight());
        assertEquals(50, seq.removeLeft());
        assertEquals(40, seq.peekLeft());
        assertEquals(10, seq.peekRight());
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
    public void stringComp() {
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
    void reduce() {
        seq.insertLeftAll(Arrays.asList(1, 2, 3, 4, 5));
        assertEquals(120, seq.reduceR(1, (r, e) -> r * e));
        seq.insertLeftAll(Arrays.asList(6, 7, 8, 9, 10));
        assertEquals(55, seq.reduceR(0, Integer::sum));
        SmallStream<Integer> ss = seq.filter(n -> n % 2 == 0);
        assertEquals(5, ss.size());
        assertTrue(ss.allMatch(n -> n % 2 == 0));
        assertFalse(ss.allMatch(n -> n > 5));
        assertTrue(ss.anyMatch(n -> n > 5));
        assertFalse(ss.anyMatch(n -> n > 1000));
        assertEquals(Optional.of(2), ss.min(Integer::compare));
        seq.clear();
        assertEquals(Optional.empty(), seq.findAny());
        seq.insertRight(77);
        assertEquals(Optional.of(77), seq.findAny());
    }


    @Test
    void streams() {
        seq.insertLeft(1);
        seq.insertLeft(2);
        seq.insertLeft(3);
        seq.insertLeft(4);
        seq.insertLeft(5);
        System.out.println(seq.toString());
        SmallStream<Integer> s1 = seq.map(n -> n * n);
        System.out.println(seq.toString());

        int sum0 = seq.reduceR(0, (r, n) -> r + n);
        int sum1 = s1.reduceR(0, Integer::sum);
        System.out.printf("sum0 = %d; sum1 = %d%n", sum0, sum1);

        System.out.println(seq.map(n -> n % 2 == 0).toString());
        System.out.println(seq.reduceR(new LRFiniteSequence<>(10),
                (st, e) -> {
                    st.insertRight(e);
                    st.insertRight(e);
                    return st;
                }));
    }

    @Test
    void streams2() {
        // square every number
        Stream<Integer> is0 = Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        Stream<Integer> is1 = is0.map(n -> n * n);
        System.out.println(Arrays.toString(is1.toArray()));

        // length of every string
        Stream<String> ss0 = Stream.of("ab", "xyz", "123456", "");
        Stream<Integer> ss1 = ss0.map(String::length);
        System.out.println(Arrays.toString(ss1.toArray()));

        // repeat every element three times
        Stream<Integer> is2 = Stream.of(4, 7);
        Stream<Integer> is3 = is2.flatMap(i -> Stream.of(i, i, i));
        System.out.println(Arrays.toString(is3.toArray()));

        // all evens
        Stream<Integer> is4 = Stream.of(4, 6, 100, 0, -2, 2);
        Stream<Integer> is5 = Stream.of(4, 1, 100, 0, -2, 2);
        Predicate<Integer> isEven = n -> n % 2 == 0;
        boolean b1 = is4.allMatch(isEven);
        boolean b2 = is5.allMatch(isEven);
        System.out.println(b1);
        System.out.println(b2);

        // product of all numbers
        Stream<Integer> is6 = Stream.of(1, 2, 3, 4, 5);
        int n = is6.reduce(1, (r, i) -> r * i);
        System.out.println(n);

        // checksum
        Stream<Integer> is7 = Stream.of(10, 13, 14, 6, 7, 23, 14, 1, 18, 19);
        int cs = is7.reduce(0, (r, i) -> (r + i) % 10);
        System.out.println(cs);

        // max
        Stream<Integer> is8 = Stream.of(10, 13, 14, 6, 7, 23, 14, 1, 18, 19);
        int mx = is8.max(Integer::compare).get();
        System.out.println(mx);
    }
    @Test
    void streamOfStrings(){
        seq2.insertRight("Bob");
        seq2.insertRight("Joe");
        seq2.insertLeft("Gilbert");
        seq2.insertLeft("Jeff");
        seq2.insertRight("Bobby");
        // The length of all these names is 22

        SmallStream<String> ss = seq2.map(n -> n + " is my name!");
        System.out.println(ss);

        int sum = seq2.reduceR(0, (i,n) -> i += n.length());
        assertEquals(22, sum);

        System.out.println(seq2.map(n -> n = "BWAAAAAA"));

        System.out.println(seq2.map(n -> n.length() > 3));

        SmallStream<Double> doubles = seq2.map(n -> n.length() + 0.5);
        System.out.println(doubles);
    }

    @Test
    void streamOfFLoats(){
        seq3.insertRight(0.4);
        seq3.insertRight(0.45);
        seq3.insertRight(0.456);
        seq3.insertRight(0.411);
        seq3.insertRight(0.2);

        SmallStream<Double> ss = seq3.map(n -> n * 3);
        System.out.println(ss);

        double mult = seq3.reduceR(1.0, (i,n) -> n * i);
        assertEquals(0.006746976000000001, mult);

        System.out.println(seq3.map(n -> n < 4));

        System.out.println(seq3.reduceR(new LRFiniteSequence<>(10),
                (st, e) -> {
                    st.insertRight(e + 0.5);
                    st.insertRight(e + 0.1);
                    return st;
                }));

        SmallStream<Boolean> boo = seq3.map(n -> n > 3);
        System.out.println(boo);
    }

}