package software.ulpgc.day05.b;

public record Range(long start, long end) {
    public long count () {
        return (end-start)+1;
    }
}
