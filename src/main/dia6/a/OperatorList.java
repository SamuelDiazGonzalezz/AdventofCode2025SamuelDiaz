package dia6.a;

import java.util.List;

/**
 * Contrato común para las listas de operandos del día 6 (sumas y productos),
 * que permiten acumular números y calcular el resultado de la operación asociada.
 */
public interface OperatorList {
    /**
     * Añade un único número a la lista de operandos.
     *
     * @param number número a añadir
     * @return la propia lista, para encadenar llamadas
     */
    public OperatorList add(long number);

    /**
     * Añade varios números (en formato varargs/array de Long) a la lista de operandos.
     *
     * @param numbers números a añadir
     * @return la propia lista, para encadenar llamadas
     */
    public OperatorList addAll(Long numbers);

    /**
     * Añade una colección de números a la lista de operandos.
     *
     * @param numbers lista de números a añadir
     * @return la propia lista, para encadenar llamadas
     */
    public OperatorList addAll(List<Long> numbers);

    /**
     * Calcula el resultado de aplicar la operación (suma o producto) a todos los operandos almacenados.
     *
     * @return el resultado de la operación
     */
    public long compute();

    /**
     * Devuelve el número de operandos almacenados.
     *
     * @return tamaño de la lista de operandos
     */
    public int size();
}
