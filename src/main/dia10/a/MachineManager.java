package dia10.a;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MachineManager {
    private final List<Machine> machineList;

    private MachineManager() {
        this.machineList = new ArrayList<>();
    }

    public static MachineManager create() {
        return new MachineManager();
    }

    public MachineManager parse(String input) {
        machineList.addAll(Arrays.stream(input.split("\n"))
                .map(Machine::create)
                .toList());
        return this;
    }

    public int result() {
        return machineList.stream().mapToInt(Machine::desiredStateSteps).sum();
    }
}
