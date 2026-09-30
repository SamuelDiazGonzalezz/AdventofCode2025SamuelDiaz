package dia10.b;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Representa una máquina del día 10 (parte b), compuesta por un conjunto de
 * botones que incrementan palancas de voltaje y una lista de requisitos de
 * voltaje objetivo. Calcula el número mínimo de pulsaciones necesarias para
 * alcanzar exactamente los requisitos, usando una técnica de "meet in the
 * middle" (se precomputan todas las combinaciones posibles de botones agrupadas
 * por paridad) combinada con memoización recursiva.
 */
public class Machine {
    private static final Pattern BUTTON_PATTERN = Pattern.compile("\\((.*?)\\)");
    private static final Pattern JOLTAGE_PATTERN = Pattern.compile("\\{(.*?)}");

    private final Set<Button> buttons;
    private final List<Integer> joltageRequirementsList;

    /**
     * Constructor privado que inicializa el conjunto de botones y la lista de
     * requisitos de voltaje vacíos. Se usa la fábrica estática {@link #create(String)}.
     */
    private Machine() {
        buttons = new HashSet<>();
        joltageRequirementsList = new ArrayList<>();
    }

    /**
     * Crea una máquina a partir de una línea de entrada, parseando sus botones
     * y sus requisitos de voltaje.
     *
     * @param input línea de texto que describe la máquina.
     * @return una nueva instancia de Machine ya inicializada.
     */
    public static Machine create(String input) {
        return new Machine().parse(input);
    }

    /**
     * Parsea la línea de entrada completa: primero los botones, luego los
     * requisitos de voltaje.
     *
     * @param input línea de texto de entrada.
     * @return this, para encadenamiento.
     */
    private Machine parse(String input) {
        return parseButton(input).parseJoltage(input);
    }

    /**
     * Extrae los requisitos de voltaje (entre llaves) de la entrada y los
     * añade a la lista correspondiente.
     *
     * @param input línea de texto de entrada.
     * @return this, para encadenamiento.
     */
    private Machine parseJoltage(String input) {
        joltageRequirementsList.addAll(getJoltageStream(parseResult(JOLTAGE_PATTERN.matcher(input)))
                .mapToInt(Integer::parseInt)
                .boxed()
                .toList());
        return this;
    }

    /**
     * Separa la cadena de requisitos de voltaje por comas.
     *
     * @param LIGHTS_PATTERN cadena con los valores de voltaje separados por comas.
     * @return stream de los valores en formato texto.
     */
    private Stream<String> getJoltageStream(String LIGHTS_PATTERN) {
        return Arrays.stream(LIGHTS_PATTERN.split(","));
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
     * {@link Button} por cada grupo de índices de palancas encontrado.
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
     * Crea un botón a partir del conjunto de índices de palancas que afecta.
     *
     * @param lights conjunto de índices de palancas.
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
     * alcanzar exactamente los requisitos de voltaje, combinando un mapa de
     * patrones agrupados por paridad ("meet in the middle") con una búsqueda
     * recursiva memoizada.
     *
     * @return el número mínimo de pulsaciones necesarias.
     */
    public int desiredStateSteps() {
        // Precalcula, para cada posible subconjunto de botones (máscara de bits),
        // el vector resultante y lo agrupa por paridad para reducir el espacio de búsqueda
        Map<List<Integer>, Map<List<Integer>, Integer>> parityMaps =
                buildParityMaps(joltageRequirementsList.size());

        return (int) findMinimumPresses(joltageRequirementsList, parityMaps, new HashMap<>());
    }

    /**
     * Construye, para cada combinación posible de botones (2^numButtons
     * subconjuntos), el patrón resultante y lo agrupa por su vector de
     * paridad. Para cada paridad se conserva, por cada resultado exacto
     * posible, el coste (número de botones) mínimo necesario para lograrlo.
     *
     * @param size número de palancas de voltaje (tamaño del vector de requisitos).
     * @return mapa de paridad -> (mapa de resultado exacto -> coste mínimo).
     */
    private Map<List<Integer>, Map<List<Integer>, Integer>> buildParityMaps(int size) {
        int numButtons = buttons.size();
        List<Button> buttonList = new ArrayList<>(buttons);

        // 1 << numButtons genera todas las combinaciones posibles de pulsar/no pulsar cada botón
        return IntStream.range(0, 1 << numButtons)
                .boxed()
                .map(mask -> computePattern(mask, buttonList, size))
                .collect(Collectors.groupingBy(
                        PatternInfo::parity,
                        // Para un mismo resultado exacto, se queda con el coste (número de botones) más bajo
                        Collectors.toMap(
                                PatternInfo::result,
                                PatternInfo::cost,
                                Math::min
                        )
                ));
    }

    /**
     * Calcula el vector resultante de aplicar la combinación de botones
     * indicada por la máscara de bits {@code mask}, junto con su vector de
     * paridad y el número de botones pulsados.
     *
     * @param mask máscara de bits que indica qué botones se pulsan (bit i = botón i pulsado).
     * @param buttonList lista de botones disponibles.
     * @param size tamaño del vector de resultado (número de palancas).
     * @return la información del patrón resultante (resultado, paridad y coste).
     */
    private PatternInfo computePattern(int mask, List<Button> buttonList, int size) {
        List<Integer> result = new ArrayList<>(Collections.nCopies(size, 0));
        int cost = 0;

        // Recorre cada bit de la máscara: si está activado, ese botón se pulsa
        for (int j = 0; j < buttonList.size(); j++) {
            if ((mask & (1 << j)) != 0) {
                cost++;
                // Cada palanca afectada por el botón se incrementa en 1
                buttonList.get(j).joltageLevers()
                        .forEach(idx -> result.set(idx, result.get(idx) + 1));
            }
        }

        // La paridad (par/impar) de cada valor sirve como clave de agrupación para el "meet in the middle"
        List<Integer> parity = result.stream()
                .map(v -> v % 2)
                .collect(Collectors.toList());

        return new PatternInfo(result, parity, cost);
    }

    /**
     * Busca recursivamente, con memoización, el número mínimo de pulsaciones
     * necesarias para reducir el estado actual (requisitos restantes) a cero.
     * En cada paso se elige un patrón de botones compatible con la paridad del
     * estado actual, se resta su efecto y se divide entre 2 (porque cada patrón
     * puede repetirse un número par de veces adicional sin cambiar la paridad),
     * continuando recursivamente sobre el estado reducido.
     *
     * @param current vector de requisitos de voltaje restantes por cumplir.
     * @param parityMaps mapa de paridad -> (resultado exacto -> coste), precalculado.
     * @param cache mapa de memoización (estado -> coste mínimo ya calculado).
     * @return el coste mínimo de pulsaciones para anular el estado actual, o Long.MAX_VALUE si es imposible.
     */
    private long findMinimumPresses(List<Integer> current,
                                    Map<List<Integer>, Map<List<Integer>, Integer>> parityMaps,
                                    Map<List<Integer>, Long> cache) {
        // Si ya se calculó el coste para este estado exacto, se reutiliza (memoización)
        if (cache.containsKey(current)) {
            return cache.get(current);
        }

        // Caso base: todos los requisitos están a cero, no hacen falta más pulsaciones
        if (current.stream().allMatch(v -> v == 0)) {
            return 0;
        }

        // Si algún requisito se ha vuelto negativo, este camino no es válido
        if (current.stream().anyMatch(v -> v < 0)) {
            return Long.MAX_VALUE;
        }

        // Se calcula la paridad del estado actual para buscar patrones compatibles
        List<Integer> currentParity = current.stream()
                .map(v -> v % 2)
                .collect(Collectors.toList());

        // Si no existe ningún patrón precalculado con esa paridad, es imposible continuar
        if (!parityMaps.containsKey(currentParity)) {
            return Long.MAX_VALUE;
        }

        long result = parityMaps.get(currentParity).entrySet().stream()
                // Solo se consideran patrones cuyo efecto no exceda los requisitos restantes
                .filter(entry -> isValidPattern(current, entry.getKey()))
                .mapToLong(entry -> {
                    // Se resta el efecto del patrón y se divide entre 2 (el resto de pulsaciones
                    // deben completarse en pares para mantener la paridad correcta)
                    List<Integer> reduced = reduceState(current, entry.getKey());
                    long subCost = findMinimumPresses(reduced, parityMaps, cache);
                    // Coste total = pulsaciones del patrón elegido + 2 veces el coste del subproblema
                    return subCost == Long.MAX_VALUE ?
                            Long.MAX_VALUE :
                            entry.getValue() + 2L * subCost;
                })
                .min()
                .orElse(Long.MAX_VALUE);

        cache.put(current, result);
        return result;
    }

    /**
     * Comprueba si el patrón dado se puede restar del estado actual sin que
     * ninguna componente quede negativa.
     *
     * @param current vector de requisitos actuales.
     * @param pattern vector de patrón a restar.
     * @return true si el patrón es aplicable (todas sus componentes caben en el estado actual).
     */
    private boolean isValidPattern(List<Integer> current, List<Integer> pattern) {
        return IntStream.range(0, current.size())
                .allMatch(i -> pattern.get(i) <= current.get(i));
    }

    /**
     * Resta el patrón del estado actual y divide el resultado entre 2,
     * generando el estado reducido para la siguiente llamada recursiva.
     *
     * @param current vector de requisitos actuales.
     * @param pattern vector de patrón a restar.
     * @return el nuevo estado reducido.
     */
    private List<Integer> reduceState(List<Integer> current, List<Integer> pattern) {
        return IntStream.range(0, current.size())
                .mapToObj(i -> (current.get(i) - pattern.get(i)) / 2)
                .collect(Collectors.toList());
    }
}
