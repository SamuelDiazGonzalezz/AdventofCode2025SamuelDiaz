package dia9.a;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Gestiona el conjunto de posiciones leídas de la entrada del día 9 (parte a)
 * y calcula, a partir de todas las combinaciones posibles de puntos, cuál es
 * el rectángulo de mayor área.
 */
public class TilesManager {
    private final List<Position> positionList;
    private final List<Rectangle> rectangleList;

    /**
     * Constructor privado que inicializa las listas internas vacías.
     * Se usa el patrón de fábrica estática {@link #create()} para instanciar la clase.
     */
    private TilesManager() {
        positionList = new ArrayList<>();
        rectangleList = new ArrayList<>();
    }

    /**
     * Crea una nueva instancia vacía de TilesManager.
     *
     * @return una nueva instancia de TilesManager.
     */
    public static TilesManager create() {
        return new TilesManager();
    }

    /**
     * Punto de entrada público para parsear la entrada completa (varias líneas).
     *
     * @param input texto completo de entrada, con una posición por línea.
     * @return this, para permitir encadenamiento de llamadas (fluent API).
     */
    public TilesManager parse(String input) {
        // Separa la entrada en líneas y delega en el parseo de cada línea
        return parse(input.split("\n"));
    }

    /**
     * Parsea un array de líneas, separando cada una por comas para obtener
     * los componentes numéricos de cada posición.
     *
     * @param split array de líneas de texto.
     * @return this, para encadenamiento.
     */
    private TilesManager parse(String[] split) {
        return parse(Arrays.stream(split).map(s -> s.trim().split(",")));
    }

    /**
     * Convierte un stream de arrays de strings (coordenadas en texto) en objetos
     * {@link Position} y los añade a la lista de posiciones.
     *
     * @param positionStream stream de arrays de strings con las coordenadas x,y.
     * @return this, para encadenamiento.
     */
    private TilesManager parse(Stream<String[]> positionStream) {
        positionList.addAll(positionStream
                // Convierte cada componente de texto a entero
                .map(strings -> Arrays.stream(strings).mapToInt(Integer::parseInt).toArray())
                // Construye la Position a partir de los dos enteros (x, y)
                .map(ints -> new Position(ints[0], ints[1]))
                .toList()
        );
        return this;
    }

    /**
     * Genera todos los rectángulos posibles combinando cada par distinto de
     * posiciones leídas, y los ordena de mayor a menor área.
     *
     * @return this, para encadenamiento.
     */
    public TilesManager rectangles() {
        rectangleList.addAll(IntStream.range(0, positionList.size())
                .boxed()
                // Para cada índice i, se combina con todos los índices j > i
                // para generar todas las combinaciones únicas de pares (sin repetir ni invertir)
                .flatMap(i ->
                        IntStream.range(i + 1, positionList.size())
                                .mapToObj(j -> new Rectangle(positionList.get(i), positionList.get(j)))
                )
                // Ordena los rectángulos de mayor a menor área
                .sorted(Comparator.comparingLong(Rectangle::area).reversed())
                .toList());

        return this;
    }

    /**
     * Devuelve el área del rectángulo más grande encontrado (el primero de la
     * lista ordenada de mayor a menor).
     *
     * @return el área máxima entre todos los rectángulos generados.
     */
    public long largestArea() {
        return rectangleList.getFirst().area();
    }
}
