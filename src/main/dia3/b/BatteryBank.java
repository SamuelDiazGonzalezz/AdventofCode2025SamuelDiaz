package software.ulpgc.day03.b;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

public record BatteryBank(List<Integer> batteries)  {

    private final static int MAX_COMBINATION_LENGTH = 12;

    public long sum() {
        // Convertir List<Integer> a List<Long> antes de procesarlo
        List<Long> longBatteries = batteries.stream()
                .map(Integer::longValue)
                .toList();
        return joinChars(maxCombination(longBatteries, MAX_COMBINATION_LENGTH));
    }

    private static List<Long> maxCombination(List<Long> list, int k) {
        return k == 0 || list.isEmpty()
                ? List.of()
                : IntStream.range(0, list.size() - k + 1)
                .reduce((i, j) -> list.get(i) >= list.get(j) ? i : j)
                .stream()
                .mapToObj(i -> concat(
                        list.get(i),
                        maxCombination(list.subList(i + 1, list.size()), k - 1)
                ))
                .findFirst()
                .orElse(List.of());
    }

    private static List<Long> concat(Long head, List<Long> tail) {
        return LongStream.concat(
                LongStream.of(head),
                tail.stream().mapToLong(Long::longValue)
        ).boxed().toList();
    }

    private long joinChars(List<Long> longElements) {
        return Long.parseLong(longElements.stream()
                .map(String::valueOf)
                .collect(Collectors.joining()));
    }
}