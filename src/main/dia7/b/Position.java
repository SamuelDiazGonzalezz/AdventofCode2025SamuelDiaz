package dia7.b;

/**
 * Representa una posición (fila, columna) dentro de la cuadrícula del puzzle del día 7 (parte b),
 * usada para marcar por dónde pasa un haz ("beam") y cuántos caminos ("timelines") llegan a ella.
 *
 * @param row fila de la posición
 * @param col columna de la posición
 */
public record Position(int row, int col) { }
