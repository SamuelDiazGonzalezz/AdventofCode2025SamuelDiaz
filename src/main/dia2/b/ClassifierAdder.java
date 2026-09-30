package dia2.b;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Agrupa y procesa múltiples rangos de IDs (expresados como texto,
 * por ejemplo "10-20,30-40") creando un {@link Classifier} por cada
 * rango, y permite obtener la suma total de todos los IDs inválidos
 * encontrados en todos los rangos. Versión del día 2, parte B.
 */
public class ClassifierAdder {
    private final List<Classifier> classifierList;

    /**
     * Constructor privado: inicializa la lista de clasificadores vacía.
     * Se usa el patrón de fábrica estática {@link #create()} en su lugar.
     */
    private ClassifierAdder() { this.classifierList = new ArrayList<>(); }

    /**
     * Crea una nueva instancia vacía de {@link ClassifierAdder}.
     *
     * @return un nuevo acumulador de clasificadores.
     */
    public static ClassifierAdder create() { return new ClassifierAdder(); }

    /**
     * Compila una cadena con varios rangos separados por comas
     * (por ejemplo "10-20,30-40").
     *
     * @param rangesString cadena con los rangos separados por comas.
     * @return el propio acumulador, para poder encadenar llamadas.
     */
    public ClassifierAdder compile(String rangesString) {
        return compile(rangesString.split(","));
    }

    /**
     * Compila varios rangos de texto (por ejemplo "10-20"), creando y
     * calculando un {@link Classifier} para cada uno.
     *
     * @param idRanges rangos de IDs en formato texto ("inicio-fin").
     * @return el propio acumulador, para poder encadenar llamadas.
     */
    public ClassifierAdder compile(String... idRanges) {
        Arrays.stream(idRanges)
                .map(String::trim) // elimina espacios en blanco sobrantes
                .map(this::parse) // convierte el texto "inicio-fin" en un array [inicio, fin]
                .forEach(this::add); // crea y calcula un Classifier para cada rango
        return this;
    }

    /**
     * Crea un {@link Classifier} para el rango indicado, lo calcula
     * y lo añade a la lista de clasificadores.
     *
     * @param ids array de dos posiciones: [inicio, fin] del rango.
     */
    private void add(long[] ids) {
        classifierList.add(Classifier.create().from(ids[0]).to(ids[1]).calculate());
    }

    /**
     * Parsea una cadena de rango en formato "inicio-fin" separándola
     * por el guion.
     *
     * @param range cadena de texto del rango.
     * @return array de long con los límites del rango.
     */
    private long[] parse(String range) {
        return parse(range.split("-"));
    }

    /**
     * Convierte un array de cadenas de texto en un array de long.
     *
     * @param split array de cadenas de texto (números).
     * @return array de long resultante de parsear cada cadena.
     */
    private long[] parse(String[] split) {
        return Arrays.stream(split).mapToLong(Long::parseLong).toArray();
    }

    /**
     * Suma los resultados de todos los clasificadores (suma de IDs
     * inválidos de todos los rangos procesados).
     *
     * @return la suma total de IDs inválidos de todos los rangos.
     */
    public long sum() {
        return classifierList.stream().mapToLong(Classifier::sum).sum();
    }
}
