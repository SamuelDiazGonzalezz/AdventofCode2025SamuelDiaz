package dia6.a;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Implementación de {@link OperatorList} que acumula operandos y calcula su suma.
 * Se usa para las filas de la entrada del día 6 que no están marcadas con el operador "*".
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
     * Añade uno o varios números (varargs) a la lista de operandos.
     *
     * @param numbers números a añadir
     * @return la propia instancia, para encadenar llamadas
     */
    @Override
    public SumList addAll(Long numbers) {
        // Arrays.asList convierte el/los Long recibido(s) en una lista antes de añadirlos.
        sumList.addAll(Arrays.asList(numbers));
        return this;
    }

    /**
     * Añade una colección de números a la lista de operandos.
     *
     * @param numbers lista de números a añadir
     * @return la propia instancia (nótese que aquí se devuelve {@code null} en vez de {@code this})
     */
    @Override
    public SumList addAll(List<Long> numbers) {
        sumList.addAll(numbers);
        // Nota: a diferencia de los demás métodos "addAll", este devuelve null en lugar de "this".
        return null;
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
