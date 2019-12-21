import lombok.Data;

@Data
class Tuple2<V, U> {
    public final V first;
    public final U second;
}

@Data
class Tuple3<V, U, T> {
    public final V first;
    public final U second;
    public final T third;
}

@Data
class Tuple4<A, B, C, D> {
    public final A first;
    public final B second;
    public final C third;
    public final D fourth;
}
