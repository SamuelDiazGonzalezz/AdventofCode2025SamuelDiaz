package dia8.b;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class ChristmasDecorationManager {
    private final List<Position> boxList;
    private List<JunctionBoxPair> pairList;
    private CircuitSet circuitSet;

    private ChristmasDecorationManager() {
        this.boxList = new ArrayList<>();
        this.pairList = new ArrayList<>();
    }

    public static ChristmasDecorationManager create() {
        return new ChristmasDecorationManager();
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

    public long calculate() {
        circuitSet = computeJunctionPairs();
        for (int i = 0; ; i++) {
            circuitSet.add(pairList.get(i));

            if (circuitSet.size() == 1) {
                return calculateMath(pairList.get(i));
            }
        }
    }

    private long calculateMath(JunctionBoxPair pair) {
        return pair.junctionBox1().x() * pair.junctionBox2().x();
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
}
