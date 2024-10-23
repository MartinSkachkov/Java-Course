package bg.sofia.uni.fmi.mjt.gameplatform.store.item.category;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Game extends Category{
    private String genre;

    public Game(String title, BigDecimal price, LocalDateTime releaseDate, String genre) {
        super(title, price, releaseDate);
        this.genre = genre;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public static void main(String[] args) {
        Game gameSample = new Game("Cyberpunk 2077", new BigDecimal("59.99"), LocalDateTime.of(2020, 12, 10, 0, 0), "RPG");

        // Извеждане на информация за играта
        System.out.println("Title: " + gameSample.getTitle());
        System.out.println("Price: " + gameSample.getPrice());
        System.out.println("Release Date: " + gameSample.getReleaseDate());
        // System.out.println("Genre: " + game.getGenre());
        System.out.println("Rating: " + gameSample.getRating());

        // Оценяване на играта
        gameSample.rate(4.5);
        gameSample.rate(5.0);
        gameSample.rate(3.0);

        // Извеждане на новата оценка
        System.out.println("Updated Rating: " + gameSample.getRating());

        // Промяна на заглавие и цена
        gameSample.setTitle("Cyberpunk 2077 - Enhanced Edition");
        gameSample.setPrice(new BigDecimal("39.99"));

        // Извеждане на обновената информация
        System.out.println("Updated Title: " + gameSample.getTitle());
        System.out.println("Updated Price: " + gameSample.getPrice());
    }
}
