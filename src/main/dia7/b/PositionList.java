package dia7.b;

import java.util.HashSet;

/**
 * Conjunto de {@link Position} sin duplicados, que representa todas las posiciones
 * ocupadas por un haz en una fila de la cuadrícula del día 7 (parte b).
 */
public class PositionList extends HashSet<Position> {

    /**
     * Comprueba si existe alguna posición almacenada con la fila y columna indicadas.
     *
     * @param row fila a buscar
     * @param col columna a buscar
     * @return true si existe una posición con esa fila y columna
     */
    public boolean positionExists(int row, int col) {
        // Se recorre el conjunto buscando una coincidencia exacta de fila y columna.
        return this.stream().anyMatch(p -> p.row() == row && p.col() == col);
    }
}
