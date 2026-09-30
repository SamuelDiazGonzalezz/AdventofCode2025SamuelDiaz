package dia8.b;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Mantiene el conjunto de circuitos (grupos de cajas conectadas) del puzzle del día 8 (parte b).
 * Cada caja empieza en su propio circuito, y al añadir pares de cajas conectadas los
 * circuitos correspondientes se van fusionando, similar a una estructura "union-find".
 */
public class CircuitSet {
    private final List<Set<Position>> circuits;

    /**
     * Inicializa un circuito independiente por cada caja de conexiones, de forma que
     * al principio ninguna caja está conectada con otra.
     *
     * @param junctionBoxes lista de todas las cajas de conexiones del puzzle
     */
    CircuitSet(List<Position> junctionBoxes) {
        circuits = new ArrayList<>();
        junctionBoxes.forEach(junctionBox -> {
            Set<Position> circuitItem = new HashSet<>();
            circuitItem.add(junctionBox);
            circuits.add(circuitItem);
        });
    }

    /**
     * Conecta las dos cajas del par indicado, fusionando los circuitos a los que pertenecen
     * si aún no estaban en el mismo circuito.
     *
     * @param junctionBoxPair par de cajas a conectar
     */
    public void add(JunctionBoxPair junctionBoxPair) {
        // Se busca el índice del circuito que contiene la primera caja del par.
        int i1 = circuits.indexOf(circuits
                .stream()
                .filter(junctionBox -> junctionBox.contains(junctionBoxPair.junctionBox1()))
                .findFirst()
                .orElse(null));
        // Se busca el índice del circuito que contiene la segunda caja del par.
        int i2 = circuits.indexOf(circuits
                .stream()
                .filter(junctionBox -> junctionBox.contains(junctionBoxPair.junctionBox2()))
                .findFirst()
                .orElse(null));

        // Si las dos cajas pertenecen a circuitos distintos, se fusionan en uno solo.
        if (i1 != i2) {
            circuits.get(i1).addAll(circuits.get(i2));
            circuits.remove(i2);
        }
    }

    /**
     * Devuelve el número de circuitos independientes existentes actualmente.
     *
     * @return cantidad de circuitos
     */
    public int size() {
        return circuits.size();
    }

    /**
     * Obtiene los tres circuitos con mayor número de cajas conectadas.
     *
     * @return lista con los tres circuitos más grandes, ordenados de mayor a menor tamaño
     */
    public List<Set<Position>> getThreeLargest() {
        return circuits
                .stream()
                // Se ordenan de mayor a menor tamaño (orden descendente).
                .sorted((c1, c2) -> Integer.compare(c2.size(), c1.size()))
                .limit(3)
                .toList();
    }
}
