package other;

import AoC.Day10;

import java.util.Objects;

public class Vector {
    public final int x, y;

    public Vector(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Vector minus() {
        return new Vector(-x, -y);
    }

    public Vector add(Vector other) {
        return new Vector(x + other.x, y + other.y);
    }

    public Vector minus(Vector other) {
        return add(other.minus());
    }

    public double dotProduct(Vector other) {
        return x * other.x + y * other.y;
    }

    public double norm() {
        return Math.sqrt(dotProduct(this));
    }

    public double getAngle() {
        double theta = Math.atan2(x, -y);
        return ((theta > 0 ? theta : (2 * Math.PI + theta)) * 360 / (2 * Math.PI)) % 360;
    }

    public double cosineSimilarity(Vector other) {
        return dotProduct(other) / (norm() * other.norm());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector vector = (Vector) o;
        return x == vector.x &&
                y == vector.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
