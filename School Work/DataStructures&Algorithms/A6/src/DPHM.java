import java.math.BigInteger;
import java.util.Stack;

/**
 * This class overrides the solutions in DP with new methods that
 * manage an appropriate hash map (as implemented in our previous
 * assignment in the class HM).
 *
 * Each method will have its own hash map.
 */

public class DPHM extends DP {
    private HM<Integer,BigInteger> fibHM;
    private HM<Record, Boolean> r;

    DPHM () {
        fibHM = new HM<>(11);
        r = new HM<>(11);
    }

    record key(Stack<Integer> s, int sum){}

    // fib from lecture as a template
    @Override
    BigInteger fib (int n) {
        if (fibHM.containsKey(n)) {
            return fibHM.get(n);
        }
        else {
            BigInteger r = super.fib(n);
            fibHM.put(n,r);
            return r;
        }
    }
    @Override
    boolean subsetSum (Stack<Integer> s, int sum) {
        Record r2 = new key(s, sum);
        if(r.containsKey(r2)){
            return r.get(r2);
        }
        else{
            boolean val = super.subsetSum(s, sum);
            r.put(r2, val);
            return val;
        }
    }
}
