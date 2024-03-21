import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.Random;
import java.util.Stack;

import static org.junit.jupiter.api.Assertions.*;

class DPTest {
    void time (DP dpRef, int n) {
        Duration d;
        Instant start, end;

        start = Instant.now();
        System.out.printf("fib(%2d) = %-20s\t\t", n, dpRef.fib(n));
        end = Instant.now();
        d = Duration.between(start,end);
        System.out.printf("time (ms) = %d%n", d.toMillis());
    }

    @Test
    void slowFib () {
        DP dpRef = new DP();
        System.out.println("---");
        for (int i=1; i<=11; i++) time(dpRef, 4*i);
        System.out.println("---");
    }

    @Test
    void hashFib () {
        DP dpRef = new DPHM();
        System.out.println("---");
        for (int i=1; i<=11; i++) time(dpRef, 4*i);
        System.out.println("---");
    }

    @Test
    void bigFib () {
        DP dpRef = new DPHM();
        assertEquals("6161314747715278029583501626149", dpRef.fib(149).toString());
        assertEquals("222232244629420445529739893461909967206666939096499764990979600",
                dpRef.fib(300).toString());
    }

    @Test
    void subsetSum () {
        Stack<Integer> s = new Stack<>();
        s.addAll(Arrays.asList(5,3,7,1));

        DP dpRef = new DP();
        assertFalse(dpRef.subsetSum(s,2));
        assertTrue(dpRef.subsetSum(s,6));
        assertTrue(dpRef.subsetSum(s,8));

        dpRef = new DPHM();
        assertFalse(dpRef.subsetSum(s,2));
        assertTrue(dpRef.subsetSum(s,6));
        assertTrue(dpRef.subsetSum(s,8));
    }

    @Test
    void subsetSumT () {
        DP dpRef = new DPHM();
        Stack<Integer> s = new Stack<>();

        s.addAll(new Random().ints(50, -10, 10).boxed().toList());
        System.out.printf("50 random elements between -10 and 10: %b%n", dpRef.subsetSum(s,0));

        s.clear();
        s.addAll(new Random().ints(100, -10, 10).boxed().toList());
        System.out.printf("100 random elements between -10 and 10: %b%n", dpRef.subsetSum(s,0));

        s.clear();
        s.addAll(new Random().ints(200, -10, 10).boxed().toList());
        System.out.printf("200 random elements between -10 and 10: %b%n", dpRef.subsetSum(s,0));

        s.clear();
        s.addAll(new Random().ints(400, -10, 10).boxed().toList());
        System.out.printf("400 random elements between -10 and 10: %b%n", dpRef.subsetSum(s,0));
    }
    @Test
    void testFunctions(){
        DP dpRef = new DP();
        DPHM dpRef2 = new DPHM();
        int sum = 0;
        Stack<Integer> s = new Stack<>();
        for (int i = 1; i < 11; i++) {
            s.push(i);
            sum += i;
            System.out.println(sum);
        }

        System.out.println("--Testing DP Method--");
        assertFalse(dpRef.subsetSum(s, 56));
        assertTrue(dpRef.subsetSum(s, 21));
        assertTrue(dpRef.subsetSum(s, 36));
        assertTrue(dpRef.subsetSum(s, 15));
        assertFalse(dpRef.subsetSum(s, 60));

        // should have the same outcome as above test
        System.out.println("--Testing DPHM Method--");
        assertFalse(dpRef2.subsetSum(s, 56));
        assertTrue(dpRef2.subsetSum(s, 21));
        assertTrue(dpRef2.subsetSum(s, 36));
        assertTrue(dpRef2.subsetSum(s, 15));
        assertFalse(dpRef2.subsetSum(s, 60));
    }
    @Test
    void subsetSumR(){
        DP dpRef = new DP();
        DPHM dpRef2 = new DPHM();
        Stack<Integer> s = new Stack<>();

        s.addAll(new Random().ints(20, 10, 100).boxed().toList());
        final long startTime = System.currentTimeMillis();
        dpRef.subsetSum(s, 10);
        dpRef.subsetSum(s, 5);
        dpRef.subsetSum(s, 2);
        dpRef.subsetSum(s, 7);
        dpRef.subsetSum(s, 10);
        dpRef.subsetSum(s, 5);
        dpRef.subsetSum(s, 2);
        dpRef.subsetSum(s, 7);
        final long endTime = System.currentTimeMillis();
        System.out.println("Total execution time of DP Method: " + (endTime - startTime));

        final long startTime2 = System.currentTimeMillis();
        dpRef2.subsetSum(s, 10);
        dpRef2.subsetSum(s, 5);
        dpRef2.subsetSum(s, 2);
        dpRef2.subsetSum(s, 7);
        dpRef2.subsetSum(s, 10);
        dpRef2.subsetSum(s, 5);
        dpRef2.subsetSum(s, 2);
        dpRef2.subsetSum(s, 7);
        final long endTime2 = System.currentTimeMillis();
        System.out.println("Total execution time of DPHM Method: " + (endTime2 - startTime2));
    }
}
