package dia7.a;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Gestiona la simulación completa del haz ("beam") del día 7, fila a fila, desde la
 * posición inicial marcada con 'S' hasta la última línea de la cuadrícula. Implementa
 * el patrón Singleton para mantener una única instancia global de la simulación.
 */
public class BeamManager {
    private final List<Beam> beamList;
    // Instancia única del Singleton.
    private static BeamManager instance;

    /**
     * Constructor privado: inicializa la lista de haces (uno por fila procesada).
     * Solo se puede instanciar a través de {@link #getInstance()}.
     */
    private BeamManager() {
        beamList = new ArrayList<>();
    }

    /**
     * Devuelve la instancia única de {@link BeamManager}, creándola si aún no existe (patrón Singleton).
     *
     * @return la instancia única del gestor de haces
     */
    public static BeamManager getInstance() {
        if (instance == null) instance = new BeamManager();
        return instance;
    }

    /**
     * Reinicia la instancia única del Singleton, descartando el estado anterior.
     * Útil para procesar una nueva entrada desde cero (por ejemplo, entre tests).
     */
    public static void resetInstance() {
        instance = new BeamManager();
    }

    /**
     * Parsea el puzzle completo, dividiéndolo en líneas.
     *
     * @param puzzle texto completo de la cuadrícula
     * @return la propia instancia, con la simulación completa calculada
     */
    public BeamManager parse(String puzzle) {
        return parse(puzzle.split("\n"));
    }

    /**
     * Inicializa el primer haz en la fila 0, en la columna donde se encuentra la 'S'
     * (punto de partida), y procesa el resto de líneas de la cuadrícula.
     *
     * @param split líneas de la cuadrícula
     * @return la propia instancia, tras procesar todas las filas
     */
    private BeamManager parse(String[] split) {
        // Se busca la posición de la 'S' en la primera línea para crear el haz inicial.
        beamList.add(Beam
                .create(0, split[0].length())
                .add(new Position(0, split[0].indexOf('S')))
        );
        // Se procesa el resto de líneas (todas menos la primera, ya usada para el haz inicial).
        return parse(Arrays.asList(split).subList(1, split.length));
    }

    /**
     * Procesa secuencialmente cada línea restante de la cuadrícula, generando el haz
     * de la fila siguiente a partir del haz de la fila anterior.
     *
     * @param strings líneas restantes de la cuadrícula (a partir de la segunda)
     * @return la propia instancia, con todos los haces ya calculados
     */
    private BeamManager parse(List<String> strings) {
        IntStream
                .range(0, strings.size())
                // Por cada línea, se calcula el haz siguiente a partir del haz ya almacenado en esa posición.
                .mapToObj(i -> beamList
                        .get(i)
                        .next(strings.get(i))
                )
                .forEach(beamList::add);

        return this;
    }

    /**
     * Devuelve el número de posiciones ocupadas por el haz en la última fila procesada.
     *
     * @return cantidad de posiciones del último haz
     */
    public long count() {
        return beamList.getLast().count();
    }

    /**
     * Devuelve el número total de divisiones sufridas por el haz al llegar a la última fila.
     *
     * @return número de divisiones acumuladas
     */
    public long splits() {
        return beamList.getLast().splits();
    }
}
