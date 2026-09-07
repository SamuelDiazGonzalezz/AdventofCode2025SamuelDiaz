package software.ulpgc.day03.b;

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
                        .mapToInt(Integer::parseInt)
                        .boxed()
                        .collect(Collectors.toList())))
                .collect(Collectors.toList())
        );
    }

    public BatteryMaximizer add(List<BatteryBank> batteryBankList) {
        batteries.addAll(batteryBankList);
        return this;
    }

    public long sum() {
        return batteries.stream().mapToLong(BatteryBank::sum).reduce(0, Long::sum);
    }
}
