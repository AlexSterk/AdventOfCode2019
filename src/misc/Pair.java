package misc;

public class Pair extends KVPair<Integer, Integer> {
    public Pair(Integer x, Integer y) {
        super(x, y);
    }

    public Pair(String x, String y) {
        super(Integer.parseInt(x), Integer.parseInt(y));
    }

    public Pair add(KVPair<Integer, Integer> other) {
        return new Pair(key() + other.key(), value() + other.value());
    }
    
    public Pair absolute() {
        return new Pair(Math.abs(key()), Math.abs(value()));
    }
    
    public Pair rotateClockWise() {
        return new Pair(-value(), key());
    }

    public Pair rotateCounterClockWise() {
        return new Pair(value(), -key());
    }

    @Override
    public String toString() {
        return "(%d, %d)".formatted(key(), value());
    }
}
