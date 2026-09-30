package dia2.a;

/**
 * Representa un identificador numérico (ID) del problema del día 2, parte A.
 * Un ID es inválido si empieza por '0', o si tiene longitud par y su
 * primera mitad es exactamente igual a su segunda mitad.
 *
 * @param number representación en texto del identificador.
 */
public record ID(String number) {

    /**
     * Comprueba si el ID es válido.
     * Un ID NO es válido si empieza por "0", o si tiene longitud par
     * y ambas mitades del número son iguales (patrón repetido).
     *
     * @return {@code true} si el ID es válido, {@code false} en caso contrario.
     */
    public boolean isValid () {
        return !number.startsWith("0") && // los IDs que empiezan por 0 nunca son válidos
                (number.length() % 2 == 1 || // si la longitud es impar, no puede tener dos mitades iguales, así que es válido
                        !number.substring(0, number.length()/2) // compara la primera mitad...
                                .equals(number.substring(number.length()/2))); // ...con la segunda mitad
    }

    /**
     * Convierte el ID (texto) a su valor numérico.
     *
     * @return el valor numérico del ID.
     */
    public long longNumber () {
        return Long.parseLong(number());
    }
}
