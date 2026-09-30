package dia12;

/**
 * Fábrica que selecciona, para una región concreta, la implementación de
 * {@link RegionSolver} más adecuada según su tamaño: para regiones de hasta
 * 64 celdas usa {@link LongMaskRegionSolver} (más rápido), y para regiones
 * mayores usa {@link BitSetRegionSolver} (sin límite de tamaño).
 * Los llamadores dependen únicamente de la abstracción {@link RegionSolver},
 * sin conocer qué implementación concreta se está usando.
 */
public final class RegionSolverFactory {

    private static final int LONG_MASK_CELL_LIMIT = 64;

    private RegionSolverFactory() {
    }

    /**
     * Elige el {@link RegionSolver} adecuado para el tamaño de la región dada.
     *
     * @param region la región a resolver.
     * @return una implementación de {@link RegionSolver} apta para esa región.
     */
    public static RegionSolver forRegion(Region region) {
        int regionSize = region.width() * region.height();
        return regionSize <= LONG_MASK_CELL_LIMIT
                ? new LongMaskRegionSolver()
                : new BitSetRegionSolver();
    }
}
