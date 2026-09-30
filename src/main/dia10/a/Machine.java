package dia10.a;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Representa una máquina del día 10 (parte a), compuesta por un panel de luces
 * indicadoras y un conjunto de botones que alternan subconjuntos de luces.
 * Calcula el número mínimo de pulsaciones de botones necesarias para pasar del
 * estado inicial de las luces al estado deseado, mediante una búsqueda en anchura (BFS).
 */
public class Machine {
    private static final Pattern LIGHTS_PATTERN = Pattern.compile("\\[(.*?)]");
    private static final Pattern BUTTON_PATTERN = Pattern.compile("\\((.*?)\\)");

    private final IndicatorLights indicatorLights;
    private final Set<Button> buttons;

    /**
     * Constructor privado que inicializa el panel de luces y el conjunto de
     * botones vacíos. Se usa la fábrica estática {@link #create(String)}.
     */
    private Machine() {
        indicatorLights = IndicatorLights.create();
        buttons = new HashSet<>();
    }

    /**
     * Crea una máquina a partir de una línea de entrada, parseando su patrón
     * de luces deseado y sus botones.
     *
     * @param input línea de texto que describe la máquina.
     * @return una nueva instancia de Machine ya inicializada.
     */
    public static Machine create(String input) {
        return new Machine().parse(input);
    }

    /**
     * Parsea la línea de entrada completa: primero las luces, luego los botones.
     *
     * @param input línea de texto de entrada.
     * @return this, para encadenamiento.
     */
    private Machine parse(String input) {
        return parseLights(input).parseButton(input);
    }

    /**
     * Extrae el patrón de luces (entre corchetes) de la entrada y lo pasa al
     * panel de luces indicadoras.
     *
     * @param input línea de texto de entrada.
     * @return this, para encadenamiento.
     */
    private Machine parseLights(String input) {
        indicatorLights.parse(parseResult(LIGHTS_PATTERN.matcher(input)));
        return this;
    }

    /**
     * Extrae todos los botones (grupos entre paréntesis) de la entrada.
     *
     * @param input línea de texto de entrada.
     * @return this, para encadenamiento.
     */
    private Machine parseButton(String input) {
        return parseButton(BUTTON_PATTERN.matcher(input));
    }

    /**
     * Recorre todas las coincidencias del patrón de botones y crea un
     * {@link Button} por cada grupo de índices de luces encontrado.
     *
     * @param matcher matcher ya configurado con el patrón de botones.
     * @return this, para encadenamiento.
     */
    private Machine parseButton(Matcher matcher) {
        // Recorre cada coincidencia "(n,n,n)" del texto y crea un botón con esos índices
        while (matcher.find()) {
            buttons.add(createButton(
                    Arrays.stream(matcher.group(1).split(","))
                            .filter(s -> !s.isBlank())
                            .mapToInt(Integer::parseInt)
                            .boxed()
                            .collect(Collectors.toSet())
            ));
        }
        return this;
    }

    /**
     * Crea un botón a partir del conjunto de índices de luces que afecta.
     *
     * @param lights conjunto de índices de luces.
     * @return un nuevo Button.
     */
    private Button createButton(Set<Integer> lights) {
        return new Button(lights);
    }

    /**
     * Ejecuta el matcher y devuelve el primer grupo capturado, o lanza una
     * excepción si no hay coincidencia.
     *
     * @param m matcher a ejecutar.
     * @return el texto capturado por el primer grupo.
     */
    private String parseResult(Matcher m) {
        if (!m.find()) throw new IllegalArgumentException("Invalid input");
        return m.group(1);
    }

    /**
     * Calcula el número mínimo de pulsaciones de botones necesarias para
     * transformar el estado actual de las luces en el estado deseado,
     * mediante una búsqueda en anchura (BFS) sobre el espacio de estados.
     *
     * @return el número mínimo de pasos, o 0 si ya coinciden los estados.
     */
    public int desiredStateSteps() {
        Set<Integer> targetState = getTargetLightIndices();
        Set<Integer> initialState = getCurrentLightIndices();

        // Si el estado inicial ya es el deseado, no hacen falta pulsaciones;
        // si no, se lanza una BFS partiendo del estado inicial como única frontera
        return initialState.equals(targetState) ? 0 :
                bfsMinSteps(
                        Set.of(initialState),
                        List.of(initialState),
                        targetState,
                        0
                );
    }

    /**
     * Obtiene el conjunto de índices de luces que deben estar encendidas
     * según el estado deseado.
     *
     * @return conjunto de índices de luces encendidas en el objetivo.
     */
    private Set<Integer> getTargetLightIndices() {
        return IntStream.range(0, indicatorLights.getDesiredState().size())
                .filter(i -> indicatorLights.getDesiredState().get(i).state() == LightState.On)
                .boxed()
                .collect(Collectors.toSet());
    }

    /**
     * Obtiene el conjunto de índices de luces que están actualmente encendidas.
     *
     * @return conjunto de índices de luces encendidas en el estado actual.
     */
    private Set<Integer> getCurrentLightIndices() {
        return IntStream.range(0, indicatorLights.getLightsState().length)
                .filter(i -> indicatorLights.getLightsState()[i] == LightState.On)
                .boxed()
                .collect(Collectors.toSet());
    }

    /**
     * Realiza una búsqueda en anchura (BFS) por niveles (implementada de forma
     * recursiva) sobre el espacio de posibles estados de luces, hasta encontrar
     * el estado objetivo o agotar la frontera sin encontrarlo.
     *
     * @param visited conjunto de estados ya visitados (para no repetir trabajo).
     * @param frontier lista de estados alcanzados en el nivel actual de la BFS.
     * @param target estado objetivo que se busca alcanzar.
     * @param depth profundidad actual (número de pulsaciones realizadas hasta ahora).
     * @return el número mínimo de pulsaciones para alcanzar el objetivo, o -1 si es inalcanzable.
     */
    private int bfsMinSteps(Set<Set<Integer>> visited, List<Set<Integer>> frontier, Set<Integer> target, int depth) {
        // Caso base: frontera vacía significa que ya no hay más estados por explorar (inalcanzable)
        // Si el objetivo está en la frontera actual, depth es la respuesta (BFS garantiza el mínimo)
        // En otro caso, se avanza un nivel más generando la siguiente frontera
        return frontier.isEmpty() ? -1 :
                frontier.contains(target) ? depth :
                        bfsMinSteps(
                                mergeVisitedAndFrontier(visited, frontier),
                                generateNextFrontier(visited, frontier),
                                target,
                                depth + 1
                        );
    }

    /**
     * Combina el conjunto de visitados con la frontera actual, para marcar
     * como visitados todos los estados del nivel actual antes de avanzar.
     *
     * @param visited conjunto de estados visitados hasta ahora.
     * @param frontier frontera del nivel actual.
     * @return nuevo conjunto de visitados incluyendo la frontera actual.
     */
    private Set<Set<Integer>> mergeVisitedAndFrontier(Set<Set<Integer>> visited, List<Set<Integer>> frontier) {
        return Stream.concat(visited.stream(), frontier.stream())
                .collect(Collectors.toSet());
    }

    /**
     * Genera la siguiente frontera de la BFS: para cada estado de la frontera
     * actual, aplica cada botón disponible y conserva solo los estados nuevos
     * (no visitados aún), eliminando duplicados.
     *
     * @param visited conjunto de estados ya visitados.
     * @param frontier frontera del nivel actual.
     * @return lista de nuevos estados alcanzables en el siguiente nivel.
     */
    private List<Set<Integer>> generateNextFrontier(Set<Set<Integer>> visited, List<Set<Integer>> frontier) {
        return frontier.stream()
                // Para cada estado, genera todos los estados resultantes de pulsar cada botón
                .flatMap(state -> buttons.stream()
                        .map(button -> applyButton(state, button.lights())))
                // Se descartan los estados ya visitados para evitar ciclos y trabajo repetido
                .filter(state -> !visited.contains(state))
                .distinct()
                .toList();
    }

    /**
     * Aplica un botón sobre un estado de luces: las luces indicadas por el
     * botón cambian de estado (las que estaban encendidas se apagan y viceversa),
     * simulando una operación XOR entre conjuntos.
     *
     * @param currentState conjunto de índices de luces actualmente encendidas.
     * @param buttonLights conjunto de índices de luces que afecta el botón.
     * @return el nuevo conjunto de luces encendidas tras pulsar el botón.
     */
    private Set<Integer> applyButton(Set<Integer> currentState, Set<Integer> buttonLights) {
        return Stream.concat(
                // Las luces que estaban encendidas y el botón NO afecta, se mantienen encendidas
                currentState.stream().filter(light -> !buttonLights.contains(light)),
                // Las luces que el botón afecta y NO estaban encendidas, se encienden
                buttonLights.stream().filter(light -> !currentState.contains(light))
        ).collect(Collectors.toSet());
    }
}
