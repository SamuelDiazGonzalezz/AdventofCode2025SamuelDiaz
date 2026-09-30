package dia12;

/**
 * Estrategia para determinar si todas las formas requeridas por una región
 * pueden colocarse dentro de ella sin solaparse. Existen varias implementaciones
 * que resuelven el mismo problema con una representación interna distinta
 * (ver {@link LongMaskRegionSolver} y {@link BitSetRegionSolver}), seleccionadas
 * en tiempo de ejecución por {@link RegionSolverFactory} según el tamaño de la
 * región.
 */
public interface RegionSolver {

    /**
     * Comprueba si todas las formas requeridas por la región pueden colocarse
     * dentro de ella sin solaparse.
     *
     * @param region la región a comprobar, con sus formas requeridas ya asignadas.
     * @return {@code true} si existe una forma de colocar todas las formas sin solaparse.
     */
    boolean canFit(Region region);
}
