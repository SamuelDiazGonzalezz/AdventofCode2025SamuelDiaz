package dia5.b;

/**
 * Representa un rango cerrado [start, end] de ids válidos, usado en la parte b
 * del día 5 tras fusionar rangos solapados.
 *
 * @param start valor mínimo (inclusive) del rango
 * @param end   valor máximo (inclusive) del rango
 */
public record Range(long start, long end) {
    /**
     * Calcula la cantidad de valores enteros contenidos en el rango (ambos extremos incluidos).
     *
     * @return número de elementos del rango
     */
    public long count () {
        // La cantidad de enteros entre start y end (inclusive) es la diferencia más 1.
        return (end-start)+1;
    }
}
