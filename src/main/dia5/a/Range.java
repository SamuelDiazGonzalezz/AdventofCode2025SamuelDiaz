package dia5.a;

/**
 * Representa un rango cerrado [start, end] de ids considerados válidos
 * (frescos) para los ingredientes del inventario.
 *
 * @param start valor mínimo (inclusive) del rango
 * @param end   valor máximo (inclusive) del rango
 */
public record Range(long start, long end) {
    /**
     * Comprueba si un id de ingrediente cae dentro de este rango (ambos extremos incluidos).
     *
     * @param ingredientId id del ingrediente a comprobar
     * @return true si el id está dentro del rango [start, end]
     */
    public boolean isInRange(long ingredientId) {
        // El id es válido si es mayor o igual que el inicio y menor o igual que el fin del rango.
        return start <= ingredientId && end >= ingredientId;
    }
}
