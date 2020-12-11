package misc;

import java.util.Objects;

public class KVPair<K, V> {
    private final K key;
    private final V value;

    public KVPair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K key() {
        return key;
    }

    public V value() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (KVPair) obj;
        return Objects.equals(this.key, that.key) &&
                Objects.equals(this.value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, value);
    }

    @Override
    public String toString() {
        return "KVPair[" +
                "key=" + key + ", " +
                "value=" + value + ']';
    }
    
    public static class Pair extends KVPair<Integer, Integer> {
        public Pair(Integer key, Integer value) {
            super(key, value);
        }
        
        public Pair add(KVPair<Integer, Integer> other) {
            return new Pair(key() + other.key(), value() + other.value());
        }
    }
}
