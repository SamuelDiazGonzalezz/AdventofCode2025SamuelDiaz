package software.ulpgc.day02.a;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.LongStream;

public class Classifier {
    private final List<ID> invalidIDs;
    private long from;
    private long to;

    private Classifier() { invalidIDs = new ArrayList<>(); }
    public static Classifier create() {
        return new Classifier();
    }

    public Classifier from(long from) {
        this.from = from;
        return this;
    }

    public Classifier to(long to) {
        this.to = to+1;
        return this;
    }

    public Classifier calculate() {
        LongStream.range(from, to).forEach(this::add);
        return this;
    }

    private void add(long number) {
        ID id = new ID(Long.toString(number));
        if (id.isValid()) return;
        invalidIDs.add(id);
    }

    public long sum() {
        return invalidIDs.stream()
                .mapToLong(ID::longNumber)
                .sum();
    }
}
