package dia9.b;

/**
 * Representa una arista (segmento) del polígono del día 9 (parte b), definida
 * por dos posiciones consecutivas (start y end). Se usa para determinar si un
 * rectángulo queda dentro del polígono, si un punto está sobre el borde, o si
 * un rayo horizontal/vertical lo cruza (algoritmo de ray casting).
 */
public record Edge(Position start, Position end) {

    /**
     * Comprueba si esta arista atraviesa (corta) el interior de un rectángulo,
     * distinguiendo si la arista es vertical u horizontal.
     *
     * @param rectangle el rectángulo a comprobar.
     * @return true si la arista corta el rectángulo.
     */
    boolean cutsThrough(Rectangle rectangle) {
        return isVertical() ? cutsThroughVertically(rectangle) : cutsThroughHorizontally(rectangle);
    }

    /**
     * Comprueba si una arista vertical atraviesa el rectángulo: su coordenada x
     * debe estar estrictamente entre los límites x del rectángulo, y su rango de
     * y debe solaparse con el rango y del rectángulo.
     *
     * @param rectangle el rectángulo a comprobar.
     * @return true si la arista vertical corta el rectángulo.
     */
    private boolean cutsThroughVertically(Rectangle rectangle) {
        return x() > rectangle.minX() && x() < rectangle.maxX() &&
                minY() < rectangle.maxY() && maxY() > rectangle.minY();
    }

    /**
     * Comprueba si una arista horizontal atraviesa el rectángulo: su coordenada y
     * debe estar estrictamente entre los límites y del rectángulo, y su rango de
     * x debe solaparse con el rango x del rectángulo.
     *
     * @param rectangle el rectángulo a comprobar.
     * @return true si la arista horizontal corta el rectángulo.
     */
    private boolean cutsThroughHorizontally(Rectangle rectangle) {
        return y() > rectangle.minY() && y() < rectangle.maxY() &&
                minX() < rectangle.maxX() && maxX() > rectangle.minX();
    }

    /**
     * Comprueba si un punto está exactamente sobre esta arista (dentro de sus
     * límites y alineado con su coordenada fija).
     *
     * @param point el punto a comprobar.
     * @return true si el punto está sobre la arista.
     */
    boolean contains(Position point) {
        return isInBounds(point) && (isVertical() ?
                point.x() == x() : point.y() == y());
    }

    /**
     * Comprueba si un punto está dentro de la caja delimitadora (bounding box)
     * de la arista.
     *
     * @param point el punto a comprobar.
     * @return true si el punto está dentro de los límites de la arista.
     */
    private boolean isInBounds(Position point) {
        return point.x() >= minX() && point.x() <= maxX() &&
                point.y() >= minY() && point.y() <= maxY();
    }

    /**
     * Determina si un rayo horizontal lanzado desde el punto hacia la derecha
     * cruza esta arista. Se usa en el algoritmo de ray casting para saber si un
     * punto está dentro de un polígono (contando el número de cruces).
     *
     * @param point el punto desde el que se lanza el rayo.
     * @return true si el rayo cruza la arista.
     */
    boolean rayCrosses(Position point) {
        return isYBetween(point) && isLeftOfIntersection(point);
    }

    /**
     * Comprueba si la coordenada y del punto está estrictamente entre las
     * coordenadas y de los extremos de la arista (uno por encima y otro por debajo).
     *
     * @param point el punto a comprobar.
     * @return true si la y del punto está entre los extremos de la arista.
     */
    private boolean isYBetween(Position point) {
        return (start.y() > point.y()) != (end.y() > point.y());
    }

    /**
     * Comprueba si el punto queda a la izquierda del punto de intersección del
     * rayo horizontal con la arista (necesario para contar correctamente los cruces).
     *
     * @param point el punto a comprobar.
     * @return true si el punto está a la izquierda de la intersección.
     */
    private boolean isLeftOfIntersection(Position point) {
        return point.x() < xIntersection(point);
    }

    /**
     * Calcula la coordenada x en la que un rayo horizontal a la altura del punto
     * dado intersecta esta arista, mediante interpolación lineal.
     *
     * @param point el punto de referencia (para su coordenada y).
     * @return la coordenada x de la intersección.
     */
    private double xIntersection(Position point) {
        // Interpolación lineal entre start y end según la proporción de y
        return start.x() + (double) (end.x() - start.x()) * (point.y() - start.y()) /
                (end.y() - start.y());
    }

    /**
     * Indica si la arista es vertical (misma coordenada x en ambos extremos).
     *
     * @return true si la arista es vertical.
     */
    private boolean isVertical() {
        return start.x() == end.x();
    }

    /** @return la coordenada x del punto de inicio de la arista. */
    private int x() {
        return start.x();
    }

    /** @return la coordenada y del punto de inicio de la arista. */
    private int y() {
        return start.y();
    }

    /** @return la menor coordenada x entre los dos extremos de la arista. */
    private int minX() {
        return Math.min(start.x(), end.x());
    }

    /** @return la mayor coordenada x entre los dos extremos de la arista. */
    private int maxX() {
        return Math.max(start.x(), end.x());
    }

    /** @return la menor coordenada y entre los dos extremos de la arista. */
    private int minY() {
        return Math.min(start.y(), end.y());
    }

    /** @return la mayor coordenada y entre los dos extremos de la arista. */
    private int maxY() {
        return Math.max(start.y(), end.y());
    }
}
