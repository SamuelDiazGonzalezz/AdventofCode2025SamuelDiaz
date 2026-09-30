package dia3.a;

import java.util.List;

/**
 * Representa un banco de baterías (una lista de dígitos) del problema del
 * día 3, parte A. Permite calcular el número más grande de dos cifras que
 * se puede formar eligiendo el mayor dígito de la primera mitad del banco
 * y el mayor dígito de lo que queda después de él.
 *
 * @param batteries lista de dígitos (baterías) del banco.
 */
public record BatteryBank(List<Integer> batteries)  {

    /**
     * Calcula el resultado del banco de baterías combinando los dos
     * dígitos más "relevantes" (según las reglas del problema) en un
     * único número.
     *
     * @return el número resultante de combinar los dígitos elegidos.
     */
    public int sum() {
        if (batteries.isEmpty()) return 0; // sin baterías no hay nada que sumar
        if (batteries.size() == 1) return batteries.getFirst(); // con una sola batería, el resultado es ella misma
        if (batteries.size() == 2) return joinChars(batteries.getFirst(), batteries.getLast()); // con dos, se combinan directamente

        return joinChars(
                getMaxNum(batteries.subList(0, batteries.size()-1)), // mayor dígito entre todos menos el último
                getMaxNum(batteries.subList(batteries.indexOf(
                        getMaxNum(
                                batteries.subList(0, batteries.size()-1)))+1, // posición justo después de encontrar ese máximo
                                batteries.size()
                        )
                ) // mayor dígito de lo que queda tras el máximo encontrado
        );
    }

    /**
     * Obtiene el valor máximo de una lista de dígitos.
     *
     * @param batteries lista de dígitos sobre la que buscar el máximo.
     * @return el valor máximo encontrado, o 0 si la lista está vacía.
     */
    private int getMaxNum(List<Integer> batteries) {
        if  (batteries.isEmpty()) return 0; // si no quedan baterías, se considera que el máximo es 0
        return batteries.stream().max(Integer::compare).get();
    }

    /**
     * Combina dos números concatenando su representación en texto y
     * volviendo a parsear el resultado como entero (por ejemplo, 3 y 7
     * se combinan en 37).
     *
     * @param integer primer dígito.
     * @param integer1 segundo dígito.
     * @return el número resultante de concatenar ambos dígitos.
     */
    private int joinChars(Integer integer, Integer integer1) {
        return Integer.parseInt(integer.toString() + integer1.toString());
    }
}
