package dia7.b;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Representa el estado de un haz ("beam") en una fila concreta de la cuadrícula del día 7
 * (parte b): el conjunto de columnas por las que pasa, cuántos caminos ("timelines")
 * distintos llegan a cada posición, y el número acumulado de divisiones ('^') sufridas.
 * A diferencia de la parte a, aquí se lleva la cuenta de cuántos caminos confluyen en cada
 * posición (en vez de solo si está ocupada o no), para poder calcular el número total de
 * caminos posibles al final de la simulación.
 */
public class Beam {
    private final int row;
    private final int maxCol;
    private final PositionList positionList;
    // Mapa que asocia cada posición con el número de caminos ("timelines") que llegan a ella.
    private final Map<Position, Long> pathCounts;
    private long splitCount = 0;

    /**
     * Constructor privado: crea un haz vacío para una fila y un ancho máximo de columna dados.
     *
     * @param row        fila del haz
     * @param maxCol     columna máxima permitida
     * @param splitCount número de divisiones acumuladas (sin usar directamente, se fija luego con {@link #setSplit(long)})
     */
    private Beam(int row, int maxCol, long splitCount) {
        this.positionList = new PositionList();
        this.pathCounts = new HashMap<>();
        this.row = row;
        this.maxCol = maxCol;
    }

    /**
     * Crea un nuevo haz vacío para la fila y el ancho máximo indicados.
     *
     * @param row    fila del haz
     * @param maxCol columna máxima permitida
     * @return un nuevo {@link Beam}
     */
    public static Beam create(int row, int maxCol) {
        return new Beam(row, maxCol, 0);
    }

    /**
     * Añade varias posiciones al haz, descartando las que se salgan del rango de columnas válido.
     *
     * @param positionList posiciones a añadir
     * @return la propia instancia, para encadenar llamadas
     */
    public Beam addAll(List<Position> positionList){
        // Solo se conservan las posiciones cuya columna está dentro de los límites [0, maxCol].
        this.positionList.addAll(positionList
                .stream()
                .filter(p -> p.col() <= maxCol && p.col() >= 0)
                .toList()
        );
        return this;
    }

    /**
     * Añade una única posición al haz (con un único camino inicial), si su columna está
     * dentro del rango válido.
     *
     * @param position posición a añadir
     * @return la propia instancia, para encadenar llamadas
     */
    public Beam add(Position position){
        // Se ignoran las posiciones fuera de los límites de columna.
        if (position.col() > maxCol || position.col() < 0) return this;
        this.positionList.add(position);
        // La posición inicial cuenta con un único camino que llega a ella.
        this.pathCounts.put(position, 1L);
        return this;
    }

    /**
     * Calcula el haz correspondiente a la siguiente fila, propagando cada columna ocupada
     * en la fila actual junto con el número de caminos que llegan a ella.
     *
     * @param nextLine texto de la siguiente fila de la cuadrícula
     * @return el nuevo {@link Beam} correspondiente a la fila siguiente
     */
    public Beam next(String nextLine) {
        // Se seleccionan las columnas de la siguiente línea que coinciden con alguna posición ocupada en la fila actual.
        return createBeam(IntStream.range(0, nextLine.length())
                .filter(i -> positionList.positionExists(this.row, i)).boxed().toList(), nextLine
        );
    }

    /**
     * Establece el contador de divisiones acumuladas del haz.
     *
     * @param splitCount número de divisiones a asignar
     * @return la propia instancia, para encadenar llamadas
     */
    public Beam setSplit(long splitCount) {
        this.splitCount = splitCount;
        return this;
    }

    /**
     * Construye el haz de la siguiente fila: genera las nuevas posiciones y sus caminos
     * asociados a partir de las columnas propagables, agrupando y sumando los caminos que
     * confluyen en la misma posición, y actualiza el contador de divisiones.
     *
     * @param columnList columnas de la fila actual que se propagan a la siguiente
     * @param nextLine   texto de la siguiente línea de la cuadrícula
     * @return el nuevo {@link Beam} de la fila siguiente
     */
    private Beam createBeam(List<Integer> columnList, String nextLine) {
        return Beam.create(this.row+1, this.maxCol)
                .setPathCounts(columnList.stream()
                        // Por cada columna se generan las nuevas posiciones (una o dos, según si hay división) con sus caminos.
                        .flatMap(i -> generateNextPositions(i, nextLine))
                        // Se agrupan por posición, sumando los caminos que llegan a la misma posición desde distintas columnas.
                        .collect(Collectors.groupingBy(
                                Map.Entry::getKey,
                                Collectors.summingLong(Map.Entry::getValue)
                        )))
                .setSplit(this.splitCount + countNewSplits(columnList, nextLine));
    }

    /**
     * Genera las entradas (posición -> número de caminos) resultantes de propagar la columna
     * indicada a la siguiente fila, dividiendo el camino en dos si hay un splitter ('^').
     *
     * @param col      columna actual a propagar
     * @param nextLine texto de la siguiente línea de la cuadrícula
     * @return flujo de entradas posición-caminos generadas a partir de esta columna
     */
    private Stream<Map.Entry<Position, Long>> generateNextPositions(int col, String nextLine) {
        Position currentPos = new Position(this.row, col);
        // Número de caminos que llegan actualmente a esta posición.
        long currentPaths = this.pathCounts.getOrDefault(currentPos, 0L);

        // Si la siguiente fila tiene un splitter en esta columna, el camino se divide en dos; si no, sigue recto.
        return isSplitter(nextLine, col) ?
                createSplitPositions(col, currentPaths) :
                createStraightPosition(col, currentPaths);
    }

    /**
     * Comprueba si el carácter en la columna indicada de la línea es un divisor de haz ('^').
     *
     * @param line línea de la cuadrícula
     * @param col  columna a comprobar
     * @return true si en esa columna hay un divisor
     */
    private boolean isSplitter(String line, int col) {
        return line.charAt(col) == '^';
    }

    /**
     * Genera las dos posiciones resultantes de una división (izquierda y derecha), cada una
     * heredando el mismo número de caminos que tenía la posición original.
     *
     * @param col   columna en la que se produce la división
     * @param paths número de caminos a propagar a cada nueva posición
     * @return flujo con las dos entradas posición-caminos generadas
     */
    private Stream<Map.Entry<Position, Long>> createSplitPositions(int col, long paths) {
        return Stream.of(
                Map.entry(new Position(this.row+1, col-1), paths),
                Map.entry(new Position(this.row+1, col+1), paths)
        );
    }

    /**
     * Genera la posición resultante de avanzar en línea recta (sin división), heredando
     * el mismo número de caminos.
     *
     * @param col   columna actual (se mantiene en la fila siguiente)
     * @param paths número de caminos a propagar
     * @return flujo con la única entrada posición-caminos generada
     */
    private Stream<Map.Entry<Position, Long>> createStraightPosition(int col, long paths) {
        return Stream.of(Map.entry(new Position(this.row+1, col), paths));
    }

    /**
     * Cuenta cuántas de las columnas indicadas corresponden a un divisor ('^') en la siguiente línea,
     * es decir, cuántas nuevas divisiones se producen al avanzar a la siguiente fila.
     *
     * @param columns  columnas propagadas desde la fila actual
     * @param nextLine texto de la siguiente línea de la cuadrícula
     * @return número de nuevas divisiones producidas
     */
    private long countNewSplits(List<Integer> columns, String nextLine) {
        return columns.stream()
                .filter(col -> isSplitter(nextLine, col))
                .count();
    }

    /**
     * Asigna el mapa de caminos por posición al haz y sincroniza el conjunto de posiciones ocupadas
     * con las claves de dicho mapa.
     *
     * @param pathCounts mapa de posición a número de caminos que llegan a ella
     * @return la propia instancia, para encadenar llamadas
     */
    private Beam setPathCounts(Map<Position, Long> pathCounts) {
        this.pathCounts.putAll(pathCounts);
        // Se añaden también las posiciones al conjunto de posiciones ocupadas del haz.
        this.positionList.addAll(new ArrayList<>(pathCounts.keySet()));
        return this;
    }

    /**
     * Calcula el número total de caminos ("timelines") distintos que llegan a este haz,
     * sumando los caminos de todas sus posiciones.
     *
     * @return número total de caminos acumulados en esta fila
     */
    public long totalPaths() {
        return pathCounts.values().stream()
                .mapToLong(Long::longValue)
                .sum();
    }

    /**
     * Devuelve el número de posiciones distintas ocupadas por el haz en su fila.
     *
     * @return cantidad de posiciones del haz
     */
    public long count() {
        return positionList.size();
    }

    /**
     * Devuelve el número acumulado de divisiones sufridas por el haz hasta esta fila.
     *
     * @return número de divisiones
     */
    public long splits() {
        return splitCount;
    }

    /**
     * Representación en texto del haz, mostrando sus posiciones y el número de divisiones.
     *
     * @return cadena descriptiva del estado del haz
     */
    @Override
    public String toString() {
        return positionList + ", splits: "+ splitCount;
    }
}
