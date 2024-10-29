package bg.sofia.uni.fmi.mjt.gameplatform.store.item.filter;

import bg.sofia.uni.fmi.mjt.gameplatform.store.item.StoreItem;

public class TitleItemFilter implements ItemFilter {
    private final String title;
    private final boolean caseSensitive;

    public TitleItemFilter(String title, boolean caseSensitive) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title cannot be null or blank");
        }

        this.title = title;
        this.caseSensitive = caseSensitive;
    }

    @Override
    public boolean matches(StoreItem item) {
        if (item == null) {
            return false;
        }

        String itemTitle = item.getTitle();
        if (itemTitle == null) {
            return false;
        }

        if (caseSensitive) {
            return itemTitle.contains(title);
        } else {
            return itemTitle.toLowerCase().contains(title.toLowerCase());
        }
    }
}
