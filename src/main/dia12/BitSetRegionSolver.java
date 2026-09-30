package dia12;

import java.util.ArrayList;
import java.util.Base64;
import java.util.BitSet;
import java.util.Comparator;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

/**
 * Implementación de {@link RegionSolver} que representa las celdas ocupadas
 * de la región mediante un {@link BitSet}, sin límite de tamaño. Es más lenta
 * que {@link LongMaskRegionSolver} (requiere clonar el BitSet y codificar las
 * claves de memoización), pero es la única opción válida para regiones de
 * más de 64 celdas.
 */
public class BitSetRegionSolver implements RegionSolver {

    /**
     * Determina, mediante backtracking recursivo con memoización, si todas
     * las formas requeridas por la región pueden colocarse sin solaparse.
     *
     * @param region la región a comprobar.
     * @return {@code true} si todas las formas caben sin solaparse.
     */
    @Override
    public boolean canFit(Region region) {
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
                .flatMap(variant -> IntStream.rangeClosed(0, region.height() - variant.height())
                        .boxed()
                        .flatMap(y -> IntStream.rangeClosed(0, region.width() - variant.width())
                                .mapToObj(x -> variant.toBitmask(x, y, region.width()))))
                .distinct()
                .toList();
    }

    /**
     * Backtracking recursivo (con memoización) que intenta colocar, en orden,
     * cada lista de posibles colocaciones (una lista por forma requerida)
     * sobre el conjunto de celdas ya ocupadas.
     *
     * @param occupied máscara de bits con las celdas ya ocupadas por formas colocadas previamente.
     * @param placements lista de listas de colocaciones posibles, una por cada forma pendiente.
     * @param index índice de la forma que se está intentando colocar actualmente.
     * @param memo caché de resultados ya calculados, indexada por "índice + estado ocupado".
     * @return {@code true} si es posible colocar todas las formas desde el índice actual en adelante.
     */
    private boolean tryFit(BitSet occupied, List<List<BitSet>> placements, int index, Map<String, Boolean> memo) {
        if (index >= placements.size()) return true;

        // Clave de memoización: combina el índice de forma actual con el estado de celdas ocupadas
        String key = index + ":" + Base64.getEncoder().encodeToString(occupied.toByteArray());
        Boolean cached = memo.get(key);
        if (cached != null) return cached;

        boolean result = placements.get(index).stream()
                // Solo se consideran colocaciones que no se solapen con las celdas ya ocupadas
                .filter(mask -> !mask.intersects(occupied))
                .anyMatch(mask -> {
                    BitSet next = (BitSet) occupied.clone();
                    next.or(mask);
                    return tryFit(next, placements, index + 1, memo);
                });

        memo.put(key, result);
        return result;
    }
}
