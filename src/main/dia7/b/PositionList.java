package dia7.b;

import java.util.HashSet;

public class PositionList extends HashSet<Position> {

    public boolean positionExists(int row, int col) {
        return this.stream().anyMatch(p -> p.row() == row && p.col() == col);
    }
}
