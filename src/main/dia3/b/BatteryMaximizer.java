package dia3.b;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Acumula varios {@link BatteryBank} (uno por cada línea de entrada) y
 * permite calcular la suma total de sus resultados individuales, para
 * el problema del día 3, parte B.
 */
public class BatteryMaximizer {
    private final List<BatteryBank> batteries;

    /**
     * Crea una nueva instancia vacía de {@link BatteryMaximizer}.
     *
     * @return un nuevo maximizador sin bancos de baterías.
     */
    public static BatteryMaximizer create() {
        return new BatteryMaximizer();
    }

    /**
     * Constructor privado: inicializa la lista de bancos de baterías vacía.
     * Se usa el patrón de fábrica estática {@link #create()} en su lugar.
     */
    private BatteryMaximizer() {
        this.batteries = new ArrayList<>();
    }

    /**
     * Construye bancos de baterías a partir de una lista de cadenas de
     * texto, donde cada carácter numérico de cada línea representa un dígito.
     *
     * @param batteryBankList lista de líneas de texto, cada una con los
     *                         dígitos de un banco de baterías.
     * @return el propio maximizador, para poder encadenar llamadas.
     */
    public BatteryMaximizer fromString(List<String> batteryBankList) {
        return add(batteryBankList
                .stream()
                .map(String::trim) // elimina espacios en blanco sobrantes de cada línea
                .map(bB -> new BatteryBank(Arrays
                        .stream(bB.split("")) // separa la línea en caracteres individuales
                        .mapToInt(Integer::parseInt) // convierte cada carácter en su dígito numérico
                        .boxed()
                        .collect(Collectors.toList())))
                .collect(Collectors.toList())
        );
    }

    /**
     * Añade una lista de bancos de baterías ya construidos.
     *
     * @param batteryBankList lista de bancos de baterías a añadir.
     * @return el propio maximizador, para poder encadenar llamadas.
     */
    public BatteryMaximizer add(List<BatteryBank> batteryBankList) {
        batteries.addAll(batteryBankList);
        return this;
    }

    /**
     * Suma los resultados individuales de todos los bancos de baterías
     * almacenados.
     *
     * @return la suma total de los resultados de todos los bancos.
     */
    public long sum() {
        return batteries.stream().mapToLong(BatteryBank::sum).reduce(0, Long::sum);
    }
}
