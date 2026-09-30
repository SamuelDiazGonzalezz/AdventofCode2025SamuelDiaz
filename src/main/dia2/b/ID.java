package dia2.b;

import java.util.stream.IntStream;

/**
 * Representa un identificador numérico (ID) del problema del día 2, parte B.
 * A diferencia de la parte A, aquí un ID es inválido si está formado por
 * la repetición de cualquier bloque de dígitos (no solo si las dos
 * mitades exactas coinciden), por ejemplo "123123" o "1212".
 *
 * @param number representación en texto del identificador.
 */
public record ID(String number) {

    /**
     * Comprueba si el ID es válido.
     * Un ID de longitud 0 o 1 siempre es válido. Para el resto, se
     * comprueba que no exista ningún bloque repetible (de longitud
     * divisor de la longitud total) cuya repetición reconstruya el número.
     *
     * @return {@code true} si el ID es válido, {@code false} en caso contrario.
     */
    public boolean isValid () {
        return number.length() <= 1 || // los números de longitud 0 o 1 no pueden formarse repitiendo un bloque más corto
                IntStream.range(1, number.length() / 2 + 1)
                        .filter(len -> number.length() % len == 0) // solo consideramos longitudes de bloque que dividen exactamente la longitud total
                        .noneMatch(len -> number.equals(number.substring(0, len).repeat(number.length() / len))); // válido si ningún bloque, repetido, reconstruye el número original
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
