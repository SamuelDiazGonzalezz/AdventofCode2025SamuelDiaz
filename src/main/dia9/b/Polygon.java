package dia9.b;

import java.util.List;
import java.util.stream.IntStream;

public class Polygon {
    private final List<Position> vertices;

    private Polygon(List<Position> vertices) {
        this.vertices = vertices;
    }

    public static Polygon create(List<Position> positionList) {
        return new Polygon(simplify(positionList));
    }

    public boolean contains(Rectangle rectangle) {
        return rectangle.corners().stream().allMatch(this::contains)
                && !anyEdgeCutsThrough(rectangle);
    }

    private boolean anyEdgeCutsThrough(Rectangle rectangle) {
        return indices().anyMatch(i -> edgeCutsThrough(i, rectangle));
    }

    private boolean edgeCutsThrough(int index, Rectangle rectangle) {
        return edgeAt(index)
                .map(edge -> edge.cutsThrough(rectangle))
                .orElse(false);
    }

    public boolean contains(Position point) {
        return isOnBoundary(point) || indices()
                .filter(i -> crossesEdge(point, i))
                .count() % 2 == 1;
    }

    private boolean isOnBoundary(Position point) {
        return indices().anyMatch(i -> isOnEdge(point, i));
    }

    private boolean isOnEdge(Position point, int index) {
        return edgeAt(index)
                .map(edge -> edge.contains(point))
                .orElse(false);
    }

    private boolean crossesEdge(Position point, int index) {
        return edgeAt(index)
                .map(edge -> edge.rayCrosses(point))
                .orElse(false);
    }

    private java.util.Optional<Edge> edgeAt(int index) {
        return java.util.Optional.of(
                new Edge(vertices.get(index), vertices.get((index + 1) % vertices.size()))
        );
    }

    private IntStream indices() {
        return IntStream.range(0, vertices.size());
    }

    private static List<Position> simplify(List<Position> positions) {
        return positions.size() <= 3 ? positions :
                IntStream.range(0, positions.size())
                        .filter(i -> crossProduct(positions, i) != 0)
                        .mapToObj(positions::get)
                        .toList();
    }

    private static long crossProduct(List<Position> positions, int index) {
        return calculateCrossProduct(
                positions.get(Math.floorMod(index - 1, positions.size())),
                positions.get(index),
                positions.get((index + 1) % positions.size())
        );
    }

    private static long calculateCrossProduct(Position p1, Position p2, Position p3) {
        return (long) (p2.x() - p1.x()) * (p3.y() - p2.y()) -
                (long) (p2.y() - p1.y()) * (p3.x() - p2.x());
    }


}
