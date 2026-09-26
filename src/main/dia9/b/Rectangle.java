package dia9.b;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public record Rectangle(Position pos1, Position pos2) {

    public long area() {
        return (long) (Math.abs(pos1.x() - pos2.x()) + 1) * (Math.abs((pos1.y() - pos2.y())) + 1);
    }

    public List<Position> corners() {
        return List.of(
                pos1,
                pos2,
                new Position(pos1.x(), pos2.y()),
                new Position(pos2.x(), pos1.y())
        );
    }

    @Override
    public String toString() {
        return "Rectangle{" + "pos1=" + pos1 + ", pos2=" + pos2 + ", area=" + area() + '}';
    }

    public String draw() {
        int minX = Math.min(pos1.x(), pos2.x());
        int maxX = Math.max(pos1.x(), pos2.x());
        int minY = Math.min(pos1.y(), pos2.y());
        int maxY = Math.max(pos1.y(), pos2.y());

        return IntStream.rangeClosed(minY, maxY)
                .mapToObj(y -> IntStream.rangeClosed(minX, maxX)
                        .mapToObj(x -> isBorder(x, y, minX, maxX, minY, maxY) ? "#" : ".")
                        .collect(Collectors.joining()))
                .collect(Collectors.joining("\n"));
    }

    private boolean isBorder(int x, int y, int minX, int maxX, int minY, int maxY) {
        return y == minY || y == maxY || x == minX || x == maxX;
    }

    int minX() {
        return Math.min(pos1.x(), pos2.x());
    }

    int maxX() {
        return Math.max(pos1.x(), pos2.x());
    }

    int minY() {
        return Math.min(pos1.y(), pos2.y());
    }

    int maxY() {
        return Math.max(pos1.y(), pos2.y());
    }
}
