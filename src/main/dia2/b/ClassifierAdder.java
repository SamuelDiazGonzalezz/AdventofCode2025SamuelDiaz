package software.ulpgc.day02.b;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ClassifierAdder {
    private final List<Classifier> classifierList;
    private ClassifierAdder() { this.classifierList = new ArrayList<>(); }
    public static ClassifierAdder create() { return new ClassifierAdder(); }

    public ClassifierAdder compile(String rangesString) {
        return compile(rangesString.split(","));
    }

    public ClassifierAdder compile(String... idRanges) {
        Arrays.stream(idRanges)
                .map(String::trim)
                .map(this::parse)
                .forEach(this::add);
        return this;
    }

    private void add(long[] ids) {
        classifierList.add(Classifier.create().from(ids[0]).to(ids[1]).calculate());
    }

    private long[] parse(String range) {
        return parse(range.split("-"));
    }

    private long[] parse(String[] split) {
        return Arrays.stream(split).mapToLong(Long::parseLong).toArray();
    }

    public long sum() {
        return classifierList.stream().mapToLong(Classifier::sum).sum();
    }
}
