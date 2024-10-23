package bg.sofia.uni.fmi.mjt.gameplatform.store.item.filter;

import bg.sofia.uni.fmi.mjt.gameplatform.store.item.StoreItem;

public class RatingItemFilter implements ItemFilter {
    private final double rating;

    public RatingItemFilter(double rating) {
        if (rating < 0 || rating > 5){
            throw new IllegalArgumentException("Rating cannot be negative");
        }

        this.rating = rating;
    }

    @Override
    public boolean matches(StoreItem item) {
        if (item == null) {
            return false;
        }

        return item.getRating() >= rating;
    }
}
