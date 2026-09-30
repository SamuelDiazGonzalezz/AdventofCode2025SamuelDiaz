package dia10.b;

import java.util.List;

/**
 * Representa el resultado de aplicar una combinación (máscara de bits) de
 * botones: el vector resultante de incrementos por palanca, su paridad
 * (cada valor módulo 2) y el número de botones pulsados (coste) para lograrlo.
 * Se usa como bloque básico del algoritmo "meet in the middle" del día 10 (parte b).
 */
public record PatternInfo(List<Integer> result, List<Integer> parity, int cost) {
}
