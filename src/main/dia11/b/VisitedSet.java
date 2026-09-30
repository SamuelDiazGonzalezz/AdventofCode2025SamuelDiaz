package dia11.b;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Mantiene el conjunto de dispositivos visitados durante la búsqueda de
 * caminos del día 11 (parte b), y comprueba si se han visitado todos los
 * dispositivos obligatorios ("dac" y "fft") que debe atravesar un camino
 * válido.
 */
public class VisitedSet {
    private final Set<Device> deviceList;
    private final Set<Device> mustHaveDevices;
    private static final String[] MUST_PASS_DEVICES = new String[] { "dac", "fft" };

    /**
     * Crea un conjunto de visitados vacío, inicializando también el conjunto
     * fijo de dispositivos obligatorios que debe contener un camino válido.
     */
    public VisitedSet() {
        this.deviceList = new HashSet<>();
        this.mustHaveDevices =  new HashSet<>(
                Arrays.stream(MUST_PASS_DEVICES)
                        .map(str -> new Device(str, List.of()))
                        .toList()
        );

    }

    /**
     * Añade un dispositivo al conjunto de visitados (modifica el objeto actual).
     *
     * @param dev el dispositivo a añadir.
     * @return this, para encadenamiento.
     */
    public VisitedSet add(Device dev) {
        deviceList.add(dev);
        return this;
    }

    /**
     * Comprueba si el conjunto de visitados contiene todos los dispositivos
     * obligatorios, es decir, si representa un camino válido.
     *
     * @return true si se han visitado todos los dispositivos obligatorios.
     */
    public boolean isValid() {
        return deviceList.containsAll(mustHaveDevices);
    }

    /**
     * Representación textual del conjunto de visitados, útil para depuración.
     *
     * @return cadena descriptiva del conjunto.
     */
    @Override
    public String toString() {
        return "VisitedSet{" +
                "deviceList=" + deviceList +
                '}';
    }

    /**
     * Crea una copia independiente de este conjunto de visitados (mismo
     * contenido, pero sin compartir la colección interna), necesaria para no
     * mutar el estado compartido entre distintas ramas de la búsqueda recursiva.
     *
     * @return una nueva instancia de VisitedSet con el mismo contenido.
     */
    public VisitedSet copy() {
        VisitedSet copy = new VisitedSet();
        copy.deviceList.addAll(this.deviceList);
        return copy;
    }

    /**
     * Comprueba si el dispositivo dado ya ha sido visitado.
     *
     * @param dev el dispositivo a comprobar.
     * @return true si el dispositivo está en el conjunto de visitados.
     */
    public boolean contains(Device dev) {
        return deviceList.contains(dev);
    }

    /**
     * Devuelve una nueva copia de este conjunto con el dispositivo dado
     * añadido, sin modificar el conjunto original (útil para ramas
     * independientes de una búsqueda recursiva tipo DFS).
     *
     * @param from el dispositivo a añadir en la copia.
     * @return una nueva instancia de VisitedSet con el dispositivo añadido.
     */
    public VisitedSet addNew(Device from) {
        return copy().add(from);
    }

    /**
     * Cuenta cuántos de los dispositivos obligatorios ya se han visitado.
     * Se usa como resumen compacto del estado de visitados para la clave de
     * memoización {@link CacheKey}.
     *
     * @return número de dispositivos obligatorios visitados hasta ahora.
     */
    public long requiredCount() {
        return deviceList.stream().filter(mustHaveDevices::contains).count();
    }
}
