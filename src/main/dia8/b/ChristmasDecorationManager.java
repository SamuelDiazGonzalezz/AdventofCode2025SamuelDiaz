package dia8.b;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

/**
 * Gestiona el puzzle del día 8 (parte b): parsea las posiciones de las cajas de conexiones,
 * calcula la distancia entre todos los pares posibles y va conectando las cajas más cercanas,
 * en orden creciente de distancia, hasta que todas quedan formando un único circuito.
 * El resultado es el producto de la coordenada x de las dos cajas que completan esa última conexión.
 */
public class ChristmasDecorationManager {
    private final List<Position> boxList;
    private List<JunctionBoxPair> pairList;
    private CircuitSet circuitSet;

    /**
     * Constructor privado: inicializa las listas vacías de cajas y pares.
     * Se usa el método estático {@link #create()} para instanciar la clase.
     */
    private ChristmasDecorationManager() {
        this.boxList = new ArrayList<>();
        this.pairList = new ArrayList<>();
    }

    /**
     * Crea una nueva instancia vacía de {@link ChristmasDecorationManager}.
     *
     * @return una nueva instancia lista para parsear datos
     */
    public static ChristmasDecorationManager create() {
        return new ChristmasDecorationManager();
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
     * Va conectando los pares de cajas en orden de menor a mayor distancia hasta que
     * todas las cajas queden formando un único circuito, y calcula el resultado a partir
     * del último par conectado (el que cierra el circuito).
     *
     * @return el resultado calculado a partir del par que completa la unión de todos los circuitos
     */
    public long calculate() {
        circuitSet = computeJunctionPairs();
        // Bucle infinito controlado: se va conectando un par cada vez hasta que solo quede un circuito.
        for (int i = 0; ; i++) {
            circuitSet.add(pairList.get(i));

            // Cuando todas las cajas están en un único circuito, se ha completado la conexión total.
            if (circuitSet.size() == 1) {
                return calculateMath(pairList.get(i));
            }
        }
    }

    /**
     * Calcula el resultado final a partir del par de cajas que completa la conexión de todo el circuito,
     * multiplicando las coordenadas x de ambas cajas.
     *
     * @param pair par de cajas que cierra el último circuito
     * @return el producto de las coordenadas x de ambas cajas
     */
    private long calculateMath(JunctionBoxPair pair) {
        return pair.junctionBox1().x() * pair.junctionBox2().x();
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
}
