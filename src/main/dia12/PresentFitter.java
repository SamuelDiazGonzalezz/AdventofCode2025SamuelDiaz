package dia12;

import java.util.*;
import java.util.stream.*;

/**
 * Resuelve el problema del día 12: dado un conjunto de formas (regalos) y una
 * lista de regiones rectangulares, cada una con una cantidad requerida de cada
 * forma, determina cuántas regiones pueden contener todas sus formas sin
 * solaparse (encaje tipo puzzle/backtracking). Usa una estrategia de
 * backtracking con memoización sobre máscaras de bits (BitSet o long) que
 * representan las celdas ya ocupadas de la región.
 */
public record PresentFitter(List<Shape> shapes, List<Region> regions) {

    /**
     * Crea un PresentFitter vacío, sin formas ni regiones.
     *
     * @return una nueva instancia vacía.
     */
    public static PresentFitter create() {
        return new PresentFitter(List.of(), List.of());
    }

    /**
     * Parsea la entrada completa (formas y regiones) a partir del texto dado.
     *
     * @param input texto completo de entrada.
     * @return un nuevo PresentFitter con las formas y regiones parseadas.
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
     * @return un nuevo PresentFitter con las formas y regiones parseadas.
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

    /**
     * Cuenta cuántas regiones pueden encajar todas sus formas requeridas sin
     * solaparse.
     *
     * @return el número de regiones en las que es posible colocar todos sus regalos.
     */
    public long fittableRegions() {
        return regions.stream()
                .filter(this::canFitPresents)
                .count();
    }

    /**
     * Determina si todas las formas requeridas por una región pueden colocarse
     * dentro de ella sin solaparse. Primero descarta por área (comprobación
     * rápida); luego, según el tamaño de la región, usa una representación en
     * {@code long} (más rápida, para regiones de hasta 64 celdas) o en
     * {@link BitSet} (para regiones mayores), aplicando backtracking con
     * memoización sobre el conjunto de celdas ya ocupadas.
     *
     * @param region la región a comprobar.
     * @return true si todas las formas de la región pueden colocarse sin solaparse.
     */
    private boolean canFitPresents(Region region) {
        // Comprobación rápida: si el área de las formas ya supera el área de la región, es imposible
        if (region.presentsArea() > region.area()) return false;

        int regionSize = region.width() * region.height();

        if (regionSize <= 64) {
            // Región pequeña: se usa una máscara de bits como long (mucho más rápida que BitSet)
            Map<Shape, List<Long>> cache = new IdentityHashMap<>();
            // Precalcula, para cada forma distinta, todas las posiciones posibles donde puede colocarse
            List<List<Long>> placements = region.presents().stream()
                    .map(s -> cache.computeIfAbsent(s, sh -> precomputePlacementsLong(sh, region)))
                    .toList();
            List<List<Long>> ordered = new ArrayList<>(placements);
            // Se ordena para colocar primero las formas con menos posiciones posibles
            // (poda más agresiva y temprana en el backtracking)
            ordered.sort(Comparator.comparingInt(List::size));
            return tryFitLong(0L, ordered, 0, new HashMap<>());
        }

        // Región grande: se usa BitSet, que soporta más de 64 celdas
        Map<Shape, List<BitSet>> cache = new IdentityHashMap<>();
        List<List<BitSet>> allPlacements = region.presents().stream()
                .map(s -> cache.computeIfAbsent(s, sh -> precomputePlacements(sh, region)))
                .toList();
        List<List<BitSet>> ordered = new ArrayList<>(allPlacements);
        ordered.sort(Comparator.comparingInt(List::size));

        return tryFit(new BitSet(), ordered, 0, new HashMap<>());
    }

    /**
     * Precalcula todas las máscaras de bits (BitSet) posibles para colocar una
     * forma dentro de una región, probando cada una de sus orientaciones
     * (rotaciones/reflejos) en cada posición válida dentro de los límites de
     * la región.
     *
     * @param shape la forma a colocar.
     * @param region la región donde se coloca.
     * @return lista de máscaras de bits distintas (una por cada colocación posible).
     */
    private List<BitSet> precomputePlacements(Shape shape, Region region) {
        return shape.allOrientations().stream()
                // Para cada orientación, se recorren todas las posiciones (x,y) donde cabe dentro de la región
                .flatMap(variant -> IntStream.rangeClosed(0, region.height() - variant.height())
                        .boxed()
                        .flatMap(y -> IntStream.rangeClosed(0, region.width() - variant.width())
                                .mapToObj(x -> variant.toBitmask(x, y, region.width()))))
                .distinct()
                .toList();
    }

        /**
         * Igual que {@link #precomputePlacements(Shape, Region)} pero generando
         * máscaras de bits como {@code long}, usado para regiones pequeñas
         * (hasta 64 celdas) por eficiencia.
         *
         * @param shape la forma a colocar.
         * @param region la región donde se coloca.
         * @return lista de máscaras long distintas (una por cada colocación posible).
         */
        private List<Long> precomputePlacementsLong(Shape shape, Region region) {
        return shape.allOrientations().stream()
            .flatMap(variant -> IntStream.rangeClosed(0, region.height() - variant.height())
                .boxed()
                .flatMap(y -> IntStream.rangeClosed(0, region.width() - variant.width())
                    .mapToObj(x -> variant.toLongMask(x, y, region.width()))))
            .distinct()
            .toList();
        }

    /**
     * Backtracking recursivo (con memoización) que intenta colocar, en orden,
     * cada lista de posibles colocaciones (una lista por forma requerida)
     * sobre el conjunto de celdas ya ocupadas, usando {@link BitSet}.
     *
     * @param occupied máscara de bits con las celdas ya ocupadas por formas colocadas previamente.
     * @param placements lista de listas de colocaciones posibles, una por cada forma pendiente (en orden).
     * @param index índice de la forma que se está intentando colocar actualmente.
     * @param memo caché de resultados ya calculados, indexada por "índice + estado ocupado".
     * @return true si es posible colocar todas las formas desde el índice actual en adelante.
     */
    private boolean tryFit(BitSet occupied, List<List<BitSet>> placements, int index, Map<String, Boolean> memo) {
        // Caso base: no quedan más formas por colocar, éxito
        if (index >= placements.size()) return true;

        // Clave de memoización: combina el índice de forma actual con el estado de celdas ocupadas
        String key = index + ":" + Base64.getEncoder().encodeToString(occupied.toByteArray());
        Boolean cached = memo.get(key);
        if (cached != null) return cached;

        boolean result = placements.get(index).stream()
                // Solo se consideran colocaciones que no se solapen con las celdas ya ocupadas
                .filter(mask -> !mask.intersects(occupied))
                // Se prueba cada colocación válida y se continúa recursivamente con la siguiente forma
                .anyMatch(mask -> {
                    BitSet next = (BitSet) occupied.clone();
                    next.or(mask);
                    return tryFit(next, placements, index + 1, memo);
                });

        memo.put(key, result);
        return result;
    }

    /**
     * Igual que {@link #tryFit(BitSet, List, int, Map)} pero trabajando con
     * máscaras de bits como {@code long}, usado para regiones pequeñas
     * (hasta 64 celdas) por eficiencia (evita clonar BitSet y codificar Base64).
     *
     * @param occupied máscara de bits (long) con las celdas ya ocupadas.
     * @param placements lista de listas de colocaciones posibles (en long), una por cada forma pendiente.
     * @param index índice de la forma que se está intentando colocar actualmente.
     * @param memo caché de resultados ya calculados, indexada por una clave combinando índice y estado ocupado.
     * @return true si es posible colocar todas las formas desde el índice actual en adelante.
     */
    private boolean tryFitLong(long occupied, List<List<Long>> placements, int index, Map<Long, Boolean> memo) {
        // Caso base: no quedan más formas por colocar, éxito
        if (index >= placements.size()) return true;

        // Se combina el estado ocupado con el índice desplazado a bits altos para formar una clave única
        long key = occupied ^ ((long) index << 56);
        Boolean cached = memo.get(key);
        if (cached != null) return cached;

        // Para cada colocación posible: (occupied & mask) == 0 comprueba que no haya solapamiento (AND de bits vacío)
        boolean result = placements.get(index).stream()
                .anyMatch(mask -> (occupied & mask) == 0L && tryFitLong(occupied | mask, placements, index + 1, memo));

        memo.put(key, result);
        return result;
    }
}
