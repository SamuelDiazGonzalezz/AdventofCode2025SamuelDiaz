package software.ulpgc.day06.b;

import java.util.List;

public interface OperatorList {
    public OperatorList add(long number);
    public OperatorList addAll(Long[] numbers);
    public OperatorList addAll(List<Long> numbers);
    public long compute();
    public int size();
}
