package misc;

public class Pair extends KVPair<Integer, Integer> {
    public Pair(Integer x, Integer y) {
        super(x, y);
    }

    public Pair add(KVPair<Integer, Integer> other) {
        return new Pair(key() + other.key(), value() + other.value());
    }
    
    public Pair absolute() {
        return new Pair(Math.abs(key()), Math.abs(value()));
    }
}
