package dia7.a;

/**
 * Representa una posición (fila, columna) dentro de la cuadrícula del puzzle del día 7,
 * usada para marcar por dónde pasa un haz ("beam").
 *
 * @param row fila de la posición
 * @param col columna de la posición
 */
public record Position(int row, int col) { }
