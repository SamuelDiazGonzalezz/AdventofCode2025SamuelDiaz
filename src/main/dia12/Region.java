package dia12;

import java.util.List;

/**
 * Representa una región rectangular del día 12, con su ancho, alto y la
 * lista de formas (regalos) que se deben intentar encajar dentro de ella.
 */
public record Region(int width, int height, List<Shape> presents) {

    /**
     * Calcula el área total de la región.
     *
     * @return el área (ancho x alto) de la región.
     */
    public int area() {
        return width * height;
    }

    /**
     * Calcula la suma de las áreas de todas las formas que hay que encajar
     * en la región. Sirve como comprobación rápida: si esta suma supera el
     * área de la región, es imposible que quepan todas las formas.
     *
     * @return la suma de áreas de todas las formas de la región.
     */
    public int presentsArea() {
        return presents.stream()
                .mapToInt(Shape::area)
                .sum();
    }
}
