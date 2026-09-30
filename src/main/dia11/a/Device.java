package dia11.a;

import java.util.List;
import java.util.Objects;

/**
 * Representa un dispositivo del rack de servidores del día 11 (parte a),
 * identificado por su etiqueta y con una lista de etiquetas de los
 * dispositivos a los que está conectado (salidas).
 */
public record Device(String label, List<String> outputs) {

    /**
     * Representación textual del dispositivo: su etiqueta seguida de sus salidas.
     *
     * @return cadena descriptiva del dispositivo.
     */
    @Override
    public String toString() {
        return label + "→"  + String.join(", ", outputs);
    }

    /**
     * Dos dispositivos se consideran iguales si tienen la misma etiqueta,
     * independientemente de sus salidas (necesario para usarlos en
     * estructuras como Set/Map basadas en identidad lógica por nombre).
     *
     * @param o el objeto a comparar.
     * @return true si tienen la misma etiqueta.
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Device device = (Device) o;
        return Objects.equals(label, device.label);
    }

    /**
     * Calcula el hash basándose únicamente en la etiqueta, en consonancia
     * con la redefinición de {@link #equals(Object)}.
     *
     * @return el código hash del dispositivo.
     */
    @Override
    public int hashCode() {
        return Objects.hashCode(label);
    }


}
