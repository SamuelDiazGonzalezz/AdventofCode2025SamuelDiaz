package dia10.a;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Gestiona el conjunto de máquinas leídas de la entrada del día 10 (parte a)
 * y suma el número mínimo de pulsaciones necesarias para cada una.
 */
public class MachineManager {
    private final List<Machine> machineList;

    /**
     * Constructor privado que inicializa la lista de máquinas vacía.
     * Se usa la fábrica estática {@link #create()} para instanciar la clase.
     */
    private MachineManager() {
        this.machineList = new ArrayList<>();
    }

    /**
     * Crea una nueva instancia vacía de MachineManager.
     *
     * @return una nueva instancia.
     */
    public static MachineManager create() {
        return new MachineManager();
    }

    /**
     * Parsea la entrada completa, donde cada línea describe una máquina.
     *
     * @param input texto completo de entrada.
     * @return this, para encadenamiento.
     */
    public MachineManager parse(String input) {
        machineList.addAll(Arrays.stream(input.split("\n"))
                .map(Machine::create)
                .toList());
        return this;
    }

    /**
     * Suma el número mínimo de pulsaciones necesarias de todas las máquinas.
     *
     * @return la suma total de pasos mínimos de todas las máquinas.
     */
    public int result() {
        return machineList.stream().mapToInt(Machine::desiredStateSteps).sum();
    }
}
