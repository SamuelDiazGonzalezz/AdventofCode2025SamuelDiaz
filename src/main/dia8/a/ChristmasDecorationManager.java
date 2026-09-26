package dia8.a;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class ChristmasDecorationManager {
    private final int MAX_JUNCTION_CONNECT_COUNT;
    private final List<Position> boxList;
    private List<JunctionBoxPair> pairList;
    private CircuitSet circuitSet;

    private ChristmasDecorationManager(int maxConnectJunction) {
        this.boxList = new ArrayList<>();
        this.pairList = new ArrayList<>();
        this.MAX_JUNCTION_CONNECT_COUNT = maxConnectJunction;
    }

    public static ChristmasDecorationManager create(int maxConnectJunction) {
        return new ChristmasDecorationManager(maxConnectJunction);
    }

    public ChristmasDecorationManager add(Position box) {
        this.boxList.add(box);
        return this;
    }

    public ChristmasDecorationManager addAll(List<Position> boxList) {
        this.boxList.addAll(boxList);
        return this;
    }

    public ChristmasDecorationManager parse(String input) {
        return parse(Arrays.stream(input.split("\n")));
    }

    public ChristmasDecorationManager parse(Stream<String> input) {
        return parse(input
                .map(s -> toIntList(s.trim().split(",")))
                .map(iL -> new Position(iL.getFirst(), iL.get(1), iL.getLast())
                ).toList()
        );
    }

    public ChristmasDecorationManager parse(List<Position> input) {
        this.boxList.addAll(input);
        return this;
    }

    private List<Integer> toIntList(String[] split) {
        return Arrays.stream(split).mapToInt(Integer::parseInt).boxed().toList();
    }

    public ChristmasDecorationManager startConnections() {
        circuitSet = computeJunctionPairs();
        IntStream.range(0, this.MAX_JUNCTION_CONNECT_COUNT)
                .mapToObj(i -> pairList.get(i))
                .forEach(circuitSet::add);

        return this;
    }

    private CircuitSet computeJunctionPairs() {
        for (int i = 0; i < boxList.size(); i++) {
            for (int j = i + 1; j < boxList.size(); j++) {
                Position box1 = boxList.get(i);
                Position box2 = boxList.get(j);
                pairList.add(new JunctionBoxPair(box1, box2, box1.distanceTo(box2)));
            }
        }
        pairList.sort(JunctionBoxPair::compareTo);
        return new CircuitSet(boxList);
    }

    public long calculate() {
        return circuitSet
                .getThreeLargest()
                .stream()
                .mapToLong(Set::size)
                .reduce(1, (a, b) -> a * b);
    }
}
