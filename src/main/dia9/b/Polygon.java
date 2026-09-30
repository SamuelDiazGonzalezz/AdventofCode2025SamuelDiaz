package dia9.b;

import java.util.List;
import java.util.stream.IntStream;

/**
 * Representa el polígono simple formado por la secuencia de vértices leída en
 * el problema del día 9 (parte b). Permite comprobar si un punto o un rectángulo
 * completo están contenidos dentro del polígono, usando el algoritmo de ray casting
 * y comprobación de intersección de aristas.
 */
public class Polygon {
    private final List<Position> vertices;

    /**
     * Constructor privado que guarda la lista de vértices ya simplificada
     * (sin vértices colineales redundantes).
     *
     * @param vertices lista de vértices del polígono.
     */
    private Polygon(List<Position> vertices) {
        this.vertices = vertices;
    }

    /**
     * Crea un polígono a partir de una lista de posiciones, simplificando
     * previamente los vértices colineales.
     *
     * @param positionList lista de posiciones (vértices) en orden.
     * @return una nueva instancia de Polygon.
     */
    public static Polygon create(List<Position> positionList) {
        return new Polygon(simplify(positionList));
    }

    /**
     * Comprueba si un rectángulo está completamente contenido dentro del polígono:
     * todas sus esquinas deben estar dentro (o en el borde) y ninguna arista del
     * polígono debe atravesar su interior.
     *
     * @param rectangle el rectángulo a comprobar.
     * @return true si el rectángulo está totalmente contenido en el polígono.
     */
    public boolean contains(Rectangle rectangle) {
        return rectangle.corners().stream().allMatch(this::contains)
                && !anyEdgeCutsThrough(rectangle);
    }

    /**
     * Comprueba si alguna arista del polígono atraviesa (corta) el interior
     * del rectángulo dado.
     *
     * @param rectangle el rectángulo a comprobar.
     * @return true si alguna arista corta el rectángulo.
     */
    private boolean anyEdgeCutsThrough(Rectangle rectangle) {
        return indices().anyMatch(i -> edgeCutsThrough(i, rectangle));
    }

    /**
     * Comprueba si la arista en el índice dado corta el rectángulo.
     *
     * @param index índice de la arista (definida por el vértice en index y el siguiente).
     * @param rectangle el rectángulo a comprobar.
     * @return true si la arista corta el rectángulo.
     */
    private boolean edgeCutsThrough(int index, Rectangle rectangle) {
        return edgeAt(index)
                .map(edge -> edge.cutsThrough(rectangle))
                .orElse(false);
    }

    /**
     * Comprueba si un punto está dentro del polígono (incluyendo el borde),
     * usando el algoritmo de ray casting: se lanza un rayo horizontal desde
     * el punto y se cuenta cuántas aristas cruza; si el número es impar, el
     * punto está dentro.
     *
     * @param point el punto a comprobar.
     * @return true si el punto está dentro (o sobre el borde) del polígono.
     */
    public boolean contains(Position point) {
        return isOnBoundary(point) || indices()
                // Cuenta cuántas aristas son cruzadas por el rayo horizontal desde el punto
                .filter(i -> crossesEdge(point, i))
                .count() % 2 == 1;
    }

    /**
     * Comprueba si el punto está exactamente sobre alguna arista del borde
     * del polígono.
     *
     * @param point el punto a comprobar.
     * @return true si el punto está en el borde.
     */
    private boolean isOnBoundary(Position point) {
        return indices().anyMatch(i -> isOnEdge(point, i));
    }

    /**
     * Comprueba si el punto está sobre la arista en el índice dado.
     *
     * @param point el punto a comprobar.
     * @param index índice de la arista.
     * @return true si el punto pertenece a esa arista.
     */
    private boolean isOnEdge(Position point, int index) {
        return edgeAt(index)
                .map(edge -> edge.contains(point))
                .orElse(false);
    }

    /**
     * Comprueba si un rayo horizontal lanzado desde el punto cruza la arista
     * en el índice dado (usado por el algoritmo de ray casting).
     *
     * @param point el punto desde el que se lanza el rayo.
     * @param index índice de la arista.
     * @return true si el rayo cruza esa arista.
     */
    private boolean crossesEdge(Position point, int index) {
        return edgeAt(index)
                .map(edge -> edge.rayCrosses(point))
                .orElse(false);
    }

    /**
     * Construye la arista formada por el vértice en el índice dado y el
     * siguiente vértice (con envoltura circular al llegar al final de la lista).
     *
     * @param index índice del vértice inicial de la arista.
     * @return un Optional con la arista correspondiente.
     */
    private java.util.Optional<Edge> edgeAt(int index) {
        // (index + 1) % size permite volver al primer vértice al llegar al final,
        // cerrando así el polígono
        return java.util.Optional.of(
                new Edge(vertices.get(index), vertices.get((index + 1) % vertices.size()))
        );
    }

    /**
     * Genera un stream con todos los índices válidos de vértices del polígono.
     *
     * @return stream de índices de 0 a vertices.size() - 1.
     */
    private IntStream indices() {
        return IntStream.range(0, vertices.size());
    }

    /**
     * Simplifica la lista de vértices eliminando aquellos que son colineales
     * con sus vecinos (no aportan un cambio de dirección real al polígono).
     *
     * @param positions lista original de vértices.
     * @return lista de vértices simplificada (sin colineales redundantes).
     */
    private static List<Position> simplify(List<Position> positions) {
        return positions.size() <= 3 ? positions :
                IntStream.range(0, positions.size())
                        // Se conserva un vértice solo si forma un giro real (producto vectorial != 0)
                        .filter(i -> crossProduct(positions, i) != 0)
                        .mapToObj(positions::get)
                        .toList();
    }

    /**
     * Calcula el producto vectorial (cross product) entre los segmentos que
     * conectan el vértice anterior, el actual y el siguiente (con envoltura
     * circular), usado para determinar si el vértice actual es colineal.
     *
     * @param positions lista de vértices.
     * @param index índice del vértice actual.
     * @return el valor del producto vectorial (0 si los tres puntos son colineales).
     */
    private static long crossProduct(List<Position> positions, int index) {
        return calculateCrossProduct(
                // Math.floorMod asegura un índice válido y positivo incluso para index - 1 = -1
                positions.get(Math.floorMod(index - 1, positions.size())),
                positions.get(index),
                positions.get((index + 1) % positions.size())
        );
    }

    /**
     * Calcula el producto vectorial (cross product) de los vectores p1->p2 y p2->p3.
     * Un resultado de 0 indica que los tres puntos son colineales.
     *
     * @param p1 punto anterior.
     * @param p2 punto actual.
     * @param p3 punto siguiente.
     * @return el valor del producto vectorial.
     */
    private static long calculateCrossProduct(Position p1, Position p2, Position p3) {
        return (long) (p2.x() - p1.x()) * (p3.y() - p2.y()) -
                (long) (p2.y() - p1.y()) * (p3.x() - p2.x());
    }


}
