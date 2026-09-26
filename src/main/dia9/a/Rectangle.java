package dia9.a;

public record Rectangle(Position pos1, Position pos2) {

    public long area() {
        return (long) (Math.abs(pos1.x() - pos2.x()) + 1) * (Math.abs((pos1.y() - pos2.y())) + 1);
    }

    @Override
    public String toString() {
        return "Rectangle{" + "pos1=" + pos1 + ", pos2=" + pos2 + ", area=" + area() + '}';
    }
}
