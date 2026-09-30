package dia11.a;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Representa el grafo de dispositivos conectados (rack de servidores) del
 * día 11 (parte a). Permite parsear la lista de conexiones y calcular, mediante
 * búsqueda en profundidad (DFS) recursiva con memoización, el número total de
 * caminos distintos desde un dispositivo origen hasta uno destino.
 */
public class ServerRack {
    private final Set<Device> deviceList;
    private final Map<Device, Long> mem;

    /**
     * Constructor privado que inicializa el conjunto de dispositivos y el
     * mapa de memoización vacíos. Se usa la fábrica estática {@link #create()}.
     */
    private ServerRack() {
        deviceList = new HashSet<>();
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
        deviceList.addAll(Arrays.stream(input
                        .split("\n"))
                        .map(String::trim)
                        .filter(l -> !l.isBlank())
                        // Separa cada línea en "etiqueta" y "lista de salidas" por el ":"
                        .map(line -> line.split(":"))
                        .collect(Collectors.toMap(
                                deviceStr -> deviceStr[0].trim(),
                                // Las salidas se separan por espacios en blanco
                                deviceStr -> Arrays.stream(deviceStr[1].trim().split("\\s+"))
                                        .toList()
                        ))
                .entrySet()
                .stream()
                .map(device -> new Device(device.getKey(), device.getValue()))
                .toList()
        );
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
        return deviceList.stream().filter(dev -> dev.label().equals(label)).findFirst().orElse(new Device(label, List.of()));
    }

    /**
     * Calcula recursivamente (DFS) el número total de caminos distintos desde
     * el dispositivo {@code from} hasta el dispositivo {@code to}, sumando los
     * caminos de cada una de sus salidas. Usa memoización para no recalcular
     * el número de caminos desde un mismo dispositivo más de una vez.
     *
     * @param from dispositivo de origen.
     * @param to dispositivo de destino.
     * @return el número total de caminos distintos entre from y to.
     */
    public long pathsTo(Device from, Device to) {
        // Caso base: si ya estamos en el destino, este es un camino válido (cuenta 1)
        if (from.equals(to)) return 1;
        // Si ya se calculó el número de caminos desde este dispositivo, se reutiliza
        if (mem.containsKey(from)) return mem.get(from);

        // Se suman recursivamente los caminos posibles desde cada salida del dispositivo actual
        long totalSteps = next(from).stream().mapToLong(out -> pathsTo(getDevice(out), to)).sum();
        mem.put(from, totalSteps);
        return totalSteps;
    }

    /**
     * Sobrecarga de conveniencia que acepta las etiquetas de los dispositivos
     * de origen y destino en lugar de los objetos Device.
     *
     * @param from etiqueta del dispositivo de origen.
     * @param to etiqueta del dispositivo de destino.
     * @return el número total de caminos distintos entre from y to.
     */
    public long pathsTo(String from, String to) {
        return pathsTo(getDevice(from), getDevice(to));
    }
}
