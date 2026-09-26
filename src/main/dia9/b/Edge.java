package dia9.b;

public record Edge(Position start, Position end) {

    boolean cutsThrough(Rectangle rectangle) {
        return isVertical() ? cutsThroughVertically(rectangle) : cutsThroughHorizontally(rectangle);
    }

    private boolean cutsThroughVertically(Rectangle rectangle) {
        return x() > rectangle.minX() && x() < rectangle.maxX() &&
                minY() < rectangle.maxY() && maxY() > rectangle.minY();
    }

    private boolean cutsThroughHorizontally(Rectangle rectangle) {
        return y() > rectangle.minY() && y() < rectangle.maxY() &&
                minX() < rectangle.maxX() && maxX() > rectangle.minX();
    }

    boolean contains(Position point) {
        return isInBounds(point) && (isVertical() ?
                point.x() == x() : point.y() == y());
    }

    private boolean isInBounds(Position point) {
        return point.x() >= minX() && point.x() <= maxX() &&
                point.y() >= minY() && point.y() <= maxY();
    }

    boolean rayCrosses(Position point) {
        return isYBetween(point) && isLeftOfIntersection(point);
    }

    private boolean isYBetween(Position point) {
        return (start.y() > point.y()) != (end.y() > point.y());
    }

    private boolean isLeftOfIntersection(Position point) {
        return point.x() < xIntersection(point);
    }

    private double xIntersection(Position point) {
        return start.x() + (double) (end.x() - start.x()) * (point.y() - start.y()) /
                (end.y() - start.y());
    }

    private boolean isVertical() {
        return start.x() == end.x();
    }

    private int x() {
        return start.x();
    }

    private int y() {
        return start.y();
    }

    private int minX() {
        return Math.min(start.x(), end.x());
    }

    private int maxX() {
        return Math.max(start.x(), end.x());
    }

    private int minY() {
        return Math.min(start.y(), end.y());
    }

    private int maxY() {
        return Math.max(start.y(), end.y());
    }
}