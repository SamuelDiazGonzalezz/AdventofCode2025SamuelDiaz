package dia11.b;

import java.util.*;
import java.util.stream.Collectors;

public class ServerRack {
    private final Map<String, Device> deviceMap;
    private final Map<CacheKey, Long> mem;

    private ServerRack() {
        deviceMap = new HashMap<>();
        mem = new HashMap<>();
    }

    public static ServerRack create() {
        return new ServerRack();
    }

    public ServerRack parse(String input) {
        Map<String, List<String>> parsed = Arrays.stream(input.split("\n"))
                .map(String::trim)
                .filter(l -> !l.isBlank())
                .map(line -> line.split(":"))
                .collect(Collectors.toMap(
                        deviceStr -> deviceStr[0].trim(),
                        deviceStr -> Arrays.stream(deviceStr[1].trim().split("\\s+"))
                                .toList()
                ));

        for (Map.Entry<String, List<String>> entry : parsed.entrySet()) {
            deviceMap.put(entry.getKey(), new Device(entry.getKey(), entry.getValue()));
        }

        return this;
    }

    public List<String> next(Device device) {
        return device.outputs();
    }

    public Device getDevice(String label) {
        return deviceMap.getOrDefault(label, new Device(label, List.of()));
    }

    public long pathsTo(Device from, Device to, VisitedSet vs) {
        CacheKey key = new CacheKey(from, vs);
        if (mem.containsKey(key)) return mem.get(key);

        if (from.equals(to)) {
            return vs.isValid() ? 1 : 0;
        }

        long total = total(from, to, vs);
        mem.put(key, total);
        return total;
    }

    private long total(Device from, Device to, VisitedSet vs) {
        return next(from)
                .stream()
                .mapToLong(newFrom -> pathsTo(getDevice(newFrom), to, vs.addNew(from)))
                .sum();
    }

    public long pathsTo(String from, String to) {
        return pathsTo(getDevice(from), getDevice(to), new VisitedSet());
    }
}
