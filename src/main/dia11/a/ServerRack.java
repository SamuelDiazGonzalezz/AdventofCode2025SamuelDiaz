package dia11.a;

import java.util.*;
import java.util.stream.Collectors;

public class ServerRack {
    private final Set<Device> deviceList;
    private final Map<Device, Long> mem;

    private ServerRack() {
        deviceList = new HashSet<>();
        mem = new HashMap<>();
    }

    public static ServerRack create() {
        return new ServerRack();
    }

    public ServerRack parse(String input) {
        deviceList.addAll(Arrays.stream(input
                        .split("\n"))
                        .map(String::trim)
                        .filter(l -> !l.isBlank())
                        .map(line -> line.split(":"))
                        .collect(Collectors.toMap(
                                deviceStr -> deviceStr[0].trim(),
                                deviceStr -> Arrays.stream(deviceStr[1].trim().split("\\s+"))
                                        .toList()
                        ))
                .entrySet()
                .stream()
                .map(device -> new Device(device.getKey(), device.getValue()))
                .toList()
        );
        return this;
    }

    public List<String> next(Device device) {
        return device.outputs();
    }

    public Device getDevice(String label) {
        return deviceList.stream().filter(dev -> dev.label().equals(label)).findFirst().orElse(new Device(label, List.of()));
    }

    public long pathsTo(Device from, Device to) {
        if (from.equals(to)) return 1;
        if (mem.containsKey(from)) return mem.get(from);

        long totalSteps = next(from).stream().mapToLong(out -> pathsTo(getDevice(out), to)).sum();
        mem.put(from, totalSteps);
        return totalSteps;
    }

    public long pathsTo(String from, String to) {
        return pathsTo(getDevice(from), getDevice(to));
    }
}
