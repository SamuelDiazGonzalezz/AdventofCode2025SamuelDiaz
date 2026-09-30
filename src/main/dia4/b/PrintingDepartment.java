package dia4.b;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Representa el departamento de impresión del problema del día 4, parte B:
 * una cuadrícula de rollos de papel donde, a diferencia de la parte A,
 * los rollos "accesibles" (con menos de {@link #MAX_ADJACENT_PAPER_ROLLS}
 * vecinos) se van eliminando iterativamente, recalculando las adyacencias
 * en cada paso, hasta que ya no queda ninguno accesible.
 */
public class PrintingDepartment {
    private final PaperRoll[][] paperRolls;
    private final int MAX_ADJACENT_PAPER_ROLLS = 4;
    private int deletedPaperRolls = 0;

    /**
     * Crea el departamento de impresión con una cuadrícula del tamaño
     * indicado por {@code maxPosition}.
     *
     * @param maxPosition posición máxima que define el tamaño de la
     *                     cuadrícula (columnas y filas).
     */
    public PrintingDepartment(Position maxPosition) {
        paperRolls = new PaperRoll[maxPosition.col()][maxPosition.row()];
    }

    /**
     * Crea una nueva instancia de {@link PrintingDepartment}.
     *
     * @param maxPosition posición máxima que define el tamaño de la cuadrícula.
     * @return un nuevo departamento de impresión.
     */
    public static PrintingDepartment create(Position maxPosition) {
        return new PrintingDepartment(maxPosition);
    }

    /**
     * Coloca un nuevo rollo de papel en la posición indicada.
     *
     * @param position posición donde colocar el rollo.
     */
    public void addNode(Position position) {
        paperRolls[position.row()][position.col()] = new PaperRoll(position);
    }

    /**
     * Procesa una fila de texto de la entrada, añadiendo un rollo de
     * papel en cada posición donde aparezca el carácter '@'.
     *
     * @param y índice de la fila a procesar.
     * @param row contenido de texto de la fila.
     * @return el propio departamento, para poder encadenar llamadas.
     */
    public PrintingDepartment addRow(int y, String row) {
        IntStream.range(0, row.length())
                .filter(i -> row.charAt(i) == '@') // solo nos interesan las posiciones marcadas con '@'
                .forEach(idx -> addNode(new Position(idx, y))); // se coloca un rollo de papel en esa posición
        return this;
    }

    /**
     * Procesa todas las filas de texto de la entrada, añadiendo los
     * rollos de papel correspondientes.
     *
     * @param rows array con todas las filas de texto de la entrada.
     * @return el propio departamento, para poder encadenar llamadas.
     */
    public PrintingDepartment addRows(String[] rows) {
        IntStream.range(0, rows.length).forEach(i -> addRow(i, rows[i].trim()));
        return this;
    }

    /**
     * Procesa la cuadrícula de forma iterativa: calcula las adyacencias,
     * elimina los rollos accesibles, y repite mientras sigan quedando
     * rollos accesibles por eliminar.
     *
     * @return el propio departamento (tras la última iteración realizada).
     */
    public PrintingDepartment process() {
        return Stream.iterate(
                this.processOne(), // primer cálculo de adyacencias
                dept -> dept.accesibleRollsCount() >= 1, // continúa mientras haya al menos un rollo accesible
                dept -> dept.removeAdjacents().processOne() // elimina los accesibles y recalcula las adyacencias
        ).reduce((first, last) -> last).orElse(this); // se queda con el último estado generado
    }

    /**
     * Elimina de la cuadrícula todos los rollos de papel que actualmente
     * son accesibles, contabilizándolos.
     *
     * @return el propio departamento, para poder encadenar llamadas.
     */
    private PrintingDepartment removeAdjacents() {
        getAllRolls()
                .filter(this::isAccessible) // solo se eliminan los rollos accesibles
                .forEach(paperRoll -> {
                    paperRolls[paperRoll.row()][paperRoll.col()] = null; // se elimina el rollo de la cuadrícula
                    deletedPaperRolls++; // se contabiliza como eliminado
                }
        );
        return this;
    }

    /**
     * Recalcula, para cada rollo de papel existente en la cuadrícula, la
     * lista de rollos adyacentes (vaciando antes la lista previa).
     *
     * @return el propio departamento, para poder encadenar llamadas.
     */
    private PrintingDepartment processOne() {
        IntStream.range(0, paperRolls.length)
                .forEach(i -> IntStream.range(0, paperRolls[i].length)
                        .filter(j -> paperRolls[i][j] != null) // solo procesamos posiciones que tienen un rollo de papel
                        .forEach(j -> paperRolls[i][j].clear().addAll(getAdjacentRolls(new Position(i, j)))) // se limpia y se recalculan los vecinos actuales
                );
        return this;
    }

    /**
     * Obtiene la lista de rollos de papel adyacentes (en las 8 direcciones)
     * a una posición dada, descartando los huecos vacíos y los fuera de rango.
     *
     * @param pos posición de referencia.
     * @return la lista de rollos de papel vecinos existentes.
     */
    private List<PaperRoll> getAdjacentRolls(Position pos) {
        return IntStream.rangeClosed(pos.row() - 1, pos.row() + 1) // recorre las filas vecinas (arriba, misma, abajo)
                .boxed()
                .flatMap(i -> IntStream.rangeClosed(pos.col() - 1, pos.col() + 1) // recorre las columnas vecinas (izquierda, misma, derecha)
                        .mapToObj(j -> new Position(i, j))
                )
                .filter(neighbor -> isValidNeighbor(neighbor, pos)) // descarta la propia posición y las que están fuera de la cuadrícula
                .map(this::getRollAt) // obtiene el rollo de papel (o null) en cada posición vecina
                .filter(Objects::nonNull) // descarta las posiciones vacías (sin rollo)
                .toList();
    }

    /**
     * Comprueba si una posición vecina es válida: distinta de la posición
     * actual y dentro de los límites de la cuadrícula.
     *
     * @param neighbor posición vecina a comprobar.
     * @param current posición actual (de referencia).
     * @return {@code true} si la posición vecina es válida.
     */
    private boolean isValidNeighbor(Position neighbor, Position current) {
        return !neighbor.equals(current) && isInBounds(neighbor);
    }

    /**
     * Comprueba si una posición está dentro de los límites de la cuadrícula.
     *
     * @param pos posición a comprobar.
     * @return {@code true} si la posición está dentro de la cuadrícula.
     */
    private boolean isInBounds(Position pos) {
        return pos.row() >= 0 && pos.row() < paperRolls.length &&
                pos.col() >= 0 && pos.col() < paperRolls[0].length;
    }

    /**
     * Obtiene el rollo de papel almacenado en una posición concreta.
     *
     * @param pos posición a consultar.
     * @return el rollo de papel en esa posición, o {@code null} si no hay ninguno.
     */
    private PaperRoll getRollAt(Position pos) {
        return paperRolls[pos.row()][pos.col()];
    }

    /**
     * Devuelve un flujo con todos los rollos de papel existentes en la
     * cuadrícula (descartando los huecos vacíos).
     *
     * @return un {@link Stream} con todos los rollos de papel presentes.
     */
    private Stream<PaperRoll> getAllRolls() {
        return Arrays.stream(paperRolls)
                .flatMap(Arrays::stream) // aplana la matriz 2D en un único flujo
                .filter(Objects::nonNull); // descarta las posiciones vacías
    }

    /**
     * Comprueba si un rollo de papel es "accesible", es decir, si tiene
     * menos de {@link #MAX_ADJACENT_PAPER_ROLLS} rollos adyacentes.
     *
     * @param roll rollo de papel a comprobar.
     * @return {@code true} si el rollo es accesible.
     */
    private boolean isAccessible(PaperRoll roll) {
        return roll.size() < MAX_ADJACENT_PAPER_ROLLS;
    }

    /**
     * Cuenta cuántos rollos de papel de toda la cuadrícula son accesibles
     * en el estado actual.
     *
     * @return el número de rollos accesibles.
     */
    public long accesibleRollsCount() {
        return getAllRolls()
                .filter(this::isAccessible)
                .count();
    }

    /**
     * Imprime por consola una representación visual de la cuadrícula:
     * '.' para huecos vacíos, 'x' para rollos accesibles y '@' para
     * rollos no accesibles (rodeados de demasiados vecinos).
     *
     * @return el propio departamento, para poder encadenar llamadas.
     */
    public PrintingDepartment print() {
        for (int y = 0; y < paperRolls[0].length; y++) { // recorre cada fila
            for (PaperRoll[] paperRoll : paperRolls) { // recorre cada columna de esa fila
                PaperRoll roll = paperRoll[y];
                if (roll == null) {
                    System.out.print("."); // no hay rollo de papel en esa posición
                } else {
                    System.out.print(roll.size() < MAX_ADJACENT_PAPER_ROLLS ? "x" : "@"); // 'x' si es accesible, '@' si no lo es
                }
            }
            System.out.println();
        }
        return this;
    }

    /**
     * Devuelve el número total de rollos de papel eliminados durante
     * todo el proceso iterativo.
     *
     * @return el número de rollos de papel eliminados.
     */
    public int removedPaperRolls() {
        return deletedPaperRolls;
    }
}
