package dia10.a;

public class Light {
    private LightState state;

    public Light() {
        state = LightState.Off;
    }

    public Light(boolean state) {
        this.state = state ? LightState.On :  LightState.Off;
    }

    public LightState state() {
        return state;
    }

    public void toggle() {
        if (state == LightState.Off) {
            state = LightState.On;
            return;
        }
        state = LightState.Off;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Light light = (Light) o;
        return state == light.state;
    }
}
