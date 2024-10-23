package bg.sofia.uni.fmi.mjt.gameplatform.store.item.filter;

import bg.sofia.uni.fmi.mjt.gameplatform.store.item.StoreItem;

import java.math.BigDecimal;

public class PriceItemFilter implements ItemFilter{
    private final BigDecimal lowerBound;
    private final BigDecimal upperBound;

    public PriceItemFilter(BigDecimal lowerBound, BigDecimal upperBound) {
        if (lowerBound == null || upperBound == null) {
            throw new IllegalArgumentException("Price bounds cannot be null");
        }

        if (lowerBound.compareTo(upperBound) > 0) {
            throw new IllegalArgumentException("Lower bound cannot be greater than upper bound");
        }

        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    @Override
    public boolean matches(StoreItem item) {
        if (item == null) {
            return false;
        }

        BigDecimal price = item.getPrice();
        return price.compareTo(lowerBound) >= 0 && price.compareTo(upperBound) <= 0;
    }
}
