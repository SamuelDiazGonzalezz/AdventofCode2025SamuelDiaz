package dia7.b;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Beam {
    private final int row;
    private final int maxCol;
    private final PositionList positionList;
    private final Map<Position, Long> pathCounts;
    private long splitCount = 0;

    private Beam(int row, int maxCol, long splitCount) {
        this.positionList = new PositionList();
        this.pathCounts = new HashMap<>();
        this.row = row;
        this.maxCol = maxCol;
    }

    public static Beam create(int row, int maxCol) {
        return new Beam(row, maxCol, 0);
    }

    public Beam addAll(List<Position> positionList){
        this.positionList.addAll(positionList
                .stream()
                .filter(p -> p.col() <= maxCol && p.col() >= 0)
                .toList()
        );
        return this;
    }

    public Beam add(Position position){
        if (position.col() > maxCol || position.col() < 0) return this;
        this.positionList.add(position);
        this.pathCounts.put(position, 1L);
        return this;
    }

    public Beam next(String nextLine) {
        return createBeam(IntStream.range(0, nextLine.length())
                .filter(i -> positionList.positionExists(this.row, i)).boxed().toList(), nextLine
        );
    }

    public Beam setSplit(long splitCount) {
        this.splitCount = splitCount;
        return this;
    }

    private Beam createBeam(List<Integer> columnList, String nextLine) {
        return Beam.create(this.row+1, this.maxCol)
                .setPathCounts(columnList.stream()
                        .flatMap(i -> generateNextPositions(i, nextLine))
                        .collect(Collectors.groupingBy(
                                Map.Entry::getKey,
                                Collectors.summingLong(Map.Entry::getValue)
                        )))
                .setSplit(this.splitCount + countNewSplits(columnList, nextLine));
    }

    private Stream<Map.Entry<Position, Long>> generateNextPositions(int col, String nextLine) {
        Position currentPos = new Position(this.row, col);
        long currentPaths = this.pathCounts.getOrDefault(currentPos, 0L);

        return isSplitter(nextLine, col) ?
                createSplitPositions(col, currentPaths) :
                createStraightPosition(col, currentPaths);
    }

    private boolean isSplitter(String line, int col) {
        return line.charAt(col) == '^';
    }

    private Stream<Map.Entry<Position, Long>> createSplitPositions(int col, long paths) {
        return Stream.of(
                Map.entry(new Position(this.row+1, col-1), paths),
                Map.entry(new Position(this.row+1, col+1), paths)
        );
    }

    private Stream<Map.Entry<Position, Long>> createStraightPosition(int col, long paths) {
        return Stream.of(Map.entry(new Position(this.row+1, col), paths));
    }

    private long countNewSplits(List<Integer> columns, String nextLine) {
        return columns.stream()
                .filter(col -> isSplitter(nextLine, col))
                .count();
    }

    private Beam setPathCounts(Map<Position, Long> pathCounts) {
        this.pathCounts.putAll(pathCounts);
        this.positionList.addAll(new ArrayList<>(pathCounts.keySet()));
        return this;
    }

    public long totalPaths() {
        return pathCounts.values().stream()
                .mapToLong(Long::longValue)
                .sum();
    }

    public long count() {
        return positionList.size();
    }

    public long splits() {
        return splitCount;
    }

    @Override
    public String toString() {
        return positionList + ", splits: "+ splitCount;
    }
}
