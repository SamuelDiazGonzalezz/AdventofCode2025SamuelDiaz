package dia8.b;

import java.util.Objects;

public record JunctionBoxPair(Position junctionBox1, Position junctionBox2, double distance) implements Comparable<JunctionBoxPair> {
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        JunctionBoxPair that = (JunctionBoxPair) o;
        return distance == that.distance && junctionBox1.equals(that.junctionBox1) && junctionBox2.equals(that.junctionBox2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(junctionBox1, junctionBox2, distance);
    }

    @Override
    public int compareTo(JunctionBoxPair o) {
        return Double.compare(distance, o.distance);
    }
}
