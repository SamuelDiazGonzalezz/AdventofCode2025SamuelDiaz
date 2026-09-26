package dia8.b;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CircuitSet {
    private final List<Set<Position>> circuits;

    CircuitSet(List<Position> junctionBoxes) {
        circuits = new ArrayList<>();
        junctionBoxes.forEach(junctionBox -> {
            Set<Position> circuitItem = new HashSet<>();
            circuitItem.add(junctionBox);
            circuits.add(circuitItem);
        });
    }

    public void add(JunctionBoxPair junctionBoxPair) {
        int i1 = circuits.indexOf(circuits
                .stream()
                .filter(junctionBox -> junctionBox.contains(junctionBoxPair.junctionBox1()))
                .findFirst()
                .orElse(null));
        int i2 = circuits.indexOf(circuits
                .stream()
                .filter(junctionBox -> junctionBox.contains(junctionBoxPair.junctionBox2()))
                .findFirst()
                .orElse(null));

        if (i1 != i2) {
            circuits.get(i1).addAll(circuits.get(i2));
            circuits.remove(i2);
        }
    }

    public int size() {
        return circuits.size();
    }

    public List<Set<Position>> getThreeLargest() {
        return circuits
                .stream()
                .sorted((c1, c2) -> Integer.compare(c2.size(), c1.size()))
                .limit(3)
                .toList();
    }
}
