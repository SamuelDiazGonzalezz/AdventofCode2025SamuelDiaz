package software.ulpgc.day05.b;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collector;

public class InventoryManagement {
    private final List<Range> ranges;
    private final static String EMPTY_LINE_REGEX = "(?m)^\\s*$\\R+";

    private InventoryManagement() {
        this.ranges = new ArrayList<>();
    }

    public static InventoryManagement create() {
        return new InventoryManagement();
    }

    public InventoryManagement addRange(long start, long end) {
        ranges.add(new Range(start, end));
        return this;
    }

    public InventoryManagement addRange(String range) {
        this.addRange(getRangeValue(range, 0), getRangeValue(range, 1));
        return this;
    }

    public InventoryManagement parse(String input) {
        ranges.addAll(input.split(EMPTY_LINE_REGEX)[0].lines()
                .map(l -> l.split("-"))
                .map(p -> new Range(
                        Long.parseLong(p[0]),
                        Long.parseLong(p[1])
                ))
                .sorted(Comparator.comparingLong(Range::start))
                .collect(mergingRanges()));

        return this;
    }

    private boolean overlaps(Range a, Range b) {
        return b.start() <= a.end() + 1;
    }

    private Range merge(Range a, Range b) {
        return new Range(a.start(), Math.max(a.end(), b.end()));
    }

    private Collector<Range, List<Range>, List<Range>> mergingRanges() {
        return Collector.of(
                ArrayList::new,
                (acc, r) -> {
                    if ((acc.isEmpty() || !overlaps(acc.getLast(), r))) {
                        acc.add(r);
                    } else {
                        acc.set(
                                acc.size() - 1,
                                merge(acc.getLast(), r)
                        );
                    }
                },
                (a, b) -> { a.addAll(b); return a; }
        );
    }

    private long getRangeValue(String range, int x) {
        return parseNumber(range.split("-")[x]);
    }

    private long parseNumber(String str) {
        return Long.parseLong(str);
    }

    public long count () {
        return this.ranges.stream()
                .mapToLong(Range::count)
                .sum();
    }
}
