package dia6.a;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Calculadora del día 6 (parte a): interpreta una entrada en forma de columnas
 * de números, donde cada columna representa un "problema" que puede ser una suma
 * o, si termina en "*", un producto. Transpone las filas de texto en columnas
 * de operandos y acumula el resultado total.
 */
public class CephalopodMathCalculator {
    final private List<ProductList> productLists;
    final private SumList sumList;

    /**
     * Crea una nueva instancia vacía de {@link CephalopodMathCalculator}.
     *
     * @return una nueva instancia lista para parsear datos
     */
    public static CephalopodMathCalculator create() {
        return new CephalopodMathCalculator();
    }

    /**
     * Constructor privado: inicializa la lista de listas de productos y la lista de sumas.
     * Se usa el método estático {@link #create()} para instanciar la clase.
     */
    private CephalopodMathCalculator() {
        this.productLists = new ArrayList<>();
        this.sumList = SumList.create();
    }

    /**
     * Parsea la entrada completa, dividiéndola en líneas.
     *
     * @param input texto completo de entrada
     * @return la propia instancia, con los datos ya parseados
     */
    public CephalopodMathCalculator parse(String input) {
         return parse(input.split("\n"));
    }

    /**
     * Convierte cada línea de texto en un array de tokens (separados por espacios).
     *
     * @param split líneas de la entrada
     * @return la propia instancia, tras delegar el parseo con las filas ya tokenizadas
     */
    private CephalopodMathCalculator parse(String[] split) {
        // Cada línea se recorta y se separa por uno o más espacios en blanco.
        return parse(Arrays.stream(split).map(s -> s.trim().split("\\s+")).toList());
    }

    /**
     * Transpone las filas de tokens en columnas: cada "problema" original está distribuido
     * verticalmente entre varias filas de texto, así que se reconstruye tomando la columna i
     * de cada fila.
     *
     * @param problems lista de filas de tokens (cada fila es un array de Strings)
     * @return la propia instancia, tras procesar cada columna como un problema independiente
     */
    private CephalopodMathCalculator parse(List<String[]> problems) {
        // Se recorre cada índice de columna (basado en la longitud de la primera fila)...
        return parse(IntStream.range(0, problems.getFirst().length)
                .mapToObj(i ->
                        // ...y para cada columna i se extrae el valor en esa posición de todas las filas,
                        // reconstruyendo así el "problema" original como una columna de operandos.
                        problems
                                .stream()
                                .map(row -> row[i])
                                .toArray(String[]::new)
                )
        );
    }

    /**
     * Procesa cada columna (problema) ya reconstruida: si el último token es "*",
     * se trata como un producto y se añade a la lista de productos; en caso contrario,
     * se añade a la lista de sumas.
     *
     * @param problems flujo de columnas, cada una representando un problema completo
     * @return la propia instancia, con todos los problemas ya clasificados
     */
    private CephalopodMathCalculator parse(Stream<String[]> problems) {
        problems.forEach(row -> {
            // El último elemento de la fila indica el operador: si es "*", es un producto.
            if (row[row.length - 1].equals("*")) {
                productLists.add(ProductList.create().addAll(toLongList(row)));
            } else {
                sumList.addAll(toLongList(row));
            }
        });
        return this;
    }

    /**
     * Convierte una fila de tokens (sin contar el último, que es el operador) en una lista de números long.
     *
     * @param row fila de tokens, con el operador en la última posición
     * @return lista de números long extraídos de la fila
     */
    private List<Long> toLongList(String[] row) {
        return Arrays
                .asList(row)
                // Se excluye el último elemento (el operador "*" o vacío) de la conversión numérica.
                .subList(0, row.length-1)
                .stream()
                .mapToLong(Long::parseLong)
                .boxed()
                .toList();
    }

    /**
     * Calcula el resultado total del cálculo del día 6: la suma de todos los sumandos
     * más la suma de los resultados de cada lista de productos.
     *
     * @return el resultado total combinando sumas y productos
     */
    public long compute() {
        // Se suma el resultado de la lista de sumas con la suma de los resultados de cada producto.
        return sumList.compute() + productLists.stream().map(ProductList::compute).reduce(0L, Long::sum);
    }
}
