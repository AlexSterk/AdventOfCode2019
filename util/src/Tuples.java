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
