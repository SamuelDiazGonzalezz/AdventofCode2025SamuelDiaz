package dia8.a;

import java.util.Objects;

/**
 * Representa la posición tridimensional de una caja de conexiones ("junction box")
 * en el puzzle del día 8, usada para calcular distancias entre cajas y formar circuitos.
 *
 * @param x coordenada x
 * @param y coordenada y
 * @param z coordenada z
 */
public record Position(long x, long y, long z) {
    /**
     * Calcula la distancia al cuadrado (sin la raíz cuadrada final) entre esta posición y otra.
     * Se omite la raíz cuadrada porque solo se necesita comparar distancias relativas.
     *
     * @param pos posición con la que calcular la distancia
     * @return la distancia euclídea al cuadrado entre ambas posiciones
     */
    public double distanceTo(Position pos) {
        // Suma de los cuadrados de las diferencias en cada eje (fórmula de distancia euclídea sin la raíz).
        return Math.pow(x - pos.x, 2) + Math.pow(y - pos.y, 2) + Math.pow(z - pos.z, 2);
    }

    /**
     * Compara esta posición con otro objeto, considerando iguales las posiciones con las mismas coordenadas.
     *
     * @param o objeto a comparar
     * @return true si el objeto es una Position con las mismas coordenadas x, y, z
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return x == position.x && y == position.y && z == position.z;
    }

    /**
     * Calcula el código hash de la posición en función de sus tres coordenadas.
     *
     * @return el código hash de la posición
     */
    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }
}
