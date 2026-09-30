package dia7.a;

import java.util.Collection;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Representa el estado de un haz ("beam") en una fila concreta de la cuadrícula del día 7:
 * el conjunto de columnas por las que pasa en esa fila y el número acumulado de divisiones
 * ('^') que ha sufrido hasta llegar a ella. Cada llamada a {@link #next(String)} genera el
 * {@link Beam} correspondiente a la siguiente fila.
 */
public class Beam {
    private final int row;
    private final int maxCol;
    private final PositionList positionList;
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
     * Añade una única posición al haz, si su columna está dentro del rango válido.
     *
     * @param position posición a añadir
     * @return la propia instancia, para encadenar llamadas
     */
    public Beam add(Position position){
        // Se ignoran las posiciones fuera de los límites de columna.
        if (position.col() > maxCol || position.col() < 0) return this;
        this.positionList.add(position);
        return this;
    }

    /**
     * Calcula el haz correspondiente a la siguiente fila, propagando cada columna ocupada
     * en la fila actual: si en la siguiente línea hay un '^' en esa columna, el haz se divide
     * en dos (columna-1 y columna+1); si no, simplemente avanza a la misma columna.
     *
     * @param nextLine texto de la siguiente fila de la cuadrícula
     * @return el nuevo {@link Beam} correspondiente a la fila siguiente
     */
    public Beam next(String nextLine) {
        // Se seleccionan las columnas de la siguiente línea que coinciden con alguna posición ocupada en la fila actual.
        return createBeam(IntStream.range(0, nextLine.length())
                .filter(i -> positionList.positionExists(this.row, i)), nextLine
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
     * Construye el haz de la siguiente fila a partir de las columnas propagables: por cada
     * columna, si el carácter correspondiente en la siguiente línea es '^' se generan dos
     * posiciones (división), y en caso contrario una sola posición (avance recto).
     *
     * @param beamPoints columnas de la fila actual que se propagan a la siguiente
     * @param nextLine   texto de la siguiente línea de la cuadrícula
     * @return el nuevo {@link Beam} de la fila siguiente, con el contador de divisiones actualizado
     */
    private Beam createBeam(IntStream beamPoints, String nextLine) {
        return Beam.create(this.row+1, this.maxCol).addAll(
                beamPoints
                        // Por cada columna propagable: si hay un '^', se divide en dos posiciones; si no, avanza recto.
                        .mapToObj(i -> nextLine.charAt(i) == '^' ?
                                divideBy(i) :
                                List.of(new Position(this.row+1, i))

                )
                        // Se aplanan las listas de posiciones generadas por cada columna en una sola lista.
                        .flatMap(Collection::stream)
                        .toList()
        ).setSplit(splitCount);
    }

    /**
     * Genera las dos posiciones resultantes de dividir el haz en la columna indicada
     * (una a la izquierda y otra a la derecha) e incrementa el contador de divisiones.
     *
     * @param i columna en la que se produce la división
     * @return lista con las dos nuevas posiciones generadas
     */
    private List<Position> divideBy(int i) {
        // Cada vez que el haz se divide en dos, se incrementa el contador de divisiones.
        splitCount++;
        return Stream.of(new Position(this.row+1, i+1), new Position(this.row+1, i-1))
                .toList()
        ;
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
