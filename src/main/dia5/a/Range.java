package software.ulpgc.day05.a;

public record Range(long start, long end) {
    public boolean isInRange(long ingredientId) {
        return start <= ingredientId && end >= ingredientId;
    }
}
