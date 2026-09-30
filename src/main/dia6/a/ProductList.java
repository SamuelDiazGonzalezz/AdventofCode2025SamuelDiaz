package dia6.a;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Implementación de {@link OperatorList} que acumula operandos y calcula su producto.
 * Se usa para las filas de la entrada del día 6 marcadas con el operador "*".
 */
public class ProductList implements OperatorList {
    final private List<Long> productList;

    /**
     * Crea una nueva instancia vacía de {@link ProductList}.
     *
     * @return una nueva lista de productos
     */
    public static ProductList create() {
        return new ProductList();
    }

    /**
     * Constructor privado: inicializa la lista vacía de operandos.
     * Se usa el método estático {@link #create()} para instanciar la clase.
     */
    private ProductList() {
        this.productList = new ArrayList<>();
    }

    /**
     * Devuelve el número de operandos almacenados.
     *
     * @return tamaño de la lista
     */
    @Override
    public int size() {
        return productList.size();
    }

    /**
     * Añade un único número a la lista de operandos.
     *
     * @param number número a añadir
     * @return la propia instancia, para encadenar llamadas
     */
    @Override
    public ProductList add(long number) {
        productList.add(number);
        return this;
    }

    /**
     * Añade uno o varios números (varargs) a la lista de operandos.
     *
     * @param numbers números a añadir
     * @return la propia instancia, para encadenar llamadas
     */
    @Override
    public ProductList addAll(Long numbers) {
        // Arrays.asList convierte el/los Long recibido(s) en una lista antes de añadirlos.
        productList.addAll(Arrays.asList(numbers));
        return this;
    }

    /**
     * Añade una colección de números a la lista de operandos.
     *
     * @param numbers lista de números a añadir
     * @return la propia instancia, para encadenar llamadas
     */
    @Override
    public ProductList addAll(List<Long> numbers) {
        productList.addAll(numbers);
        return this;
    }

    /**
     * Calcula el producto de todos los operandos almacenados.
     *
     * @return el resultado de multiplicar todos los números de la lista
     */
    @Override
    public long compute() {
        // Se parte de 1 (elemento neutro de la multiplicación) y se van multiplicando todos los valores.
        return productList.stream().mapToLong(Long::longValue).reduce(1, (a, b) -> a * b);
    }
}
