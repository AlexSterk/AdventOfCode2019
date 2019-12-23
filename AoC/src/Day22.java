import lombok.Data;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day22 {    
    public static void main(String[] args) throws IOException {
        test();
        partOne();
        partTwo();
    }

    private static void test() throws IOException {
        CardShuffle shuffleStrategy = Files.lines(Paths.get("AoC/resources/day22-test.txt")).map(line -> {
            String[] s = line.split(" ");
            if (s.length == 2) return new CutN(Integer.parseInt(s[1]));
            if (Character.isDigit(s[3].charAt(0))) return new IncrementN(Integer.parseInt(s[3]));
            else return new DealNewStack();
        }).reduce(Combined::new).get();
        System.out.println(shuffleStrategy.apply(IntStream.range(0, 10).boxed().collect(Collectors.toList())));
    }

    private static void partOne() throws IOException {
        CardShuffle shuffleStrategy = Files.lines(Paths.get("AoC/resources/day22.txt")).map(line -> {
            String[] s = line.split(" ");
            if (s.length == 2) return new CutN(Integer.parseInt(s[1]));
            if (Character.isDigit(s[3].charAt(0))) return new IncrementN(Integer.parseInt(s[3]));
            else return new DealNewStack();
        }).reduce(Combined::new).get();
        System.out.println(shuffleStrategy.apply(new BigInteger("2019"), new BigInteger("10007")));
    }

    private static void partTwo() throws IOException {
        CardShuffle shuffleStrategy = Files.lines(Paths.get("AoC/resources/day22.txt")).map(line -> {
            String[] s = line.split(" ");
            if (s.length == 2) return new CutN(Integer.parseInt(s[1]));
            if (Character.isDigit(s[3].charAt(0))) return new IncrementN(Integer.parseInt(s[3]));
            else return new DealNewStack();
        }).reduce(Combined::new).get();
        
        var M = new BigInteger("119315717514047");
        var N = new BigInteger("101741582076661");
        var X = new BigInteger("2020");

       
        var B = shuffleStrategy.inverseApply(BigInteger.ZERO, M);
        var A = shuffleStrategy.inverseApply(BigInteger.ONE, M).subtract(B).mod(M);

        var P = A.modPow(N, M);
        var PX = P.multiply(X);
        var P_1 = P.subtract(BigInteger.ONE);
        var Ainv = A.subtract(BigInteger.ONE).modInverse(M);
        System.out.println(PX.add(P_1.multiply(Ainv).multiply(B)).mod(M));
    }
}

abstract class CardShuffle {
    public abstract BigInteger inverseApply(BigInteger a, BigInteger M);
    public abstract BigInteger apply(BigInteger x, BigInteger M);
    
    public List apply(List items) {
        List t = new ArrayList(Collections.nCopies(items.size(), null));

        for (Integer i = 0; i < items.size(); i++) {
            t.set(apply(new BigInteger(i.toString()), new BigInteger(Integer.toString(items.size()))).intValue(), items.get(i));
        }
        return t;
    }
}

class DealNewStack extends CardShuffle {
    @Override
    public BigInteger inverseApply(BigInteger a, BigInteger M) {
        return M.subtract(a).subtract(BigInteger.ONE);
    }

    @Override
    public BigInteger apply(BigInteger x, BigInteger M) {
        return M.subtract(BigInteger.ONE).subtract(x).mod(M);
    }
}

@Data
class CutN extends CardShuffle {
    private final Integer n;

    @Override
    public BigInteger inverseApply(BigInteger a, BigInteger M) {
        return a.add(new BigInteger(n.toString())).mod(M);
    }

    @Override
    public BigInteger apply(BigInteger x, BigInteger M) {
        return x.subtract(new BigInteger(n.toString())).mod(M);
    }
}

@Data
class IncrementN extends CardShuffle {
    private final Integer n;

    @Override
    public BigInteger inverseApply(BigInteger a, BigInteger M) {
        BigInteger inverse = new BigInteger(n.toString()).modInverse(M);
        return inverse.multiply(a).mod(M);
    }

    @Override
    public BigInteger apply(BigInteger x, BigInteger M) {
        return x.multiply(new BigInteger(n.toString())).mod(M);
    }
}

class Combined extends CardShuffle {
    public final CardShuffle first, second;

    Combined(CardShuffle first, CardShuffle second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public BigInteger inverseApply(BigInteger a, BigInteger M) {
        BigInteger t = second.inverseApply(a, M);
        return first.inverseApply(t, M);
    }

    @Override
    public BigInteger apply(BigInteger x, BigInteger M) {
        BigInteger t = first.apply(x, M);
        return second.apply(t, M);
    }
}