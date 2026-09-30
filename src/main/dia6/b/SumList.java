package dia6.b;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Implementación de {@link OperatorList} que acumula operandos y calcula su suma.
 * Se usa para los problemas del día 6 (parte b) asociados al operador "+".
 */
public class SumList implements OperatorList {
    final private List<Long> sumList;

    /**
     * Crea una nueva instancia vacía de {@link SumList}.
     *
     * @return una nueva lista de sumandos
     */
    public static SumList create() {
        return new SumList();
    }

    /**
     * Constructor privado: inicializa la lista vacía de operandos.
     * Se usa el método estático {@link #create()} para instanciar la clase.
     */
    private SumList() {
        this.sumList = new ArrayList<>();
    }

    /**
     * Devuelve el número de operandos almacenados.
     *
     * @return tamaño de la lista
     */
    @Override
    public int size() {
        return sumList.size();
    }

    /**
     * Añade un único número a la lista de operandos.
     *
     * @param number número a añadir
     * @return la propia instancia, para encadenar llamadas
     */
    @Override
    public SumList add(long number) {
        sumList.add(number);
        return this;
    }

    /**
     * Añade un array de números a la lista de operandos.
     *
     * @param numbers array de números a añadir
     * @return la propia instancia, para encadenar llamadas
     */
    @Override
    public SumList addAll(Long[] numbers) {
        // Arrays.asList convierte el array en una vista de lista antes de añadirla.
        sumList.addAll(Arrays.asList(numbers));
        return this;
    }

    /**
     * Añade una colección de números a la lista de operandos.
     *
     * @param numbers lista de números a añadir
     * @return la propia instancia, para encadenar llamadas
     */
    @Override
    public SumList addAll(List<Long> numbers) {
        sumList.addAll(numbers);
        return this;
    }

    /**
     * Calcula la suma de todos los operandos almacenados.
     *
     * @return el resultado de sumar todos los números de la lista
     */
    @Override
    public long compute() {
        return sumList.stream().mapToLong(Long::longValue).sum();
    }
}
