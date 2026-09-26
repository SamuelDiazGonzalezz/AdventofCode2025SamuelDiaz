package dia12;

import java.util.List;

public record Region(int width, int height, List<Shape> presents) {

    public int area() {
        return width * height;
    }

    public int presentsArea() {
        return presents.stream()
                .mapToInt(Shape::area)
                .sum();
    }
}
