package dia5.b;

public record Range(long start, long end) {
    public long count () {
        return (end-start)+1;
    }
}
