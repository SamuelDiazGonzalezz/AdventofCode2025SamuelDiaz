package dia12;

import java.util.BitSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public record Shape(Set<Position> cells) {

    public static Shape create(List<String> lines) {
        return new Shape(parseCells(lines));
    }

    private static Set<Position> parseCells(List<String> lines) {
        return IntStream.range(0, lines.size())
                .boxed()
                .flatMap(y -> IntStream.range(0, lines.get(y).length())
                        .filter(x -> lines.get(y).charAt(x) == '#')
                        .mapToObj(x -> new Position(x, y)))
                .collect(Collectors.toSet());
    }

    public int width() {
        int max = -1;
        for (Position p : cells) {
            if (p.x() > max) max = p.x();
        }
        return max + 1;
    }

    public int height() {
        int max = -1;
        for (Position p : cells) {
            if (p.y() > max) max = p.y();
        }
        return max + 1;
    }

    public int area() {
        return cells.size();
    }

    public Set<Shape> allOrientations() {
        return Stream.iterate(this, Shape::rotate90)
                .limit(4)
                .flatMap(s -> Stream.of(s, s.flipHorizontal()))
                .map(Shape::normalized)
                .collect(Collectors.toSet());
    }

    private Shape rotate90() {
        return new Shape(cells.stream()
                .map(p -> new Position(-p.y(), p.x()))
                .collect(Collectors.toSet()));
    }

    private Shape flipHorizontal() {
        return new Shape(cells.stream()
                .map(p -> new Position(-p.x(), p.y()))
                .collect(Collectors.toSet()));
    }

    private Shape normalized() {
        int minX = cells.stream().mapToInt(Position::x).min().orElse(0);
        int minY = cells.stream().mapToInt(Position::y).min().orElse(0);
        return new Shape(cells.stream()
                .map(p -> new Position(p.x() - minX, p.y() - minY))
                .collect(Collectors.toSet()));
    }

    public BitSet toBitmask(int offsetX, int offsetY, int regionWidth) {
        BitSet mask = new BitSet(regionWidth * (offsetY + height()));
        for (Position p : cells) {
            int idx = (p.y() + offsetY) * regionWidth + (p.x() + offsetX);
            mask.set(idx);
        }
        return mask;
    }

    public long toLongMask(int offsetX, int offsetY, int regionWidth) {
        return cells.stream()
                .mapToLong(p -> 1L << ((long) (p.y() + offsetY) * regionWidth + (p.x() + offsetX)))
                .reduce(0L, (a, b) -> a | b);
    }
}