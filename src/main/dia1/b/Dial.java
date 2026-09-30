package dia1.b;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Representa el dial circular (de 0 a 99) del problema del día 1, parte B.
 * Además de calcular la posición final, añade la capacidad de contar
 * cuántas veces el dial cruza (pasa por) la posición 0 durante cada orden,
 * incluso si la orden hace varias vueltas completas.
 */
public class Dial {
    private final List<Order> orders;

    /**
     * Constructor privado: inicializa la lista de órdenes vacía.
     * Se usa el patrón de fábrica estática {@link #create()} en su lugar.
     */
    private Dial() {
        this.orders = new ArrayList<>();
    }

    /**
     * Crea una nueva instancia vacía de {@link Dial}.
     *
     * @return un nuevo dial sin órdenes.
     */
    public static Dial create() {
        return new Dial();
    }

    /**
     * Añade varias órdenes en formato texto (por ejemplo "L5", "R10") al dial.
     *
     * @param orders cadenas de texto con las órdenes a parsear y añadir.
     * @return el propio dial, para poder encadenar llamadas.
     */
    public Dial add(String... orders) {
        Arrays.stream(orders)
                .map(this::parse) // convierte cada cadena de texto en un objeto Order
                .forEach(this::add); // añade cada Order ya parseada a la lista
        return this;
    }

    /**
     * Añade una orden ya parseada a la lista interna.
     *
     * @param order la orden a añadir.
     */
    private void add(Order order) {
        orders.add(order);
    }

    /**
     * Convierte una cadena de texto (ej. "L5") en un objeto {@link Order},
     * combinando el signo (L=negativo, R=positivo) con el valor numérico.
     *
     * @param order cadena de texto de la orden.
     * @return la orden parseada como objeto {@link Order}.
     */
    private Order parse(String order) {
        return new Order(signOf(order) * valueOf(order));
    }

    /**
     * Determina el signo del giro según la primera letra de la orden.
     * 'L' (izquierda) es negativo, cualquier otra letra ('R') es positivo.
     *
     * @param order cadena de texto de la orden.
     * @return -1 si es giro a la izquierda, 1 en caso contrario.
     */
    private int signOf(String order) {
        return order.charAt(0) == 'L' ? -1 : 1;
    }

    /**
     * Extrae el valor numérico de la orden (todo excepto la primera letra).
     *
     * @param order cadena de texto de la orden.
     * @return el valor numérico del giro.
     */
    private int valueOf(String order) {
        return Integer.parseInt(order.substring(1));
    }

    /**
     * Calcula la posición final del dial tras aplicar todas las órdenes,
     * normalizada al rango [0, 99].
     *
     * @return la posición final del dial.
     */
    public int position() {
        return normalize(sumAll());
    }

    /**
     * Suma el efecto de todas las órdenes almacenadas.
     *
     * @return la suma total (sin normalizar al rango final).
     */
    private int sumAll() {
        return sum(orders.stream());
    }

    /**
     * Cuenta cuántas posiciones parciales (tras aplicar un prefijo de órdenes)
     * hacen que el dial quede exactamente en la posición 0.
     *
     * @return el número de veces que el dial pasa exactamente por 0 al final
     *         de una orden.
     */
    public int count() {
        return (int) iterate()
                .map(this::sumPartial) // calcula la posición normalizada tras las primeras N órdenes
                .filter(s -> s == 0) // nos quedamos solo con las que dan posición 0
                .count(); // contamos cuántas hay
    }

    /**
     * Suma, para todas las órdenes, el número de veces que el dial cruza
     * por la posición 0 durante la ejecución de esa orden (incluyendo
     * posibles vueltas completas).
     *
     * @return el número total de cruces por 0 en todo el recorrido del dial.
     */
    public int zeros() {
        return IntStream.range(0, orders.size())
                .map(this::zerosAt) // calcula los cruces por cero producidos por la orden en cada índice
                .sum(); // suma todos los cruces
    }

    /**
     * Calcula cuántas veces se cruza por 0 durante la ejecución de la
     * orden situada en la posición {@code index}, teniendo en cuenta la
     * posición de partida (antes de aplicar esa orden) y la de llegada.
     *
     * @param index índice de la orden dentro de la lista.
     * @return el número de cruces por 0 producidos por esa orden concreta.
     */
    private int zerosAt(int index) {
        return IntStream.of(orders.get(index).step())
                .filter(step -> step != 0) // si el paso es 0, no hay movimiento y por tanto no hay cruces
                .map(step -> zerosBetween(rawSum(index), rawSum(index) + step)) // cuenta los cruces entre la posición inicial y la final
                .findFirst()
                .orElse(0); // si el paso era 0 (filtrado), no hay cruces
    }

    /**
     * Cuenta cuántos múltiplos de 100 (vueltas completas al dial, es decir,
     * cruces por 0) hay estrictamente entre las posiciones "crudas" (sin
     * normalizar) {@code from} y {@code to}.
     *
     * @param from posición cruda de partida.
     * @param to posición cruda de llegada.
     * @return el número de veces que se cruza por un múltiplo de 100 (el 0 del dial).
     */
    private int zerosBetween(int from, int to) {
        return from < to
                ? Math.floorDiv(to, 100) - Math.floorDiv(from, 100) // movimiento hacia adelante: diferencia de vueltas completas
                : Math.floorDiv(from - 1, 100) - Math.floorDiv(to - 1, 100); // movimiento hacia atrás: mismo cálculo mirando el límite inferior
    }

    /**
     * Calcula la suma "cruda" (sin normalizar al rango [0,99]) de los pasos
     * de las primeras {@code index} órdenes, partiendo de la posición inicial 50.
     *
     * @param index número de órdenes (desde el inicio) a considerar.
     * @return la suma cruda de los pasos hasta ese índice, incluyendo el offset inicial de 50.
     */
    private int rawSum(int index) {
        return orders.stream()
                .limit(index) // solo las primeras "index" órdenes
                .mapToInt(Order::step)
                .sum() + 50; // se parte de la posición central 50
    }

    /**
     * Genera un flujo paralelo con los tamaños de prefijo posibles
     * (de 1 hasta el número total de órdenes).
     *
     * @return un IntStream paralelo de 1 a orders.size() (ambos incluidos).
     */
    private IntStream iterate() {
        return IntStream.rangeClosed(1, orders.size()).parallel();
    }

    /**
     * Calcula la posición normalizada del dial tras aplicar las primeras
     * {@code size} órdenes.
     *
     * @param size número de órdenes (desde el inicio) a tener en cuenta.
     * @return la posición normalizada resultante.
     */
    private int sumPartial(int size) {
        return normalize(sum(orders.stream().limit(size)));
    }

    /**
     * Suma los pasos ("step") de un flujo de órdenes, partiendo de la
     * posición inicial 50 y aplicando módulo 100.
     *
     * @param orders flujo de órdenes a sumar.
     * @return la suma total módulo 100, partiendo de la posición 50.
     */
    private static int sum(Stream<Order> orders) {
        return (orders.mapToInt(Order::step).sum() + 50) % 100; // se parte de la posición 50 (centro del dial)
    }

    /**
     * Normaliza un valor al rango [0, 99], gestionando correctamente los
     * valores negativos (el módulo en Java puede devolver negativos).
     *
     * @param value el valor a normalizar.
     * @return el valor normalizado en el rango [0, 99].
     */
    private int normalize(int value) {
        return ((value % 100) + 100) % 100; // se suma 100 extra para evitar resultados negativos del módulo
    }

    /**
     * Ejecuta el dial a partir de un bloque de texto con las órdenes
     * separadas por saltos de línea.
     *
     * @param orders texto con las órdenes separadas por "\n".
     * @return el propio dial, para poder encadenar llamadas.
     */
    public Dial execute(String orders) {
        if (orders.isEmpty()) return this; // si no hay texto, no hay nada que procesar
        return add(orders.split("\n")); // separa por líneas y añade cada orden
    }
}
