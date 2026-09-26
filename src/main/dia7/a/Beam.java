package dia7.a;

import java.util.Collection;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Beam {
    private final int row;
    private final int maxCol;
    private final PositionList positionList;
    private long splitCount = 0;

    private Beam(int row, int maxCol, long splitCount) {
        this.positionList = new PositionList();
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
        return this;
    }

    public Beam next(String nextLine) {
        return createBeam(IntStream.range(0, nextLine.length())
                .filter(i -> positionList.positionExists(this.row, i)), nextLine
        );
    }

    public Beam setSplit(long splitCount) {
        this.splitCount = splitCount;
        return this;
    }

    private Beam createBeam(IntStream beamPoints, String nextLine) {
        return Beam.create(this.row+1, this.maxCol).addAll(
                beamPoints
                        .mapToObj(i -> nextLine.charAt(i) == '^' ?
                                divideBy(i) :
                                List.of(new Position(this.row+1, i))

                )
                        .flatMap(Collection::stream)
                        .toList()
        ).setSplit(splitCount);
    }

    private List<Position> divideBy(int i) {
        splitCount++;
        return Stream.of(new Position(this.row+1, i+1), new Position(this.row+1, i-1))
                .toList()
        ;
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
