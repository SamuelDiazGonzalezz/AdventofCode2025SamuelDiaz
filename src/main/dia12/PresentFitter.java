package dia12;

import java.util.*;
import java.util.stream.*;

public record PresentFitter(List<Shape> shapes, List<Region> regions) {

    public static PresentFitter create() {
        return new PresentFitter(List.of(), List.of());
    }

    public PresentFitter parse(String input) {
        return parseLines(Arrays.stream(input.split("\n")).toList());
    }

    private PresentFitter parseLines(List<String> lines) {
        List<String> cleaned = lines.stream()
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();

        int splitIndex = IntStream.range(0, cleaned.size())
                .filter(i -> isRegionLine(cleaned.get(i)))
                .findFirst()
                .orElse(cleaned.size());

        List<Shape> parsedShapes = parseShapes(cleaned.subList(0, splitIndex));
        List<Region> parsedRegions = parseRegions(cleaned.subList(splitIndex, cleaned.size()), parsedShapes);

        return new PresentFitter(parsedShapes, parsedRegions);
    }

    private boolean isRegionLine(String line) {
        return line.matches("\\d+x\\d+:.*");
    }

    private List<Shape> parseShapes(List<String> lines) {
        return IntStream.range(0, lines.size())
                .filter(i -> lines.get(i).matches("\\d+:"))
                .mapToObj(i -> extractShape(lines, i))
                .toList();
    }

    private Shape extractShape(List<String> lines, int startIdx) {
        return Shape.create(
                IntStream.range(startIdx + 1, lines.size())
                        .takeWhile(i -> !lines.get(i).matches("\\d+:") && !isRegionLine(lines.get(i)))
                        .mapToObj(lines::get)
                        .toList()
        );
    }

    private List<Region> parseRegions(List<String> lines, List<Shape> parsedShapes) {
        return lines.stream()
                .filter(this::isRegionLine)
                .map(line -> parseRegion(line, parsedShapes))
                .toList();
    }

    private Region parseRegion(String line, List<Shape> parsedShapes) {
        String[] parts = line.split(":\\s*");
        String[] dimensions = parts[0].split("x");
        int[] counts = Arrays.stream(parts[1].trim().split("\\s+"))
                .mapToInt(Integer::parseInt)
                .toArray();

        List<Shape> presents = IntStream.range(0, counts.length)
                .boxed()
                .flatMap(i -> IntStream.range(0, counts[i])
                        .mapToObj(_ -> parsedShapes.get(i)))
                .toList();

        return new Region(
                Integer.parseInt(dimensions[0]),
                Integer.parseInt(dimensions[1]),
                presents
        );
    }

    public long fittableRegions() {
        return regions.stream()
                .filter(this::canFitPresents)
                .count();
    }

    private boolean canFitPresents(Region region) {
        if (region.presentsArea() > region.area()) return false;

        int regionSize = region.width() * region.height();

        if (regionSize <= 64) {
            Map<Shape, List<Long>> cache = new IdentityHashMap<>();
            List<List<Long>> placements = region.presents().stream()
                    .map(s -> cache.computeIfAbsent(s, sh -> precomputePlacementsLong(sh, region)))
                    .toList();
            List<List<Long>> ordered = new ArrayList<>(placements);
            ordered.sort(Comparator.comparingInt(List::size));
            return tryFitLong(0L, ordered, 0, new HashMap<>());
        }

        Map<Shape, List<BitSet>> cache = new IdentityHashMap<>();
        List<List<BitSet>> allPlacements = region.presents().stream()
                .map(s -> cache.computeIfAbsent(s, sh -> precomputePlacements(sh, region)))
                .toList();
        List<List<BitSet>> ordered = new ArrayList<>(allPlacements);
        ordered.sort(Comparator.comparingInt(List::size));

        return tryFit(new BitSet(), ordered, 0, new HashMap<>());
    }

    private List<BitSet> precomputePlacements(Shape shape, Region region) {
        return shape.allOrientations().stream()
                .flatMap(variant -> IntStream.rangeClosed(0, region.height() - variant.height())
                        .boxed()
                        .flatMap(y -> IntStream.rangeClosed(0, region.width() - variant.width())
                                .mapToObj(x -> variant.toBitmask(x, y, region.width()))))
                .distinct()
                .toList();
    }

        private List<Long> precomputePlacementsLong(Shape shape, Region region) {
        return shape.allOrientations().stream()
            .flatMap(variant -> IntStream.rangeClosed(0, region.height() - variant.height())
                .boxed()
                .flatMap(y -> IntStream.rangeClosed(0, region.width() - variant.width())
                    .mapToObj(x -> variant.toLongMask(x, y, region.width()))))
            .distinct()
            .toList();
        }

    private boolean tryFit(BitSet occupied, List<List<BitSet>> placements, int index, Map<String, Boolean> memo) {
        if (index >= placements.size()) return true;

        String key = index + ":" + Base64.getEncoder().encodeToString(occupied.toByteArray());
        Boolean cached = memo.get(key);
        if (cached != null) return cached;

        boolean result = placements.get(index).stream()
                .filter(mask -> !mask.intersects(occupied))
                .anyMatch(mask -> {
                    BitSet next = (BitSet) occupied.clone();
                    next.or(mask);
                    return tryFit(next, placements, index + 1, memo);
                });

        memo.put(key, result);
        return result;
    }

    private boolean tryFitLong(long occupied, List<List<Long>> placements, int index, Map<Long, Boolean> memo) {
        if (index >= placements.size()) return true;

        long key = occupied ^ ((long) index << 56);
        Boolean cached = memo.get(key);
        if (cached != null) return cached;

        boolean result = placements.get(index).stream()
                .anyMatch(mask -> (occupied & mask) == 0L && tryFitLong(occupied | mask, placements, index + 1, memo));

        memo.put(key, result);
        return result;
    }
}