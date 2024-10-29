package bg.sofia.uni.fmi.mjt.gameplatform.store.item.category;

import bg.sofia.uni.fmi.mjt.gameplatform.store.item.StoreItem;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

public abstract class Category implements StoreItem {
    private String title;
    private BigDecimal price;
    private LocalDateTime releaseDate;
    private int peopleRatedCount = 0;
    private double sumOfRatings = 0;

    public Category(String title, BigDecimal price, LocalDateTime releaseDate){
        this.title = title;
        this.price = price;
        this.releaseDate = releaseDate;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public BigDecimal getPrice() {
        return price.setScale(2, RoundingMode.DOWN);
    }

    @Override
    public void setPrice(BigDecimal price){
        this.price = price;
    }

    @Override
    public LocalDateTime getReleaseDate() {
        return releaseDate;
    }

    @Override
    public void setReleaseDate(LocalDateTime releaseDate) {
        this.releaseDate = releaseDate;
    }

    @Override
    public double getRating() {
        if (peopleRatedCount == 0) {
            return 0;
        }

        return Math.round((sumOfRatings / peopleRatedCount) * 10.0) / 10.0;
    }

    @Override
    public void rate(double rating) {
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5.");
        }
        sumOfRatings += rating;
        peopleRatedCount++;
    }
}
