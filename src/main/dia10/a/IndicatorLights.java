package dia10.a;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class IndicatorLights {
    private final List<Light> lightList;
    private final List<Light> desiredState;

    private IndicatorLights() {
        lightList = new ArrayList<>();
        desiredState = new ArrayList<>();
    }

    public static IndicatorLights create() {
        return new IndicatorLights();
    }

    public IndicatorLights parse(String input) {
        return parse(input.split(""));
    }

    private IndicatorLights parse(String[] split) {
        desiredState.addAll(Arrays.stream(split)
                .map(str -> str.equals(".") ?
                        new Light() :
                        new Light(true)
                )
                .toList());
        return parse(split.length);
    }

    public IndicatorLights parse(int lights) {
        return parse(IntStream.range(0, lights)
                .mapToObj(_ -> new Light())
                .toList());
    }

    private IndicatorLights parse(List<Light> list) {
        lightList.addAll(list);
        return this;
    }

    public LightState[] getLightsState() {
        return lightList
                .stream()
                .map(Light::state)
                .toArray(LightState[]::new);
    }

    public List<Light>  getDesiredState() {
        return desiredState;
    }
}
