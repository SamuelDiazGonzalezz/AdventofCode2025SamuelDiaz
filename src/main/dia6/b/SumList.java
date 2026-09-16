package software.ulpgc.day06.b;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SumList implements OperatorList {
    final private List<Long> sumList;

    public static SumList create() {
        return new SumList();
    }

    private SumList() {
        this.sumList = new ArrayList<>();
    }

    @Override
    public int size() {
        return sumList.size();
    }

    @Override
    public SumList add(long number) {
        sumList.add(number);
        return this;
    }

    @Override
    public SumList addAll(Long[] numbers) {
        sumList.addAll(Arrays.asList(numbers));
        return this;
    }

    @Override
    public SumList addAll(List<Long> numbers) {
        sumList.addAll(numbers);
        return this;
    }

    @Override
    public long compute() {
        return sumList.stream().mapToLong(Long::longValue).sum();
    }
}
