import lombok.Data;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
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
        List<Integer> factoryDeck = IntStream.range(0, 10).boxed().collect(Collectors.toList());
        List<Integer> shuffled = shuffleStrategy.apply(factoryDeck);
        System.out.println(shuffled);
    }

    private static void partOne() throws IOException {
        CardShuffle shuffleStrategy = Files.lines(Paths.get("AoC/resources/day22.txt")).map(line -> {
            String[] s = line.split(" ");
            if (s.length == 2) return new CutN(Integer.parseInt(s[1]));
            if (Character.isDigit(s[3].charAt(0))) return new IncrementN(Integer.parseInt(s[3]));
            else return new DealNewStack();
        }).reduce(Combined::new).get();
        List<Integer> factoryDeck = IntStream.range(0, 10007).boxed().collect(Collectors.toList());
        List<Integer> shuffled = shuffleStrategy.apply(factoryDeck);
        System.out.println(shuffled.indexOf(2019));
    }

    private static void partTwo() throws IOException {
        CardShuffle shuffleStrategy = Files.lines(Paths.get("AoC/resources/day22-test.txt")).map(line -> {
            String[] s = line.split(" ");
            if (s.length == 2) return new CutN(Integer.parseInt(s[1]));
            if (Character.isDigit(s[3].charAt(0))) return new IncrementN(Integer.parseInt(s[3]));
            else return new DealNewStack();
        }).reduce(Combined::new).get();
//        System.out.println(((Combined)shuffleStrategy).first.apply(IntStream.range(0, 10).boxed().collect(Collectors.toList())));
//        System.out.println(shuffleStrategy.apply(IntStream.range(0, 10).boxed().collect(Collectors.toList())));
//        System.out.println(shuffleStrategy.apply(BigInteger.TWO, new BigInteger("10")));
//        System.out.println(shuffleStrategy.inverseApply(new BigInteger("1"), new BigInteger("10")));
        
        var M = new BigInteger("119315717514047");
        var N = new BigInteger("101741582076661");
        var X = new BigInteger("2020");
        var Y = shuffleStrategy.inverseApply(X, M);
        var Z = shuffleStrategy.inverseApply(Y, M);
        
        var A = Y.subtract(Z).multiply(X.subtract(Y).modInverse(M)).mod(M);
        var B = Y.subtract(A.multiply(X)).mod(M);
        
        var P = A.modPow(N, M);
        var PX = P.multiply(X);
        var P_1 = P.subtract(BigInteger.ONE);
        var Ainv = A.subtract(BigInteger.ONE).modInverse(M);
        System.out.println(PX.add(P_1.multiply(Ainv).multiply(B)).mod(M));
    }
}

abstract class CardShuffle implements Function<List, List> {
    public abstract BigInteger inverseApply(BigInteger a, BigInteger M);
    public abstract BigInteger apply(BigInteger x, BigInteger M);
}

class DealNewStack extends CardShuffle {
    @Override
    public BigInteger inverseApply(BigInteger a, BigInteger M) {
        return M.subtract(a).subtract(BigInteger.ONE).mod(M);
    }

    @Override
    public BigInteger apply(BigInteger x, BigInteger M) {
        return M.subtract(BigInteger.ONE).subtract(x).mod(M);
    }

    @Override
    public List apply(List list) {
        List t = new ArrayList(list);
        Collections.reverse(t);
        return t;
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

    @Override
    public List apply(List list) {
        int n = this.n < 0 ? list.size() + this.n : this.n;
        ArrayList t = new ArrayList<>(list.subList(0, n));
        t.addAll(list.subList(n, list.size()));
        return t;
    }
}

@Data
class IncrementN extends CardShuffle {
    private final Integer n;

    @Override
    public BigInteger inverseApply(BigInteger a, BigInteger M) {
        return new BigInteger(n.toString()).modInverse(M).multiply(a).mod(M);
    }

    @Override
    public BigInteger apply(BigInteger x, BigInteger M) {
        return x.multiply(new BigInteger(n.toString())).mod(M);
    }

    @Override
    public List apply(List list) {
        List t = new ArrayList(Collections.nCopies(list.size(), -1));
        int i = 0;
        for (Object o : list) {
            t.set(i, o);
            i = (i + n) % list.size();
        }
        return t;
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

    @Override
    public List apply(List list) {
        return second.apply(first.apply(list));
    }
}