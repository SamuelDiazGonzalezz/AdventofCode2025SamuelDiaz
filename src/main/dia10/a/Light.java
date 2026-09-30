package dia10.a;

/**
 * Representa una luz individual del panel indicador, con un estado
 * (encendida/apagada) que puede alternarse.
 */
public class Light {
    private LightState state;

    /**
     * Crea una luz apagada por defecto.
     */
    public Light() {
        state = LightState.Off;
    }

    /**
     * Crea una luz con el estado indicado.
     *
     * @param state true para encendida, false para apagada.
     */
    public Light(boolean state) {
        this.state = state ? LightState.On :  LightState.Off;
    }

    /**
     * @return el estado actual de la luz.
     */
    public LightState state() {
        return state;
    }

    /**
     * Alterna el estado de la luz: si está apagada la enciende, y viceversa.
     */
    public void toggle() {
        if (state == LightState.Off) {
            state = LightState.On;
            return;
        }
        state = LightState.Off;
    }

    /**
     * Compara esta luz con otro objeto, considerando iguales dos luces con
     * el mismo estado.
     *
     * @param o el objeto a comparar.
     * @return true si son iguales.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Light light = (Light) o;
        return state == light.state;
    }
}
