package dia8.a;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Gestiona el puzzle del día 8: parsea las posiciones de las cajas de conexiones,
 * calcula la distancia entre todos los pares posibles, conecta las cajas más cercanas
 * hasta un límite indicado y calcula el resultado en función de los tres circuitos
 * más grandes formados.
 */
public class ChristmasDecorationManager {
    // Número máximo de conexiones (pares de cajas) que se van a realizar.
    private final int MAX_JUNCTION_CONNECT_COUNT;
    private final List<Position> boxList;
    private List<JunctionBoxPair> pairList;
    private CircuitSet circuitSet;

    /**
     * Constructor privado: inicializa las listas de cajas y pares, y fija el límite de conexiones.
     *
     * @param maxConnectJunction número máximo de conexiones a realizar
     */
    private ChristmasDecorationManager(int maxConnectJunction) {
        this.boxList = new ArrayList<>();
        this.pairList = new ArrayList<>();
        this.MAX_JUNCTION_CONNECT_COUNT = maxConnectJunction;
    }

    /**
     * Crea una nueva instancia de {@link ChristmasDecorationManager} con el límite de conexiones indicado.
     *
     * @param maxConnectJunction número máximo de conexiones a realizar
     * @return una nueva instancia lista para parsear datos
     */
    public static ChristmasDecorationManager create(int maxConnectJunction) {
        return new ChristmasDecorationManager(maxConnectJunction);
    }

    /**
     * Añade una caja de conexiones a la lista.
     *
     * @param box posición de la caja a añadir
     * @return la propia instancia, para encadenar llamadas
     */
    public ChristmasDecorationManager add(Position box) {
        this.boxList.add(box);
        return this;
    }

    /**
     * Añade varias cajas de conexiones a la lista.
     *
     * @param boxList lista de posiciones a añadir
     * @return la propia instancia, para encadenar llamadas
     */
    public ChristmasDecorationManager addAll(List<Position> boxList) {
        this.boxList.addAll(boxList);
        return this;
    }

    /**
     * Parsea la entrada completa, dividiéndola en líneas.
     *
     * @param input texto completo de entrada
     * @return la propia instancia, con las cajas ya cargadas
     */
    public ChristmasDecorationManager parse(String input) {
        return parse(Arrays.stream(input.split("\n")));
    }

    /**
     * Parsea un flujo de líneas de texto, cada una con las coordenadas x,y,z separadas por comas.
     *
     * @param input flujo de líneas de entrada
     * @return la propia instancia, con las cajas ya cargadas
     */
    public ChristmasDecorationManager parse(Stream<String> input) {
        return parse(input
                // Cada línea se recorta y se separa por comas para obtener sus tres componentes.
                .map(s -> toIntList(s.trim().split(",")))
                // Se construye una Position con los tres valores (x, y, z).
                .map(iL -> new Position(iL.getFirst(), iL.get(1), iL.getLast())
                ).toList()
        );
    }

    /**
     * Añade una lista ya construida de posiciones de cajas.
     *
     * @param input lista de posiciones a añadir
     * @return la propia instancia, con las cajas ya cargadas
     */
    public ChristmasDecorationManager parse(List<Position> input) {
        this.boxList.addAll(input);
        return this;
    }

    /**
     * Convierte un array de cadenas numéricas en una lista de enteros.
     *
     * @param split array de cadenas numéricas
     * @return lista de enteros equivalente
     */
    private List<Integer> toIntList(String[] split) {
        return Arrays.stream(split).mapToInt(Integer::parseInt).boxed().toList();
    }

    /**
     * Calcula todos los pares posibles de cajas ordenados por distancia y conecta,
     * en orden de menor a mayor distancia, tantos pares como indique el límite configurado.
     *
     * @return la propia instancia, con los circuitos ya calculados
     */
    public ChristmasDecorationManager startConnections() {
        circuitSet = computeJunctionPairs();
        // Se conectan únicamente los MAX_JUNCTION_CONNECT_COUNT pares más cercanos (ya ordenados por distancia).
        IntStream.range(0, this.MAX_JUNCTION_CONNECT_COUNT)
                .mapToObj(i -> pairList.get(i))
                .forEach(circuitSet::add);

        return this;
    }

    /**
     * Calcula la distancia entre todos los pares posibles de cajas (combinaciones sin repetición),
     * los ordena de menor a mayor distancia y crea el conjunto inicial de circuitos (uno por caja).
     *
     * @return el conjunto de circuitos inicial, con una caja por circuito
     */
    private CircuitSet computeJunctionPairs() {
        // Doble bucle para generar todas las combinaciones únicas de pares de cajas (i, j) con j > i.
        for (int i = 0; i < boxList.size(); i++) {
            for (int j = i + 1; j < boxList.size(); j++) {
                Position box1 = boxList.get(i);
                Position box2 = boxList.get(j);
                pairList.add(new JunctionBoxPair(box1, box2, box1.distanceTo(box2)));
            }
        }
        // Se ordenan los pares de menor a mayor distancia, para conectarlos empezando por los más cercanos.
        pairList.sort(JunctionBoxPair::compareTo);
        return new CircuitSet(boxList);
    }

    /**
     * Calcula el resultado final del puzzle: el producto de los tamaños de los tres circuitos
     * más grandes formados tras las conexiones.
     *
     * @return el producto de los tamaños de los tres circuitos más grandes
     */
    public long calculate() {
        return circuitSet
                .getThreeLargest()
                .stream()
                .mapToLong(Set::size)
                // Se multiplican entre sí los tamaños de los tres circuitos más grandes.
                .reduce(1, (a, b) -> a * b);
    }
}
