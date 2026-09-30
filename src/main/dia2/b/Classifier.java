package dia2.b;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.LongStream;

/**
 * Clasifica todos los identificadores (IDs) dentro de un rango [from, to]
 * en válidos e inválidos, y permite sumar los valores numéricos de los
 * identificadores inválidos encontrados. Versión del día 2, parte B,
 * que usa la definición de validez de {@link ID} (patrones repetidos de
 * cualquier longitud de bloque, no solo la mitad exacta).
 */
public class Classifier {
    private final List<ID> invalidIDs;
    private long from;
    private long to;

    /**
     * Constructor privado: inicializa la lista de IDs inválidos vacía.
     * Se usa el patrón de fábrica estática {@link #create()} en su lugar.
     */
    private Classifier() { invalidIDs = new ArrayList<>(); }

    /**
     * Crea una nueva instancia vacía de {@link Classifier}.
     *
     * @return un nuevo clasificador sin rango ni resultados.
     */
    public static Classifier create() {
        return new Classifier();
    }

    /**
     * Establece el límite inferior (inclusive) del rango de IDs a analizar.
     *
     * @param from valor inicial del rango.
     * @return el propio clasificador, para poder encadenar llamadas.
     */
    public Classifier from(long from) {
        this.from = from;
        return this;
    }

    /**
     * Establece el límite superior (inclusive) del rango de IDs a analizar.
     * Internamente se guarda como {@code to + 1} para poder usar un rango
     * exclusivo en {@link LongStream#range}.
     *
     * @param to valor final del rango (inclusive).
     * @return el propio clasificador, para poder encadenar llamadas.
     */
    public Classifier to(long to) {
        this.to = to+1; // se suma 1 porque LongStream.range es exclusivo en el límite superior
        return this;
    }

    /**
     * Recorre todo el rango [from, to) y clasifica cada número como
     * ID válido o inválido.
     *
     * @return el propio clasificador, para poder encadenar llamadas.
     */
    public Classifier calculate() {
        LongStream.range(from, to).forEach(this::add); // procesa uno a uno todos los números del rango
        return this;
    }

    /**
     * Analiza un número concreto del rango: lo convierte en {@link ID}
     * y, si no es válido, lo añade a la lista de IDs inválidos.
     *
     * @param number número a analizar.
     */
    private void add(long number) {
        ID id = new ID(Long.toString(number));
        if (id.isValid()) return; // si es válido, no se hace nada más
        invalidIDs.add(id); // si es inválido, se guarda para sumarlo después
    }

    /**
     * Suma los valores numéricos de todos los IDs inválidos detectados.
     *
     * @return la suma de los IDs inválidos.
     */
    public long sum() {
        return invalidIDs.stream()
                .mapToLong(ID::longNumber)
                .sum();
    }
}
