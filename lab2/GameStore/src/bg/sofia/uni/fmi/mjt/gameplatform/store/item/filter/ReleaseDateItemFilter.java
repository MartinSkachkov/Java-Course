package bg.sofia.uni.fmi.mjt.gameplatform.store.item.filter;

import bg.sofia.uni.fmi.mjt.gameplatform.store.item.StoreItem;
import java.time.LocalDateTime;

public class ReleaseDateItemFilter implements ItemFilter {
    private final LocalDateTime lowerBound;
    private final LocalDateTime upperBound;

    public ReleaseDateItemFilter(LocalDateTime lowerBound, LocalDateTime upperBound) {
        if (lowerBound == null || upperBound == null) {
            throw new IllegalArgumentException("Date bounds cannot be null");
        }

        if (lowerBound.isAfter(upperBound)) {
            throw new IllegalArgumentException("Lower bound cannot be after upper bound");
        }

        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    @Override
    public boolean matches(StoreItem item) {
        if (item == null) {
            return false;
        }

        LocalDateTime releaseDate = item.getReleaseDate();
        return !releaseDate.isBefore(lowerBound) && !releaseDate.isAfter(upperBound);
    }


}
