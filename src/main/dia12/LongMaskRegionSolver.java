package dia12;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

/**
 * Implementación de {@link RegionSolver} que representa las celdas ocupadas
 * de la región como una máscara de bits en un único {@code long}. Solo es
 * válida para regiones de hasta 64 celdas, pero es mucho más rápida que
 * {@link BitSetRegionSolver} al evitar la clonación de objetos y la
 * codificación de claves de memoización en texto.
 */
public class LongMaskRegionSolver implements RegionSolver {

    /**
     * Determina, mediante backtracking recursivo con memoización, si todas
     * las formas requeridas por la región pueden colocarse sin solaparse.
     *
     * @param region la región a comprobar.
     * @return {@code true} si todas las formas caben sin solaparse.
     */
    @Override
    public boolean canFit(Region region) {
        Map<Shape, List<Long>> cache = new IdentityHashMap<>();
        // Precalcula, para cada forma distinta, todas las posiciones posibles donde puede colocarse
        List<List<Long>> placements = region.presents().stream()
                .map(s -> cache.computeIfAbsent(s, sh -> precomputePlacements(sh, region)))
                .toList();
        List<List<Long>> ordered = new ArrayList<>(placements);
        // Se ordena para colocar primero las formas con menos posiciones posibles (poda más agresiva)
        ordered.sort(Comparator.comparingInt(List::size));
        return tryFit(0L, ordered, 0, new HashMap<>());
    }

    /**
     * Precalcula todas las máscaras de bits (long) posibles para colocar una
     * forma dentro de una región, probando cada una de sus orientaciones en
     * cada posición válida dentro de los límites de la región.
     *
     * @param shape la forma a colocar.
     * @param region la región donde se coloca.
     * @return lista de máscaras long distintas (una por cada colocación posible).
     */
    private List<Long> precomputePlacements(Shape shape, Region region) {
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
     * sobre el conjunto de celdas ya ocupadas.
     *
     * @param occupied máscara de bits (long) con las celdas ya ocupadas.
     * @param placements lista de listas de colocaciones posibles, una por cada forma pendiente.
     * @param index índice de la forma que se está intentando colocar actualmente.
     * @param memo caché de resultados ya calculados.
     * @return {@code true} si es posible colocar todas las formas desde el índice actual en adelante.
     */
    private boolean tryFit(long occupied, List<List<Long>> placements, int index, Map<Long, Boolean> memo) {
        // Caso base: no quedan más formas por colocar, éxito
        if (index >= placements.size()) return true;

        // Se combina el estado ocupado con el índice desplazado a bits altos para formar una clave única
        long key = occupied ^ ((long) index << 56);
        Boolean cached = memo.get(key);
        if (cached != null) return cached;

        // (occupied & mask) == 0 comprueba que no haya solapamiento (AND de bits vacío)
        boolean result = placements.get(index).stream()
                .anyMatch(mask -> (occupied & mask) == 0L && tryFit(occupied | mask, placements, index + 1, memo));

        memo.put(key, result);
        return result;
    }
}
