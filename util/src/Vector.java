import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Vector {
    public final int size;
    private final List<Integer> elements;

    public Vector(Integer... elements) {
        size = elements.length;
        this.elements = new ArrayList<>(List.of(elements));
    }

    private Vector(List<Integer> elements) {
        size = elements.size();
        this.elements = new ArrayList<>(elements);
    }

    public int get(int i) {
        return elements.get(i);
    }

    public Vector minus() {
        return new Vector(elements.stream().map(x -> -x).collect(Collectors.toList()));
    }

    public Vector add(Vector other) {
        if (size != other.size) throw new IllegalArgumentException("Vectors must be of same size");
        ArrayList<Integer> newVectorElements = new ArrayList<>(size);

        for (int i = 0; i < size; i++) {
            newVectorElements.add(elements.get(i) + other.elements.get(i));
        }

        return new Vector(newVectorElements);
    }

    public Vector minus(Vector other) {
        return add(other.minus());
    }

    public double dotProduct(Vector other) {
        if (size != other.size) throw new IllegalArgumentException("Vectors must be of same size");

        int dotProduct = 0;
        for (int i = 0; i < size; i++) {
            dotProduct += elements.get(i) * other.elements.get(i);
        }

        return dotProduct;
    }

    public double norm() {
        return Math.sqrt(dotProduct(this));
    }

    public Stream<Integer> asStream() {
        return elements.stream();
    }

    public double getAngle(int d1, int d2) {
        int x = elements.get(d1), y = elements.get(d2);
        double theta = Math.atan2(x, -y);
        return ((theta > 0 ? theta : (2 * Math.PI + theta)) * 360 / (2 * Math.PI)) % 360;
    }

    public double cosineSimilarity(Vector other) {
        return dotProduct(other) / (norm() * other.norm());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vector)) return false;
        Vector vector = (Vector) o;
        return size == vector.size &&
                elements.equals(vector.elements);
    }

    @Override
    public int hashCode() {
        return Objects.hash(size, elements);
    }

    @Override
    public String toString() {
        return elements.toString();
    }
}
