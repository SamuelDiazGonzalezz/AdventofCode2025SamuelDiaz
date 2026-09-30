package dia12;

/**
 * Representa una coordenada (x, y) dentro de una forma o región del problema
 * del día 12 (empaquetado de regalos/formas en regiones rectangulares).
 */
public record Position(int x, int y) {
    /**
     * Suma componente a componente esta posición con otra, útil para
     * trasladar celdas de una forma a un desplazamiento (offset) determinado.
     *
     * @param other la posición a sumar.
     * @return una nueva posición resultante de la suma.
     */
    public Position add(Position other) {
        return new Position(x + other.x, y + other.y);
    }
}
