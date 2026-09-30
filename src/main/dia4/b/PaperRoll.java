package dia4.b;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * Representa un rollo de papel dentro de la cuadrícula del problema del
 * día 4, parte B. A diferencia de la parte A, cada rollo conoce también
 * su propia {@link Position} dentro de la cuadrícula, lo que permite
 * eliminarlo directamente de la matriz cuando deja de ser accesible.
 */
public class PaperRoll {
    private final List<PaperRoll> adjacentPaperRolls;
    private final Position position;

    /**
     * Crea un nuevo rollo de papel en la posición indicada, sin rollos
     * adyacentes.
     *
     * @param position posición del rollo dentro de la cuadrícula.
     */
    public PaperRoll(Position position) {
        this.adjacentPaperRolls = new ArrayList<>();
        this.position = position;
    }

    /**
     * Devuelve la columna de la posición de este rollo.
     *
     * @return la columna del rollo.
     */
    public int col() {
        return position.col();
    }

    /**
     * Devuelve la fila de la posición de este rollo.
     *
     * @return la fila del rollo.
     */
    public int row() {
        return position.row();
    }

    /**
     * Devuelve el número de rollos de papel adyacentes a este.
     *
     * @return el número de vecinos.
     */
    public int size() {
        return adjacentPaperRolls.size();
    }

    /**
     * Añade un rollo de papel a la lista de adyacentes.
     *
     * @param paperRoll el rollo adyacente a añadir.
     * @return el propio rollo, para poder encadenar llamadas.
     */
    public PaperRoll add(PaperRoll paperRoll) {
        adjacentPaperRolls.add(paperRoll);
        return this;
    }

    /**
     * Vacía la lista de rollos adyacentes (se usa para recalcular las
     * adyacencias en cada iteración del proceso de eliminación).
     *
     * @return el propio rollo, para poder encadenar llamadas.
     */
    public PaperRoll clear() {
        adjacentPaperRolls.clear();
        return this;
    }

    /**
     * Elimina un elemento de la lista de rollos adyacentes.
     *
     * @param o el objeto a eliminar.
     * @return el propio rollo, para poder encadenar llamadas.
     */
    public PaperRoll remove(Object o) {
        adjacentPaperRolls.remove(o);
        return this;
    }

    /**
     * Añade una colección completa de rollos adyacentes.
     *
     * @param c colección de rollos a añadir.
     * @return el propio rollo, para poder encadenar llamadas.
     */
    public PaperRoll addAll(Collection<? extends PaperRoll> c) {
        adjacentPaperRolls.addAll(c);
        return this;
    }

    /**
     * Añade una colección de rollos adyacentes en una posición concreta
     * de la lista interna.
     *
     * @param index posición en la que insertar los elementos.
     * @param c colección de rollos a añadir.
     * @return el propio rollo, para poder encadenar llamadas.
     */
    public PaperRoll addAll(int index, Collection<? extends PaperRoll> c) {
        adjacentPaperRolls.addAll(index, c);
        return this;
    }

    /**
     * Elimina de la lista de adyacentes todos los elementos presentes
     * en la colección dada.
     *
     * @param c colección de elementos a eliminar.
     * @return el propio rollo, para poder encadenar llamadas.
     */
    public PaperRoll removeAll(Collection<?> c) {
        adjacentPaperRolls.removeAll(c);
        return this;
    }

    /**
     * Ejecuta una acción sobre cada rollo de papel adyacente.
     *
     * @param action acción a ejecutar sobre cada vecino.
     * @return el propio rollo, para poder encadenar llamadas.
     */
    public PaperRoll forEach(Consumer<? super PaperRoll> action) {
        adjacentPaperRolls.forEach(action);
        return this;
    }

    /**
     * Devuelve un flujo secuencial con los rollos adyacentes.
     *
     * @return un {@link Stream} de los rollos adyacentes.
     */
    public Stream<PaperRoll> stream() {
        return adjacentPaperRolls.stream();
    }

    /**
     * Devuelve un flujo paralelo con los rollos adyacentes.
     *
     * @return un {@link Stream} paralelo de los rollos adyacentes.
     */
    public Stream<PaperRoll> parallelStream() {
        return adjacentPaperRolls.parallelStream();
    }
}
