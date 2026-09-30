package dia10.b;

import java.util.Set;

/**
 * Representa un botón de la máquina del día 10 (parte b). Al pulsarlo,
 * incrementa en 1 el nivel de voltaje de cada palanca (lever) indicada
 * en {@code joltageLevers}.
 */
public record Button(Set<Integer> joltageLevers) {
}
