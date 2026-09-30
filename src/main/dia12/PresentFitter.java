package dia12;

import java.util.List;

/**
 * Resuelve el problema del día 12: dado un conjunto de formas (regalos) y una
 * lista de regiones rectangulares, cada una con una cantidad requerida de cada
 * forma, determina cuántas regiones pueden contener todas sus formas sin
 * solaparse (encaje tipo puzzle). El parseo de la entrada se delega en
 * {@link PresentFitterParser} y la comprobación de encaje de cada región se
 * delega en la abstracción {@link RegionSolver} (elegida por
 * {@link RegionSolverFactory}), de forma que esta clase solo se encarga de
 * orquestar el flujo completo, sin conocer los detalles de cómo se parsea
 * el texto ni de cómo se resuelve el backtracking.
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
     * Parsea la entrada completa (formas y regiones) a partir del texto dado,
     * delegando todo el trabajo de parseo en {@link PresentFitterParser}.
     *
     * @param input texto completo de entrada.
     * @return un nuevo PresentFitter con las formas y regiones parseadas.
     */
    public PresentFitter parse(String input) {
        return new PresentFitterParser().parse(input);
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
     * rápida y barata); si no se puede descartar así, delega la comprobación
     * completa en el {@link RegionSolver} adecuado para el tamaño de la región.
     *
     * @param region la región a comprobar.
     * @return true si todas las formas de la región pueden colocarse sin solaparse.
     */
    private boolean canFitPresents(Region region) {
        // Comprobación rápida: si el área de las formas ya supera el área de la región, es imposible
        if (region.presentsArea() > region.area()) return false;
        return RegionSolverFactory.forRegion(region).canFit(region);
    }
}
