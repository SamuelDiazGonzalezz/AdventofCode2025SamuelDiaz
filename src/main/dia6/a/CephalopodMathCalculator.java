package software.ulpgc.day06.a;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class CephalopodMathCalculator {
    final private List<ProductList> productLists;
    final private SumList sumList;

    public static CephalopodMathCalculator create() {
        return new CephalopodMathCalculator();
    }

    private CephalopodMathCalculator() {
        this.productLists = new ArrayList<>();
        this.sumList = SumList.create();
    }

    public CephalopodMathCalculator parse(String input) {
         return parse(input.split("\n"));
    }

    private CephalopodMathCalculator parse(String[] split) {
        return parse(Arrays.stream(split).map(s -> s.trim().split("\\s+")).toList());
    }

    private CephalopodMathCalculator parse(List<String[]> problems) {
        return parse(IntStream.range(0, problems.getFirst().length)
                .mapToObj(i ->
                        problems
                                .stream()
                                .map(row -> row[i])
                                .toArray(String[]::new)
                )
        );
    }

    private CephalopodMathCalculator parse(Stream<String[]> problems) {
        problems.forEach(row -> {
            if (row[row.length - 1].equals("*")) {
                productLists.add(ProductList.create().addAll(toLongList(row)));
            } else {
                sumList.addAll(toLongList(row));
            }
        });
        return this;
    }

    private List<Long> toLongList(String[] row) {
        return Arrays
                .asList(row)
                .subList(0, row.length-1)
                .stream()
                .mapToLong(Long::parseLong)
                .boxed()
                .toList();
    }

    public long compute() {
        return sumList.compute() + productLists.stream().map(ProductList::compute).reduce(0L, Long::sum);
    }
}
