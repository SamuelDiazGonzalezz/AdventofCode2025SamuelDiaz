package dia10.b;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Machine {
    private static final Pattern BUTTON_PATTERN = Pattern.compile("\\((.*?)\\)");
    private static final Pattern JOLTAGE_PATTERN = Pattern.compile("\\{(.*?)}");

    private final Set<Button> buttons;
    private final List<Integer> joltageRequirementsList;

    private Machine() {
        buttons = new HashSet<>();
        joltageRequirementsList = new ArrayList<>();
    }

    public static Machine create(String input) {
        return new Machine().parse(input);
    }
    
    private Machine parse(String input) {
        return parseButton(input).parseJoltage(input);
    }

    private Machine parseJoltage(String input) {
        joltageRequirementsList.addAll(getJoltageStream(parseResult(JOLTAGE_PATTERN.matcher(input)))
                .mapToInt(Integer::parseInt)
                .boxed()
                .toList());
        return this;
    }

    private Stream<String> getJoltageStream(String LIGHTS_PATTERN) {
        return Arrays.stream(LIGHTS_PATTERN.split(","));
    }

    private Machine parseButton(String input) {
        return parseButton(BUTTON_PATTERN.matcher(input));
    }

    private Machine parseButton(Matcher matcher) {
        while (matcher.find()) {
            buttons.add(createButton(
                    Arrays.stream(matcher.group(1).split(","))
                            .filter(s -> !s.isBlank())
                            .mapToInt(Integer::parseInt)
                            .boxed()
                            .collect(Collectors.toSet())
            ));
        }
        return this;
    }

    private Button createButton(Set<Integer> lights) {
        return new Button(lights);
    }

    private String parseResult(Matcher m) {
        if (!m.find()) throw new IllegalArgumentException("Invalid input");
        return m.group(1);
    }

    public int desiredStateSteps() {
        Map<List<Integer>, Map<List<Integer>, Integer>> parityMaps =
                buildParityMaps(joltageRequirementsList.size());

        return (int) findMinimumPresses(joltageRequirementsList, parityMaps, new HashMap<>());
    }

    private Map<List<Integer>, Map<List<Integer>, Integer>> buildParityMaps(int size) {
        int numButtons = buttons.size();
        List<Button> buttonList = new ArrayList<>(buttons);

        return IntStream.range(0, 1 << numButtons)
                .boxed()
                .map(mask -> computePattern(mask, buttonList, size))
                .collect(Collectors.groupingBy(
                        PatternInfo::parity,
                        Collectors.toMap(
                                PatternInfo::result,
                                PatternInfo::cost,
                                Math::min
                        )
                ));
    }

    private PatternInfo computePattern(int mask, List<Button> buttonList, int size) {
        List<Integer> result = new ArrayList<>(Collections.nCopies(size, 0));
        int cost = 0;

        for (int j = 0; j < buttonList.size(); j++) {
            if ((mask & (1 << j)) != 0) {
                cost++;
                buttonList.get(j).joltageLevers()
                        .forEach(idx -> result.set(idx, result.get(idx) + 1));
            }
        }

        List<Integer> parity = result.stream()
                .map(v -> v % 2)
                .collect(Collectors.toList());

        return new PatternInfo(result, parity, cost);
    }

    private long findMinimumPresses(List<Integer> current,
                                    Map<List<Integer>, Map<List<Integer>, Integer>> parityMaps,
                                    Map<List<Integer>, Long> cache) {
        if (cache.containsKey(current)) {
            return cache.get(current);
        }

        if (current.stream().allMatch(v -> v == 0)) {
            return 0;
        }

        if (current.stream().anyMatch(v -> v < 0)) {
            return Long.MAX_VALUE;
        }

        List<Integer> currentParity = current.stream()
                .map(v -> v % 2)
                .collect(Collectors.toList());

        if (!parityMaps.containsKey(currentParity)) {
            return Long.MAX_VALUE;
        }

        long result = parityMaps.get(currentParity).entrySet().stream()
                .filter(entry -> isValidPattern(current, entry.getKey()))
                .mapToLong(entry -> {
                    List<Integer> reduced = reduceState(current, entry.getKey());
                    long subCost = findMinimumPresses(reduced, parityMaps, cache);
                    return subCost == Long.MAX_VALUE ?
                            Long.MAX_VALUE :
                            entry.getValue() + 2L * subCost;
                })
                .min()
                .orElse(Long.MAX_VALUE);

        cache.put(current, result);
        return result;
    }

    private boolean isValidPattern(List<Integer> current, List<Integer> pattern) {
        return IntStream.range(0, current.size())
                .allMatch(i -> pattern.get(i) <= current.get(i));
    }

    private List<Integer> reduceState(List<Integer> current, List<Integer> pattern) {
        return IntStream.range(0, current.size())
                .mapToObj(i -> (current.get(i) - pattern.get(i)) / 2)
                .collect(Collectors.toList());
    }
}
