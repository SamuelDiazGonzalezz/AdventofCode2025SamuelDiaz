package dia3.b;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

/**
 * Representa un banco de baterías (una lista de dígitos) del problema del
 * día 3, parte B. A diferencia de la parte A, aquí se busca la mejor
 * combinación de hasta {@value #MAX_COMBINATION_LENGTH} dígitos (no solo
 * dos) que maximice el número resultante, manteniendo el orden original.
 *
 * @param batteries lista de dígitos (baterías) del banco.
 */
public record BatteryBank(List<Integer> batteries)  {

    /** Número máximo de dígitos que se combinan para formar el resultado. */
    private final static int MAX_COMBINATION_LENGTH = 12;

    /**
     * Calcula el resultado del banco de baterías: la mejor combinación
     * (manteniendo el orden) de hasta {@value #MAX_COMBINATION_LENGTH}
     * dígitos, unidos en un único número.
     *
     * @return el número resultante de la mejor combinación de dígitos.
     */
    public long sum() {
        // Convertir List<Integer> a List<Long> antes de procesarlo
        List<Long> longBatteries = batteries.stream()
                .map(Integer::longValue)
                .toList();
        return joinChars(maxCombination(longBatteries, MAX_COMBINATION_LENGTH));
    }

    /**
     * Calcula recursivamente la combinación de {@code k} dígitos (tomados
     * en orden, sin reordenar) que produce el mayor número posible.
     * En cada paso elige, de entre las posiciones válidas, el dígito más
     * grande que aún permite completar los {@code k} elementos restantes,
     * y continúa recursivamente con el resto de la lista.
     *
     * @param list lista de dígitos disponibles.
     * @param k número de dígitos que se deben elegir.
     * @return la lista con los {@code k} dígitos elegidos, en orden.
     */
    private static List<Long> maxCombination(List<Long> list, int k) {
        return k == 0 || list.isEmpty()
                ? List.of() // caso base: no quedan dígitos por elegir o no hay más lista disponible
                : IntStream.range(0, list.size() - k + 1) // solo se consideran posiciones que dejan suficientes elementos para completar k
                .reduce((i, j) -> list.get(i) >= list.get(j) ? i : j) // se queda con el índice del dígito más grande (el primero en caso de empate)
                .stream()
                .mapToObj(i -> concat(
                        list.get(i), // dígito elegido en este paso
                        maxCombination(list.subList(i + 1, list.size()), k - 1) // continúa eligiendo el resto tras la posición elegida
                ))
                .findFirst()
                .orElse(List.of());
    }

    /**
     * Concatena un dígito ("head") al principio de una lista de dígitos
     * ("tail"), devolviendo una nueva lista combinada.
     *
     * @param head dígito a colocar al principio.
     * @param tail resto de dígitos que van después.
     * @return la lista combinada con {@code head} seguido de {@code tail}.
     */
    private static List<Long> concat(Long head, List<Long> tail) {
        return LongStream.concat(
                LongStream.of(head),
                tail.stream().mapToLong(Long::longValue)
        ).boxed().toList();
    }

    /**
     * Une una lista de dígitos en un único número, concatenando su
     * representación en texto y volviendo a parsearlo como long.
     *
     * @param longElements lista de dígitos a combinar.
     * @return el número resultante de concatenar todos los dígitos.
     */
    private long joinChars(List<Long> longElements) {
        return Long.parseLong(longElements.stream()
                .map(String::valueOf)
                .collect(Collectors.joining()));
    }
}