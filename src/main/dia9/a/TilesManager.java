package dia9.a;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class TilesManager {
    private final List<Position> positionList;
    private final List<Rectangle> rectangleList;

    private TilesManager() {
        positionList = new ArrayList<>();
        rectangleList = new ArrayList<>();
    }

    public static TilesManager create() {
        return new TilesManager();
    }

    public TilesManager parse(String input) {
        return parse(input.split("\n"));
    }

    private TilesManager parse(String[] split) {
        return parse(Arrays.stream(split).map(s -> s.trim().split(",")));
    }

    private TilesManager parse(Stream<String[]> positionStream) {
        positionList.addAll(positionStream
                .map(strings -> Arrays.stream(strings).mapToInt(Integer::parseInt).toArray())
                .map(ints -> new Position(ints[0], ints[1]))
                .toList()
        );
        return this;
    }

    public TilesManager rectangles() {
        rectangleList.addAll(IntStream.range(0, positionList.size())
                .boxed()
                .flatMap(i ->
                        IntStream.range(i + 1, positionList.size())
                                .mapToObj(j -> new Rectangle(positionList.get(i), positionList.get(j)))
                )
                .sorted(Comparator.comparingLong(Rectangle::area).reversed())
                .toList());

        return this;
    }

    public long largestArea() {
        return rectangleList.getFirst().area();
    }
}
