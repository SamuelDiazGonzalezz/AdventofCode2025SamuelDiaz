package dia10.a;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Gestiona el panel de luces indicadoras de una máquina del día 10 (parte a):
 * mantiene la lista de luces en su estado actual y la lista de luces en el
 * estado deseado (objetivo a alcanzar).
 */
public class IndicatorLights {
    private final List<Light> lightList;
    private final List<Light> desiredState;

    /**
     * Constructor privado que inicializa las listas internas vacías.
     * Se usa la fábrica estática {@link #create()} para instanciar la clase.
     */
    private IndicatorLights() {
        lightList = new ArrayList<>();
        desiredState = new ArrayList<>();
    }

    /**
     * Crea una nueva instancia vacía de IndicatorLights.
     *
     * @return una nueva instancia.
     */
    public static IndicatorLights create() {
        return new IndicatorLights();
    }

    /**
     * Parsea el patrón de luces deseado a partir de una cadena, donde cada
     * carácter representa una luz ('.' apagada, cualquier otro carácter encendida).
     *
     * @param input cadena con el patrón deseado de luces.
     * @return this, para encadenamiento.
     */
    public IndicatorLights parse(String input) {
        // Separa la cadena en caracteres individuales, uno por luz
        return parse(input.split(""));
    }

    /**
     * Construye el estado deseado de las luces a partir del array de caracteres,
     * y además inicializa la lista de luces actuales (todas apagadas).
     *
     * @param split array de caracteres, uno por cada luz.
     * @return this, para encadenamiento.
     */
    private IndicatorLights parse(String[] split) {
        desiredState.addAll(Arrays.stream(split)
                // '.' significa luz apagada; cualquier otro carácter significa encendida
                .map(str -> str.equals(".") ?
                        new Light() :
                        new Light(true)
                )
                .toList());
        return parse(split.length);
    }

    /**
     * Inicializa la lista de luces actuales con el número de luces indicado,
     * todas apagadas por defecto.
     *
     * @param lights número de luces a crear.
     * @return this, para encadenamiento.
     */
    public IndicatorLights parse(int lights) {
        return parse(IntStream.range(0, lights)
                .mapToObj(_ -> new Light())
                .toList());
    }

    /**
     * Añade la lista de luces dada a la lista de luces actuales.
     *
     * @param list lista de luces a añadir.
     * @return this, para encadenamiento.
     */
    private IndicatorLights parse(List<Light> list) {
        lightList.addAll(list);
        return this;
    }

    /**
     * Obtiene el estado actual de todas las luces como un array.
     *
     * @return array con el estado (On/Off) de cada luz actual.
     */
    public LightState[] getLightsState() {
        return lightList
                .stream()
                .map(Light::state)
                .toArray(LightState[]::new);
    }

    /**
     * @return la lista de luces que representa el estado deseado (objetivo).
     */
    public List<Light>  getDesiredState() {
        return desiredState;
    }
}
