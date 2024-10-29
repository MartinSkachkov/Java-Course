package bg.sofia.uni.fmi.mjt.gameplatform.store;

import bg.sofia.uni.fmi.mjt.gameplatform.store.item.StoreItem;
import bg.sofia.uni.fmi.mjt.gameplatform.store.item.filter.ItemFilter;

import java.math.BigDecimal;
import java.util.Arrays;

public class GameStore implements StoreAPI {
    private StoreItem[] availableItems;
    private final String PROMO_CODE_VAN40 = "VAN40";
    private final String PROMO_CODE_100YO = "100YO";
    private final BigDecimal DISCOUNT_VAN40 = new BigDecimal("0.60");
    private final BigDecimal DISCOUNT_100YO = BigDecimal.ZERO;

    private boolean codeVan40Used = false;
    private boolean code100YOUsed = false;

    public GameStore(StoreItem[] availableItems) {
        if (availableItems == null) {
            throw new IllegalArgumentException("Available items array cannot be null");
        }

        this.availableItems = Arrays.copyOf(availableItems, availableItems.length);
    }

    @Override
    public StoreItem[] findItemByFilters(ItemFilter[] itemFilters) {
        if (itemFilters == null) {
            throw new IllegalArgumentException("Item filters array cannot be null");
        }

        int matchingItemsCount = 0;
        boolean matchesAllFilters;

        for (StoreItem item : availableItems) {
            matchesAllFilters = true;

            for (ItemFilter filter : itemFilters) {
                if (filter != null && !filter.matches(item)) {
                    matchesAllFilters = false;
                    break;
                }
            }

            if (matchesAllFilters) {
                matchingItemsCount++;
            }
        }

        StoreItem[] filteredItems = new StoreItem[matchingItemsCount];
        int currentIndex = 0;

        for (StoreItem item : availableItems) {
            matchesAllFilters = true;

            for (ItemFilter filter : itemFilters) {
                if (filter != null && !filter.matches(item)) {
                    matchesAllFilters = false;
                    break;
                }
            }

            if (matchesAllFilters) {
                filteredItems[currentIndex] = item;
                currentIndex++;
            }
        }

        return filteredItems;
    }

    @Override
    public void applyDiscount(String promoCode) {
        if (promoCode == null || promoCode.isBlank()) {
            return;
        }

        if (promoCode.equals(PROMO_CODE_VAN40) && codeVan40Used) {
            return;
        }

        if (promoCode.equals(PROMO_CODE_100YO) && code100YOUsed) {
            return;
        }

        BigDecimal discount;
        switch (promoCode) {
            case PROMO_CODE_VAN40 -> {
                discount = DISCOUNT_VAN40;
                codeVan40Used = true;
            }
            case PROMO_CODE_100YO -> {
                discount = DISCOUNT_100YO;
                code100YOUsed = true;
            }
            default -> discount = null;
        };

        if (discount != null) {
            for (StoreItem item : availableItems) {
                BigDecimal currentPrice = item.getPrice();
                BigDecimal newPrice = currentPrice.multiply(discount);
                item.setPrice(newPrice);
            }
        }
    }

    @Override
    public boolean rateItem(StoreItem item, int rating) {
        if (item == null) {
            return false;
        }

        if (rating < 1 || rating > 5) {
            return false;
        }

        boolean itemExists = false;
        for (StoreItem storeItem : availableItems) {
            if (storeItem == item) {
                itemExists = true;
                break;
            }
        }

        if (!itemExists) {
            return false;
        }

        try {
            item.rate(rating);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
