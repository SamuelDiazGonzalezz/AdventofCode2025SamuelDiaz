package dia11.b;

import java.util.Objects;

public record CacheKey(Device from, VisitedSet set) {

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CacheKey cacheKey = (CacheKey) o;
        return Objects.equals(from, cacheKey.from) &&
                Objects.equals(set.requiredCount(), cacheKey.set.requiredCount());
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, set.requiredCount());
    }
}
