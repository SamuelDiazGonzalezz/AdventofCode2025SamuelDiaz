package software.ulpgc.day03.a;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class BatteryMaximizer {
    private final List<BatteryBank> batteries;

    public static BatteryMaximizer create() {
        return new BatteryMaximizer();
    }

    private BatteryMaximizer() {
        this.batteries = new ArrayList<>();
    }

    public BatteryMaximizer fromString(List<String> batteryBankList) {
        return add(batteryBankList
                .stream()
                .map(String::trim)
                .map(bB -> new BatteryBank(Arrays
                        .stream(bB.split(""))
                        .map(Integer::parseInt)
                        .collect(Collectors.toList())))
                .collect(Collectors.toList())
        );
    }

    public BatteryMaximizer add(List<BatteryBank> batteryBankList) {
        batteries.addAll(batteryBankList);
        return this;
    }

    public int sum() {
        return batteries.stream().mapToInt(BatteryBank::sum).reduce(0, Integer::sum);
    }
}
