package dia9.b;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Representa un rectángulo definido por dos esquinas opuestas (pos1 y pos2)
 * en el problema del día 9 (parte b). Añade utilidades para obtener sus cuatro
 * esquinas y sus límites, necesarias para comprobar si está contenido en el polígono.
 */
public record Rectangle(Position pos1, Position pos2) {

    /**
     * Calcula el área del rectángulo, incluyendo ambos bordes.
     *
     * @return el área del rectángulo.
     */
    public long area() {
        // +1 en cada dimensión porque ambos extremos forman parte del rectángulo
        return (long) (Math.abs(pos1.x() - pos2.x()) + 1) * (Math.abs((pos1.y() - pos2.y())) + 1);
    }

    /**
     * Calcula las cuatro esquinas del rectángulo a partir de las dos esquinas
     * opuestas dadas (se completan las otras dos combinando las coordenadas).
     *
     * @return lista con las cuatro esquinas del rectángulo.
     */
    public List<Position> corners() {
        return List.of(
                pos1,
                pos2,
                new Position(pos1.x(), pos2.y()),
                new Position(pos2.x(), pos1.y())
        );
    }

    /**
     * Representación textual del rectángulo, incluyendo sus esquinas y área.
     *
     * @return cadena descriptiva del rectángulo.
     */
    @Override
    public String toString() {
        return "Rectangle{" + "pos1=" + pos1 + ", pos2=" + pos2 + ", area=" + area() + '}';
    }

    /**
     * Genera una representación ASCII del rectángulo, marcando el borde con '#'
     * y el interior con '.'. Útil para depuración visual.
     *
     * @return cadena multilínea representando el rectángulo dibujado.
     */
    public String draw() {
        int minX = Math.min(pos1.x(), pos2.x());
        int maxX = Math.max(pos1.x(), pos2.x());
        int minY = Math.min(pos1.y(), pos2.y());
        int maxY = Math.max(pos1.y(), pos2.y());

        // Recorre cada fila (y) y dentro cada columna (x), dibujando '#' si es borde
        return IntStream.rangeClosed(minY, maxY)
                .mapToObj(y -> IntStream.rangeClosed(minX, maxX)
                        .mapToObj(x -> isBorder(x, y, minX, maxX, minY, maxY) ? "#" : ".")
                        .collect(Collectors.joining()))
                .collect(Collectors.joining("\n"));
    }

    /**
     * Determina si la celda (x, y) pertenece al borde del rectángulo definido
     * por los límites dados.
     *
     * @return true si la celda está en el borde (primera/última fila o columna).
     */
    private boolean isBorder(int x, int y, int minX, int maxX, int minY, int maxY) {
        return y == minY || y == maxY || x == minX || x == maxX;
    }

    /** @return la menor coordenada x entre las dos esquinas del rectángulo. */
    int minX() {
        return Math.min(pos1.x(), pos2.x());
    }

    /** @return la mayor coordenada x entre las dos esquinas del rectángulo. */
    int maxX() {
        return Math.max(pos1.x(), pos2.x());
    }

    /** @return la menor coordenada y entre las dos esquinas del rectángulo. */
    int minY() {
        return Math.min(pos1.y(), pos2.y());
    }

    /** @return la mayor coordenada y entre las dos esquinas del rectángulo. */
    int maxY() {
        return Math.max(pos1.y(), pos2.y());
    }
}
