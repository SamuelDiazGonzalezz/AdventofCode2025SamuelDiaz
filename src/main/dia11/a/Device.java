package dia11.a;

import java.util.List;
import java.util.Objects;

public record Device(String label, List<String> outputs) {
    @Override
    public String toString() {
        return label + "→"  + String.join(", ", outputs);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Device device = (Device) o;
        return Objects.equals(label, device.label);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(label);
    }


}
