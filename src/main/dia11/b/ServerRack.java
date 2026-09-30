package dia11.b;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Representa el grafo de dispositivos conectados (rack de servidores) del
 * día 11 (parte b). A diferencia de la parte a, los caminos válidos deben
 * pasar obligatoriamente por ciertos dispositivos ("dac" y "fft"), por lo que
 * la búsqueda en profundidad (DFS) recursiva necesita arrastrar el conjunto de
 * dispositivos visitados y memoizar según el dispositivo actual y cuántos de
 * los obligatorios se han visitado ya.
 */
public class ServerRack {
    private final Map<String, Device> deviceMap;
    private final Map<CacheKey, Long> mem;

    /**
     * Constructor privado que inicializa el mapa de dispositivos y el mapa de
     * memoización vacíos. Se usa la fábrica estática {@link #create()}.
     */
    private ServerRack() {
        deviceMap = new HashMap<>();
        mem = new HashMap<>();
    }

    /**
     * Crea una nueva instancia vacía de ServerRack.
     *
     * @return una nueva instancia.
     */
    public static ServerRack create() {
        return new ServerRack();
    }

    /**
     * Parsea la entrada completa: cada línea define un dispositivo con el
     * formato "etiqueta: salida1 salida2 ...".
     *
     * @param input texto completo de entrada.
     * @return this, para encadenamiento.
     */
    public ServerRack parse(String input) {
        Map<String, List<String>> parsed = Arrays.stream(input.split("\n"))
                .map(String::trim)
                .filter(l -> !l.isBlank())
                // Separa cada línea en "etiqueta" y "lista de salidas" por el ":"
                .map(line -> line.split(":"))
                .collect(Collectors.toMap(
                        deviceStr -> deviceStr[0].trim(),
                        deviceStr -> Arrays.stream(deviceStr[1].trim().split("\\s+"))
                                .toList()
                ));

        // Convierte el mapa intermedio (etiqueta -> salidas) en dispositivos completos
        for (Map.Entry<String, List<String>> entry : parsed.entrySet()) {
            deviceMap.put(entry.getKey(), new Device(entry.getKey(), entry.getValue()));
        }

        return this;
    }

    /**
     * Obtiene las etiquetas de los dispositivos a los que se conecta
     * directamente el dispositivo dado.
     *
     * @param device el dispositivo de origen.
     * @return lista de etiquetas de los dispositivos siguientes.
     */
    public List<String> next(Device device) {
        return device.outputs();
    }

    /**
     * Busca el dispositivo con la etiqueta indicada. Si no existe, devuelve
     * un dispositivo "vacío" sin salidas (nodo terminal).
     *
     * @param label etiqueta del dispositivo buscado.
     * @return el dispositivo encontrado, o uno vacío si no existe.
     */
    public Device getDevice(String label) {
        return deviceMap.getOrDefault(label, new Device(label, List.of()));
    }

    /**
     * Calcula recursivamente (DFS), con memoización, el número de caminos
     * distintos desde {@code from} hasta {@code to} que además pasan por todos
     * los dispositivos obligatorios registrados en {@link VisitedSet}.
     *
     * @param from dispositivo actual en la búsqueda.
     * @param to dispositivo de destino.
     * @param vs conjunto de dispositivos visitados hasta ahora en este camino.
     * @return el número de caminos válidos desde from hasta to que cumplen los requisitos.
     */
    public long pathsTo(Device from, Device to, VisitedSet vs) {
        CacheKey key = new CacheKey(from, vs);
        // Si ya se calculó este mismo estado (dispositivo + nº de obligatorios visitados), se reutiliza
        if (mem.containsKey(key)) return mem.get(key);

        if (from.equals(to)) {
            // Al llegar al destino, el camino solo es válido si ya pasó por todos los dispositivos obligatorios
            return vs.isValid() ? 1 : 0;
        }

        long total = total(from, to, vs);
        mem.put(key, total);
        return total;
    }

    /**
     * Suma recursivamente el número de caminos válidos que se pueden alcanzar
     * desde cada una de las salidas del dispositivo actual, marcando el
     * dispositivo actual como visitado en cada rama.
     *
     * @param from dispositivo actual.
     * @param to dispositivo de destino.
     * @param vs conjunto de visitados hasta ahora.
     * @return la suma de caminos válidos desde todas las salidas.
     */
    private long total(Device from, Device to, VisitedSet vs) {
        return next(from)
                .stream()
                // Cada rama recibe una copia del conjunto de visitados con el dispositivo actual añadido
                .mapToLong(newFrom -> pathsTo(getDevice(newFrom), to, vs.addNew(from)))
                .sum();
    }

    /**
     * Sobrecarga de conveniencia que acepta las etiquetas de los dispositivos
     * de origen y destino, iniciando la búsqueda con un conjunto de visitados vacío.
     *
     * @param from etiqueta del dispositivo de origen.
     * @param to etiqueta del dispositivo de destino.
     * @return el número de caminos válidos entre from y to.
     */
    public long pathsTo(String from, String to) {
        return pathsTo(getDevice(from), getDevice(to), new VisitedSet());
    }
}
