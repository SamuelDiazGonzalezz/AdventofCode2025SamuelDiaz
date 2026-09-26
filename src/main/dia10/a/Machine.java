package dia10.a;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Machine {
    private static final Pattern LIGHTS_PATTERN = Pattern.compile("\\[(.*?)]");
    private static final Pattern BUTTON_PATTERN = Pattern.compile("\\((.*?)\\)");

    private final IndicatorLights indicatorLights;
    private final Set<Button> buttons;

    private Machine() {
        indicatorLights = IndicatorLights.create();
        buttons = new HashSet<>();
    }

    public static Machine create(String input) {
        return new Machine().parse(input);
    }
    
    private Machine parse(String input) {
        return parseLights(input).parseButton(input);
    }

    private Machine parseLights(String input) {
        indicatorLights.parse(parseResult(LIGHTS_PATTERN.matcher(input)));
        return this;
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
        Set<Integer> targetState = getTargetLightIndices();
        Set<Integer> initialState = getCurrentLightIndices();

        return initialState.equals(targetState) ? 0 :
                bfsMinSteps(
                        Set.of(initialState),
                        List.of(initialState),
                        targetState,
                        0
                );
    }

    private Set<Integer> getTargetLightIndices() {
        return IntStream.range(0, indicatorLights.getDesiredState().size())
                .filter(i -> indicatorLights.getDesiredState().get(i).state() == LightState.On)
                .boxed()
                .collect(Collectors.toSet());
    }

    private Set<Integer> getCurrentLightIndices() {
        return IntStream.range(0, indicatorLights.getLightsState().length)
                .filter(i -> indicatorLights.getLightsState()[i] == LightState.On)
                .boxed()
                .collect(Collectors.toSet());
    }

    private int bfsMinSteps(Set<Set<Integer>> visited, List<Set<Integer>> frontier, Set<Integer> target, int depth) {
        return frontier.isEmpty() ? -1 :
                frontier.contains(target) ? depth :
                        bfsMinSteps(
                                mergeVisitedAndFrontier(visited, frontier),
                                generateNextFrontier(visited, frontier),
                                target,
                                depth + 1
                        );
    }

    private Set<Set<Integer>> mergeVisitedAndFrontier(Set<Set<Integer>> visited, List<Set<Integer>> frontier) {
        return Stream.concat(visited.stream(), frontier.stream())
                .collect(Collectors.toSet());
    }

    private List<Set<Integer>> generateNextFrontier(Set<Set<Integer>> visited, List<Set<Integer>> frontier) {
        return frontier.stream()
                .flatMap(state -> buttons.stream()
                        .map(button -> applyButton(state, button.lights())))
                .filter(state -> !visited.contains(state))
                .distinct()
                .toList();
    }

    private Set<Integer> applyButton(Set<Integer> currentState, Set<Integer> buttonLights) {
        return Stream.concat(
                currentState.stream().filter(light -> !buttonLights.contains(light)),
                buttonLights.stream().filter(light -> !currentState.contains(light))
        ).collect(Collectors.toSet());
    }
}
