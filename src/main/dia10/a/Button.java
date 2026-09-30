package dia10.a;

import java.util.Set;

/**
 * Representa un botón de la máquina del día 10 (parte a). Al pulsarlo,
 * cambia el estado (encendido/apagado) de las luces indicadas en {@code lights}.
 */
public record Button(Set<Integer> lights) {
}
