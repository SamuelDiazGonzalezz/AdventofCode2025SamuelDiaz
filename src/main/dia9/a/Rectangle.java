package dia9.a;

/**
 * Representa un rectángulo definido por dos esquinas opuestas (pos1 y pos2).
 * Permite calcular su área, usada para encontrar el rectángulo más grande formado
 * por pares de puntos de entrada.
 */
public record Rectangle(Position pos1, Position pos2) {

    /**
     * Calcula el área del rectángulo a partir de las dos esquinas opuestas.
     * Se suma 1 a cada diferencia de coordenadas porque ambos extremos (bordes)
     * están incluidos en el rectángulo (no es una distancia exclusiva).
     *
     * @return el área del rectángulo como un valor long (para evitar overflow).
     */
    public long area() {
        // Ancho = diferencia absoluta de x + 1 (incluye ambos bordes)
        // Alto = diferencia absoluta de y + 1 (incluye ambos bordes)
        return (long) (Math.abs(pos1.x() - pos2.x()) + 1) * (Math.abs((pos1.y() - pos2.y())) + 1);
    }

    /**
     * Representación textual del rectángulo, incluyendo sus esquinas y área,
     * útil para depuración.
     *
     * @return cadena descriptiva del rectángulo.
     */
    @Override
    public String toString() {
        return "Rectangle{" + "pos1=" + pos1 + ", pos2=" + pos2 + ", area=" + area() + '}';
    }
}
