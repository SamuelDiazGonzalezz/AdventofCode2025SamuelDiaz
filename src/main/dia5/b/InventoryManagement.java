package dia5.b;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collector;

/**
 * Gestiona el inventario del día 5 (parte b): parsea los rangos de la entrada,
 * los ordena y fusiona los que se solapan o son contiguos, para luego poder
 * contar el total de valores cubiertos por todos los rangos combinados.
 */
public class InventoryManagement {
    private final List<Range> ranges;
    // Expresión regular que detecta una o más líneas vacías (separador entre bloques de la entrada).
    private final static String EMPTY_LINE_REGEX = "(?m)^\\s*$\\R+";

    /**
     * Constructor privado: inicializa la lista vacía de rangos.
     * Se usa el método estático {@link #create()} para instanciar la clase (patrón factoría estática).
     */
    private InventoryManagement() {
        this.ranges = new ArrayList<>();
    }

    /**
     * Crea una nueva instancia vacía de {@link InventoryManagement}.
     *
     * @return una nueva instancia lista para parsear datos
     */
    public static InventoryManagement create() {
        return new InventoryManagement();
    }

    /**
     * Añade un nuevo rango a la lista, sin fusionarlo con los existentes.
     *
     * @param start inicio (inclusive) del rango
     * @param end   fin (inclusive) del rango
     * @return la propia instancia, para encadenar llamadas
     */
    public InventoryManagement addRange(long start, long end) {
        ranges.add(new Range(start, end));
        return this;
    }

    /**
     * Añade un rango a partir de su representación en texto, con formato "inicio-fin".
     *
     * @param range cadena con el rango en formato "inicio-fin"
     * @return la propia instancia, para encadenar llamadas
     */
    public InventoryManagement addRange(String range) {
        this.addRange(getRangeValue(range, 0), getRangeValue(range, 1));
        return this;
    }

    /**
     * Parsea el bloque de rangos de la entrada, los ordena por su valor de inicio
     * y fusiona los solapados o contiguos antes de guardarlos.
     *
     * @param input texto completo de entrada
     * @return la propia instancia, con los rangos ya fusionados y cargados
     */
    public InventoryManagement parse(String input) {
        ranges.addAll(input.split(EMPTY_LINE_REGEX)[0].lines()
                // Cada línea "inicio-fin" se separa por el guion.
                .map(l -> l.split("-"))
                // Se construye un Range a partir de las dos partes numéricas.
                .map(p -> new Range(
                        Long.parseLong(p[0]),
                        Long.parseLong(p[1])
                ))
                // Se ordenan los rangos por su valor de inicio para poder fusionarlos en un solo recorrido.
                .sorted(Comparator.comparingLong(Range::start))
                // Se recolectan fusionando los rangos solapados o contiguos.
                .collect(mergingRanges()));

        return this;
    }

    /**
     * Comprueba si dos rangos (ya ordenados por inicio) se solapan o son contiguos,
     * es decir, si el inicio del segundo cae dentro o justo después del final del primero.
     *
     * @param a primer rango (con menor o igual inicio)
     * @param b segundo rango a comparar
     * @return true si b se solapa con a o es contiguo a él
     */
    private boolean overlaps(Range a, Range b) {
        // Si el inicio de b es menor o igual que el final de a más uno, se consideran solapados/contiguos.
        return b.start() <= a.end() + 1;
    }

    /**
     * Fusiona dos rangos solapados o contiguos en uno solo que los cubre a ambos.
     *
     * @param a primer rango
     * @param b segundo rango
     * @return un nuevo rango que abarca desde el inicio de a hasta el mayor de los dos finales
     */
    private Range merge(Range a, Range b) {
        // El inicio del rango fusionado es el de a (ya que están ordenados), y el fin es el máximo de ambos.
        return new Range(a.start(), Math.max(a.end(), b.end()));
    }

    /**
     * Construye un {@link Collector} personalizado que recorre una lista de rangos ya ordenados
     * y va fusionando en el acumulador los que se solapan o son contiguos con el último añadido.
     *
     * @return un collector que produce la lista de rangos fusionados
     */
    private Collector<Range, List<Range>, List<Range>> mergingRanges() {
        return Collector.of(
                // Supplier: el acumulador inicial es una lista vacía.
                ArrayList::new,
                // Accumulator: por cada rango, si no se solapa con el último acumulado se añade nuevo,
                // si se solapa se sustituye el último por la fusión de ambos.
                (acc, r) -> {
                    if ((acc.isEmpty() || !overlaps(acc.getLast(), r))) {
                        acc.add(r);
                    } else {
                        acc.set(
                                acc.size() - 1,
                                merge(acc.getLast(), r)
                        );
                    }
                },
                // Combiner: en caso de ejecución en paralelo, se concatenan los acumuladores parciales.
                (a, b) -> { a.addAll(b); return a; }
        );
    }

    /**
     * Extrae uno de los dos valores numéricos (inicio o fin) de una cadena de rango "inicio-fin".
     *
     * @param range cadena con el rango en formato "inicio-fin"
     * @param x     índice del valor a extraer (0 = inicio, 1 = fin)
     * @return el valor numérico extraído
     */
    private long getRangeValue(String range, int x) {
        return parseNumber(range.split("-")[x]);
    }

    /**
     * Convierte una cadena de texto en un número long.
     *
     * @param str cadena a convertir
     * @return el valor numérico correspondiente
     */
    private long parseNumber(String str) {
        return Long.parseLong(str);
    }

    /**
     * Suma la cantidad de valores cubiertos por todos los rangos ya fusionados.
     *
     * @return el total de ids cubiertos por los rangos
     */
    public long count () {
        return this.ranges.stream()
                // Se obtiene el tamaño de cada rango y se suman todos.
                .mapToLong(Range::count)
                .sum();
    }
}
