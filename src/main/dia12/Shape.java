package dia12;

import java.util.BitSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Representa una forma (pieza de regalo) del día 12 como un conjunto de
 * celdas ocupadas ({@link Position}). Permite calcular sus dimensiones,
 * generar todas sus orientaciones posibles (rotaciones y reflejos) y
 * convertirla en una máscara de bits para comprobar solapamientos de forma
 * eficiente al intentar encajarla en una región.
 */
public record Shape(Set<Position> cells) {

    /**
     * Crea una forma a partir de su representación en líneas de texto,
     * donde el carácter '#' indica una celda ocupada.
     *
     * @param lines líneas de texto que dibujan la forma.
     * @return una nueva instancia de Shape.
     */
    public static Shape create(List<String> lines) {
        return new Shape(parseCells(lines));
    }

    /**
     * Extrae las posiciones de las celdas marcadas con '#' en las líneas dadas.
     *
     * @param lines líneas de texto que dibujan la forma.
     * @return conjunto de posiciones ocupadas.
     */
    private static Set<Position> parseCells(List<String> lines) {
        return IntStream.range(0, lines.size())
                .boxed()
                // Para cada fila (y), se buscan las columnas (x) que contienen '#'
                .flatMap(y -> IntStream.range(0, lines.get(y).length())
                        .filter(x -> lines.get(y).charAt(x) == '#')
                        .mapToObj(x -> new Position(x, y)))
                .collect(Collectors.toSet());
    }

    /**
     * Calcula el ancho de la forma (coordenada x máxima + 1).
     *
     * @return el ancho de la forma.
     */
    public int width() {
        int max = -1;
        // Recorre todas las celdas buscando la mayor coordenada x
        for (Position p : cells) {
            if (p.x() > max) max = p.x();
        }
        return max + 1;
    }

    /**
     * Calcula el alto de la forma (coordenada y máxima + 1).
     *
     * @return el alto de la forma.
     */
    public int height() {
        int max = -1;
        // Recorre todas las celdas buscando la mayor coordenada y
        for (Position p : cells) {
            if (p.y() > max) max = p.y();
        }
        return max + 1;
    }

    /**
     * @return el número de celdas ocupadas (área) de la forma.
     */
    public int area() {
        return cells.size();
    }

    /**
     * Genera el conjunto de todas las orientaciones distintas de esta forma:
     * las 4 rotaciones de 90 grados, cada una con su versión reflejada
     * horizontalmente, normalizadas y sin duplicados (algunas formas son
     * simétricas y producen la misma orientación tras rotar/reflejar).
     *
     * @return conjunto de formas equivalentes bajo rotación y reflejo.
     */
    public Set<Shape> allOrientations() {
        // Genera las 4 rotaciones sucesivas de 90 grados
        return Stream.iterate(this, Shape::rotate90)
                .limit(4)
                // Por cada rotación, añade también su versión reflejada horizontalmente
                .flatMap(s -> Stream.of(s, s.flipHorizontal()))
                // Normaliza cada variante para que sus coordenadas empiecen en (0,0)
                .map(Shape::normalized)
                // El Set elimina automáticamente las orientaciones duplicadas (formas simétricas)
                .collect(Collectors.toSet());
    }

    /**
     * Genera una nueva forma rotada 90 grados en sentido horario, aplicando
     * la transformación (x, y) -> (-y, x) a cada celda.
     *
     * @return la forma rotada.
     */
    private Shape rotate90() {
        return new Shape(cells.stream()
                .map(p -> new Position(-p.y(), p.x()))
                .collect(Collectors.toSet()));
    }

    /**
     * Genera una nueva forma reflejada horizontalmente, invirtiendo el signo
     * de la coordenada x de cada celda.
     *
     * @return la forma reflejada.
     */
    private Shape flipHorizontal() {
        return new Shape(cells.stream()
                .map(p -> new Position(-p.x(), p.y()))
                .collect(Collectors.toSet()));
    }

    /**
     * Traslada todas las celdas de la forma para que la coordenada mínima en
     * x e y sea 0, de modo que dos formas equivalentes (tras rotar/reflejar)
     * queden representadas de forma idéntica y comparable.
     *
     * @return la forma normalizada (con esquina superior izquierda en el origen).
     */
    private Shape normalized() {
        int minX = cells.stream().mapToInt(Position::x).min().orElse(0);
        int minY = cells.stream().mapToInt(Position::y).min().orElse(0);
        return new Shape(cells.stream()
                .map(p -> new Position(p.x() - minX, p.y() - minY))
                .collect(Collectors.toSet()));
    }

    /**
     * Convierte la forma en una máscara de bits ({@link BitSet}) que representa
     * las celdas que ocuparía si se colocara en la posición (offsetX, offsetY)
     * dentro de una región de ancho {@code regionWidth}. Cada celda de la región
     * se representa como un único bit (índice = fila * ancho + columna).
     *
     * @param offsetX desplazamiento horizontal donde se coloca la forma.
     * @param offsetY desplazamiento vertical donde se coloca la forma.
     * @param regionWidth ancho de la región (para calcular el índice lineal de cada celda).
     * @return el BitSet con los bits activados correspondientes a las celdas ocupadas.
     */
    public BitSet toBitmask(int offsetX, int offsetY, int regionWidth) {
        BitSet mask = new BitSet(regionWidth * (offsetY + height()));
        for (Position p : cells) {
            // Convierte la posición 2D (fila, columna) en un índice lineal dentro de la región
            int idx = (p.y() + offsetY) * regionWidth + (p.x() + offsetX);
            mask.set(idx);
        }
        return mask;
    }

    /**
     * Igual que {@link #toBitmask(int, int, int)} pero usando un {@code long}
     * como máscara de bits en lugar de un BitSet, mucho más rápido para
     * regiones pequeñas (hasta 64 celdas) ya que cabe en una sola palabra.
     *
     * @param offsetX desplazamiento horizontal donde se coloca la forma.
     * @param offsetY desplazamiento vertical donde se coloca la forma.
     * @param regionWidth ancho de la región (para calcular el índice de bit de cada celda).
     * @return la máscara de bits (long) con los bits activados correspondientes a las celdas ocupadas.
     */
    public long toLongMask(int offsetX, int offsetY, int regionWidth) {
        return cells.stream()
                // Cada celda se convierte en un bit activado (1L << índice) y se combinan con OR
                .mapToLong(p -> 1L << ((long) (p.y() + offsetY) * regionWidth + (p.x() + offsetX)))
                .reduce(0L, (a, b) -> a | b);
    }
}
