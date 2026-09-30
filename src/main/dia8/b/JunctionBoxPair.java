package dia8.b;

import java.util.Objects;

/**
 * Representa un par de cajas de conexiones ("junction boxes") candidatas a ser conectadas,
 * junto con la distancia que las separa. Es comparable por distancia, lo que permite
 * ordenar los pares de menor a mayor distancia para conectarlos en ese orden (similar a
 * un algoritmo tipo Kruskal para formar los circuitos).
 *
 * @param junctionBox1 primera caja del par
 * @param junctionBox2 segunda caja del par
 * @param distance     distancia entre ambas cajas
 */
public record JunctionBoxPair(Position junctionBox1, Position junctionBox2, double distance) implements Comparable<JunctionBoxPair> {
    /**
     * Compara este par con otro objeto, considerando iguales los pares con las mismas cajas y distancia.
     *
     * @param o objeto a comparar
     * @return true si el objeto es un JunctionBoxPair con las mismas cajas y distancia
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        JunctionBoxPair that = (JunctionBoxPair) o;
        return distance == that.distance && junctionBox1.equals(that.junctionBox1) && junctionBox2.equals(that.junctionBox2);
    }

    /**
     * Calcula el código hash del par en función de sus dos cajas y la distancia.
     *
     * @return el código hash del par
     */
    @Override
    public int hashCode() {
        return Objects.hash(junctionBox1, junctionBox2, distance);
    }

    /**
     * Compara este par con otro según su distancia, para poder ordenar los pares de menor a mayor.
     *
     * @param o otro par con el que comparar
     * @return un valor negativo, cero o positivo según si la distancia de este par es menor,
     *         igual o mayor que la del otro
     */
    @Override
    public int compareTo(JunctionBoxPair o) {
        return Double.compare(distance, o.distance);
    }
}
