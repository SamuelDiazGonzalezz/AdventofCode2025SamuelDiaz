package dia11.b;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class VisitedSet {
    private final Set<Device> deviceList;
    private final Set<Device> mustHaveDevices;
    private static final String[] MUST_PASS_DEVICES = new String[] { "dac", "fft" };

    public VisitedSet() {
        this.deviceList = new HashSet<>();
        this.mustHaveDevices =  new HashSet<>(
                Arrays.stream(MUST_PASS_DEVICES)
                        .map(str -> new Device(str, List.of()))
                        .toList()
        );

    }

    public VisitedSet add(Device dev) {
        deviceList.add(dev);
        return this;
    }

    public boolean isValid() {
        return deviceList.containsAll(mustHaveDevices);
    }

    @Override
    public String toString() {
        return "VisitedSet{" +
                "deviceList=" + deviceList +
                '}';
    }

    public VisitedSet copy() {
        VisitedSet copy = new VisitedSet();
        copy.deviceList.addAll(this.deviceList);
        return copy;
    }

    public boolean contains(Device dev) {
        return deviceList.contains(dev);
    }

    public VisitedSet addNew(Device from) {
        return copy().add(from);
    }

    public long requiredCount() {
        return deviceList.stream().filter(mustHaveDevices::contains).count();
    }
}
