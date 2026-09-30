package dia12;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Se encarga únicamente de parsear la entrada de texto del día 12 (formas y
 * regiones) y construir el {@link PresentFitter} correspondiente. Mantener
 * el parseo separado de la resolución del puzzle evita que una misma clase
 * mezcle dos responsabilidades distintas (leer texto vs. resolver el encaje).
 */
public class PresentFitterParser {

    /**
     * Parsea la entrada completa (formas y regiones) a partir del texto dado.
     *
     * @param input texto completo de entrada.
     * @return un nuevo {@link PresentFitter} con las formas y regiones parseadas.
     */
    public PresentFitter parse(String input) {
        return parseLines(Arrays.stream(input.split("\n")).toList());
    }

    /**
     * Limpia las líneas de entrada (recorta espacios y descarta vacías),
     * localiza dónde empieza la sección de regiones, y parsea por separado
     * las formas y las regiones.
     *
     * @param lines líneas de entrada sin procesar.
     * @return un nuevo {@link PresentFitter} con las formas y regiones parseadas.
     */
    private PresentFitter parseLines(List<String> lines) {
        List<String> cleaned = lines.stream()
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();

        // Busca el índice de la primera línea que define una región (formato "AxB: ...")
        // para saber dónde termina la sección de formas y empieza la de regiones
        int splitIndex = IntStream.range(0, cleaned.size())
                .filter(i -> isRegionLine(cleaned.get(i)))
                .findFirst()
                .orElse(cleaned.size());

        List<Shape> parsedShapes = parseShapes(cleaned.subList(0, splitIndex));
        List<Region> parsedRegions = parseRegions(cleaned.subList(splitIndex, cleaned.size()), parsedShapes);

        return new PresentFitter(parsedShapes, parsedRegions);
    }

    /**
     * Comprueba si una línea tiene el formato de definición de región
     * ("anchoxalto: cantidades").
     *
     * @param line la línea a comprobar.
     * @return true si la línea describe una región.
     */
    private boolean isRegionLine(String line) {
        return line.matches("\\d+x\\d+:.*");
    }

    /**
     * Extrae todas las formas definidas en las líneas dadas: cada forma
     * empieza con una línea "n:" (su identificador) seguida de las líneas
     * que dibujan su silueta con '#'.
     *
     * @param lines líneas correspondientes a la sección de formas.
     * @return lista de formas parseadas, en el orden en que aparecen.
     */
    private List<Shape> parseShapes(List<String> lines) {
        return IntStream.range(0, lines.size())
                // Identifica las líneas que marcan el inicio de una nueva forma ("n:")
                .filter(i -> lines.get(i).matches("\\d+:"))
                .mapToObj(i -> extractShape(lines, i))
                .toList();
    }

    /**
     * Extrae la forma que comienza justo después de la línea de índice dada,
     * tomando todas las líneas siguientes hasta encontrar el inicio de otra
     * forma o de la sección de regiones.
     *
     * @param lines todas las líneas de la sección de formas.
     * @param startIdx índice de la línea "n:" que marca el inicio de la forma.
     * @return la forma extraída.
     */
    private Shape extractShape(List<String> lines, int startIdx) {
        return Shape.create(
                IntStream.range(startIdx + 1, lines.size())
                        // Toma líneas consecutivas mientras no empiece otra forma ni una región
                        .takeWhile(i -> !lines.get(i).matches("\\d+:") && !isRegionLine(lines.get(i)))
                        .mapToObj(lines::get)
                        .toList()
        );
    }

    /**
     * Parsea todas las líneas de región de la sección correspondiente.
     *
     * @param lines líneas de la sección de regiones.
     * @param parsedShapes lista de formas ya parseadas (para referenciarlas por índice).
     * @return lista de regiones parseadas.
     */
    private List<Region> parseRegions(List<String> lines, List<Shape> parsedShapes) {
        return lines.stream()
                .filter(this::isRegionLine)
                .map(line -> parseRegion(line, parsedShapes))
                .toList();
    }

    /**
     * Parsea una línea de región con formato "anchoxalto: cantidad1 cantidad2 ...",
     * donde cada cantidad indica cuántas copias de la forma correspondiente
     * (por índice) debe contener la región.
     *
     * @param line la línea de región a parsear.
     * @param parsedShapes lista de formas disponibles (indexadas en el mismo orden que las cantidades).
     * @return la región parseada, con la lista expandida de formas que debe contener.
     */
    private Region parseRegion(String line, List<Shape> parsedShapes) {
        String[] parts = line.split(":\\s*");
        String[] dimensions = parts[0].split("x");
        int[] counts = Arrays.stream(parts[1].trim().split("\\s+"))
                .mapToInt(Integer::parseInt)
                .toArray();

        // Expande las cantidades: por cada forma i, se repite counts[i] veces en la lista final
        List<Shape> presents = IntStream.range(0, counts.length)
                .boxed()
                .flatMap(i -> IntStream.range(0, counts[i])
                        .mapToObj(_ -> parsedShapes.get(i)))
                .toList();

        return new Region(
                Integer.parseInt(dimensions[0]),
                Integer.parseInt(dimensions[1]),
                presents
        );
    }
}
