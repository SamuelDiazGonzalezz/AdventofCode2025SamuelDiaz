package software.ulpgc.day04.b;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class PrintingDepartment {
    private final PaperRoll[][] paperRolls;
    private final int MAX_ADJACENT_PAPER_ROLLS = 4;
    private int deletedPaperRolls = 0;

    public PrintingDepartment(Position maxPosition) {
        paperRolls = new PaperRoll[maxPosition.col()][maxPosition.row()];
    }
    public static PrintingDepartment create(Position maxPosition) {
        return new PrintingDepartment(maxPosition);
    }

    public void addNode(Position position) {
        paperRolls[position.row()][position.col()] = new PaperRoll(position);
    }

    public PrintingDepartment addRow(int y, String row) {
        IntStream.range(0, row.length())
                .filter(i -> row.charAt(i) == '@')
                .forEach(idx -> addNode(new Position(idx, y)));
        return this;
    }

    public PrintingDepartment addRows(String[] rows) {
        IntStream.range(0, rows.length).forEach(i -> addRow(i, rows[i].trim()));
        return this;
    }

    public PrintingDepartment process() {
        return Stream.iterate(
                this.processOne(),
                dept -> dept.accesibleRollsCount() >= 1,
                dept -> dept.removeAdjacents().processOne()
        ).reduce((first, last) -> last).orElse(this);
    }

    private PrintingDepartment removeAdjacents() {
        getAllRolls()
                .filter(this::isAccessible)
                .forEach(paperRoll -> {
                    paperRolls[paperRoll.row()][paperRoll.col()] = null;
                    deletedPaperRolls++;
                }
        );
        return this;
    }

    private PrintingDepartment processOne() {
        IntStream.range(0, paperRolls.length)
                .forEach(i -> IntStream.range(0, paperRolls[i].length)
                        .filter(j -> paperRolls[i][j] != null)
                        .forEach(j -> paperRolls[i][j].clear().addAll(getAdjacentRolls(new Position(i, j))))
                );
        return this;
    }

    private List<PaperRoll> getAdjacentRolls(Position pos) {
        return IntStream.rangeClosed(pos.row() - 1, pos.row() + 1)
                .boxed()
                .flatMap(i -> IntStream.rangeClosed(pos.col() - 1, pos.col() + 1)
                        .mapToObj(j -> new Position(i, j))
                )
                .filter(neighbor -> isValidNeighbor(neighbor, pos))
                .map(this::getRollAt)
                .filter(Objects::nonNull)
                .toList();
    }

    private boolean isValidNeighbor(Position neighbor, Position current) {
        return !neighbor.equals(current) && isInBounds(neighbor);
    }

    private boolean isInBounds(Position pos) {
        return pos.row() >= 0 && pos.row() < paperRolls.length &&
                pos.col() >= 0 && pos.col() < paperRolls[0].length;
    }

    private PaperRoll getRollAt(Position pos) {
        return paperRolls[pos.row()][pos.col()];
    }

    private Stream<PaperRoll> getAllRolls() {
        return Arrays.stream(paperRolls)
                .flatMap(Arrays::stream)
                .filter(Objects::nonNull);
    }

    private boolean isAccessible(PaperRoll roll) {
        return roll.size() < MAX_ADJACENT_PAPER_ROLLS;
    }

    public long accesibleRollsCount() {
        return getAllRolls()
                .filter(this::isAccessible)
                .count();
    }

    public PrintingDepartment print() {
        for (int y = 0; y < paperRolls[0].length; y++) {
            for (PaperRoll[] paperRoll : paperRolls) {
                PaperRoll roll = paperRoll[y];
                if (roll == null) {
                    System.out.print(".");
                } else {
                    System.out.print(roll.size() < MAX_ADJACENT_PAPER_ROLLS ? "x" : "@");
                }
            }
            System.out.println();
        }
        return this;
    }

    public int removedPaperRolls() {
        return deletedPaperRolls;
    }
}
