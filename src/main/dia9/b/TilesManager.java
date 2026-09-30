package dia9.b;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Gestiona las posiciones (vértices del polígono) leídas de la entrada del día 9
 * (parte b) y calcula, entre todos los rectángulos formados por pares de vértices
 * que quedan completamente contenidos dentro del polígono, cuál tiene mayor área.
 */
public class TilesManager {
    private final List<Position> positionList;
    private final List<Rectangle> rectangleList;
    private Polygon polygon;

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
     * Punto de entrada público para parsear la entrada completa (varias líneas,
     * una posición por línea, que forman los vértices del polígono).
     *
     * @param input texto completo de entrada.
     * @return this, para encadenamiento.
     */
    public TilesManager parse(String input) {
        return parse(input.split("\n"));
    }

    /**
     * Parsea un array de líneas, separando cada una por comas.
     *
     * @param split array de líneas de texto.
     * @return this, para encadenamiento.
     */
    private TilesManager parse(String[] split) {
        return parse(Arrays.stream(split).map(s -> s.trim().split(",")));
    }

    /**
     * Convierte un stream de arrays de strings (coordenadas en texto) en objetos
     * {@link Position}, los añade a la lista de posiciones y construye el polígono
     * resultante a partir de ellos.
     *
     * @param positionStream stream de arrays de strings con las coordenadas x,y.
     * @return this, para encadenamiento.
     */
    private TilesManager parse(Stream<String[]> positionStream) {
        positionList.addAll(positionStream
                .map(strings -> Arrays.stream(strings).mapToInt(Integer::parseInt).toArray())
                .map(ints -> new Position(ints[0], ints[1]))
                .toList()
        );
        // Una vez leídos todos los vértices, se construye el polígono correspondiente
        polygon = Polygon.create(positionList);
        return this;
    }

    /**
     * Genera todos los rectángulos posibles combinando cada par distinto de
     * vértices, filtra solo los que quedan completamente dentro del polígono,
     * y los ordena de mayor a menor área.
     *
     * @return this, para encadenamiento.
     */
    public TilesManager rectangles() {
        if (polygon == null) return this;
        rectangleList.addAll(IntStream.range(0, positionList.size())
                .boxed()
                // Genera todas las combinaciones únicas de pares de vértices (i < j)
                .flatMap(i ->
                        IntStream.range(i + 1, positionList.size())
                                .mapToObj(j -> new Rectangle(positionList.get(i), positionList.get(j)))
                )
                // Descarta los rectángulos que no están completamente dentro del polígono
                .filter(polygon::contains)
                // Ordena de mayor a menor área
                .sorted(Comparator.comparingLong(Rectangle::area).reversed())
                .toList());

        return this;
    }

    /**
     * Devuelve el área del rectángulo válido más grande encontrado.
     *
     * @return el área máxima entre los rectángulos contenidos en el polígono.
     */
    public long largestArea() {
        return rectangleList.getFirst().area();
    }
}
