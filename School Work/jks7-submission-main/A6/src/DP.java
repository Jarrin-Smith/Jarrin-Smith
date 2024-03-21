import java.math.BigInteger;
import java.util.Arrays;
import java.util.EmptyStackException;
import java.util.Stack;

/**
 * This class contains the recursive solutions for the
 * dynamic programming problems
 *
 * These solutions will only work for small problems as they
 * involve many repeated subcomputations.
 */

public class DP {
    // fib from lecture repeated here as an template
    BigInteger fib (int n) {
        if (n < 2) return BigInteger.valueOf(n);
        else return fib(n-1).add(fib(n-2));
    }

    /**
     * Take a stack of numbers, a desired sum 'sum', and
     * return T/F depending on whether it is possible to use some
     * of the numbers in the stack to produce the desired sum.
     */
    boolean subsetSum (Stack<Integer> s, int sum) {
        Stack<Integer> s2 = (Stack<Integer>) s.clone();
        if (sum == 0) {
            return true;
        }
        else if (s2.size() == 0) {
            return false;
        }
        else {
            int pop = s2.pop();
            return subsetSum(s2, sum - pop) || subsetSum(s2, sum);
        }
    }
}
