package dia11.b;

import java.util.Objects;

/**
 * Clave de memoización usada en la búsqueda de caminos del día 11 (parte b).
 * Combina el dispositivo actual ({@code from}) con el conjunto de dispositivos
 * obligatorios ya visitados, representado de forma compacta mediante
 * {@link VisitedSet#requiredCount()} en lugar del conjunto completo, ya que solo
 * importa cuántos de los dispositivos obligatorios se han visitado, no el resto
 * del historial de visitados.
 */
public record CacheKey(Device from, VisitedSet set) {

    /**
     * Dos claves son iguales si corresponden al mismo dispositivo y al mismo
     * número de dispositivos obligatorios ya visitados (no se compara todo el
     * conjunto de visitados, solo ese contador, para maximizar la reutilización
     * de la caché).
     *
     * @param o el objeto a comparar.
     * @return true si representan el mismo estado relevante para la memoización.
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CacheKey cacheKey = (CacheKey) o;
        return Objects.equals(from, cacheKey.from) &&
                Objects.equals(set.requiredCount(), cacheKey.set.requiredCount());
    }

    /**
     * Calcula el hash en consonancia con {@link #equals(Object)}, usando el
     * dispositivo y el número de dispositivos obligatorios visitados.
     *
     * @return el código hash de la clave.
     */
    @Override
    public int hashCode() {
        return Objects.hash(from, set.requiredCount());
    }
}
