package software.ulpgc.day04.b;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class PaperRoll {
    private final List<PaperRoll> adjacentPaperRolls;
    private final Position position;

    public PaperRoll(Position position) {
        this.adjacentPaperRolls = new ArrayList<>();
        this.position = position;
    }

    public int col() {
        return position.col();
    }
    public int row() {
        return position.row();
    }

    public int size() {
        return adjacentPaperRolls.size();
    }

    public PaperRoll add(PaperRoll paperRoll) {
        adjacentPaperRolls.add(paperRoll);
        return this;
    }

    public PaperRoll clear() {
        adjacentPaperRolls.clear();
        return this;
    }

    public PaperRoll remove(Object o) {
        adjacentPaperRolls.remove(o);
        return this;
    }

    public PaperRoll addAll(Collection<? extends PaperRoll> c) {
        adjacentPaperRolls.addAll(c);
        return this;
    }

    public PaperRoll addAll(int index, Collection<? extends PaperRoll> c) {
        adjacentPaperRolls.addAll(index, c);
        return this;
    }

    public PaperRoll removeAll(Collection<?> c) {
        adjacentPaperRolls.removeAll(c);
        return this;
    }

    public PaperRoll forEach(Consumer<? super PaperRoll> action) {
        adjacentPaperRolls.forEach(action);
        return this;
    }

    public Stream<PaperRoll> stream() {
        return adjacentPaperRolls.stream();
    }

    public Stream<PaperRoll> parallelStream() {
        return adjacentPaperRolls.parallelStream();
    }
}
