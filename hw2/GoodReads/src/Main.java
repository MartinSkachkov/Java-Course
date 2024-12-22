import bg.sofia.uni.fmi.mjt.goodreads.book.Book;
import bg.sofia.uni.fmi.mjt.goodreads.recommender.BookRecommender;
import bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.SimilarityCalculator;
import bg.sofia.uni.fmi.mjt.goodreads.recommender.similaritycalculator.genres.GenresOverlapSimilarityCalculator;
import bg.sofia.uni.fmi.mjt.goodreads.tokenizer.TextTokenizer;

import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;

public class Main {
    public static void main(String[] args) {
        // Пътят до вашия файл
        String filePath = "C:\\Users\\Marto\\IdeaProjects\\GoodReads\\src\\goodreads_data_copy.csv";
        String stopwords = "C:\\Users\\Marto\\IdeaProjects\\GoodReads\\src\\stopwords_update.txt";

//        try (FileReader reader = new FileReader(filePath)) {
//            // Извикване на BookLoader.load
//            Set<Book> books = BookLoader.load(reader);
//
//            // Отпечатване на резултата
//            books.forEach(book -> System.out.println(book.genres()));
//
//        } catch (IOException e) {
//            System.err.println("Error reading file: " + e.getMessage());
//        } catch (IllegalArgumentException e) {
//            System.err.println("Invalid input: " + e.getMessage());
//        }

        //Book[ID=17, title=The Giver (The Giver, #1), author=Lois Lowry, description=At the age of twelve, Jonas, a young boy from a seemingly utopian, futuristic world,
        // is singled out to receive special training from The Giver, who alone holds the memories of the true joys and pain of life.,
        // genres=['Young Adult', 'Fiction', 'Classics', 'Dystopia', 'Science Fiction', 'Fantasy', 'School'], rating=4.12, ratingCount=2285401, URL=https://www.goodreads.com/book/show/3636.The_Giver]

        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        try (FileReader reader = new FileReader(stopwords)) {
            TextTokenizer tt = new TextTokenizer(reader);
            System.out.println(tt.stopwords());

            System.out.println(tt.tokenize("MARTO . about ABOve a?a  user-friendly   haven't again where's   all a,m an i'd  агубамуба    any i'll aren't as\n"));
            System.out.println(tt.tokenize(""));
            System.out.println(tt.tokenize("Do You Do What You Do Best Every Day?Chances are, you don't."));
            //System.out.println(tt.formatStopwords());

            System.out.println(tt.tokenize("don't"));
            System.out.println(tt.tokenize("dDDn't"));
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid input: " + e.getMessage());
        }

        // [very, been, don't, about, what's, couldn't, during, your, when, haven't, ...]

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        // Примерни книги
        Book book1 = new Book("1", "Book One", "Author A", "Description A",
                Arrays.asList("Fiction", "Drama", "Adventure", "Thrill", "Comedy"), 4.5, 500, "url1");
        Book book2 = new Book("2", "Book Two", "Author B", "Descript.ion Bdasd  don't I sdsd sdaaa ssvfvg vcv!",
                Arrays.asList("Fiction", "Fantasy", "Literature", "Children", "Adults"), 4.2, 300, "url2");

        //System.out.println(book1.parseGenres(book1.genres().toString()));

        // Създаваме обект за изчисляване на сходство
        GenresOverlapSimilarityCalculator calculator = new GenresOverlapSimilarityCalculator();

        // Изчисляваме сходството между двете книги
        double similarity = calculator.calculateSimilarity(book1, book2);
        System.out.println("Genres Similarity: " + similarity); // between 0 and 1

        try (FileReader reader = new FileReader(stopwords)) {
            TextTokenizer tt = new TextTokenizer(reader);


            System.out.println(tt.tokenize(book2.description())); // [descript, ion, bdasd, sdsd, sdaaa, ssvfvg, vcv]
            System.out.println(tt.tokenize(book2.title()));
        } catch (Exception e) {
            System.out.println(e);
        }

        ////////////////////////////////////////////////////////////////////////////////////////////////////////////

//        Book bookX = new Book("1", "Book X", "Author A", "academy superhero club superhero", Arrays.asList("Fiction", "Drama", "Adventure", "Thrill", "Comedy"), 4.5, 500, "url1");
//        Book bookY = new Book("2", "Book Y", "Author B", "superhero mission save club", Arrays.asList("Fiction", "Drama", "Adventure", "Thrill", "Comedy"), 4.5, 500, "url1");
//        Book bookZ = new Book("3", "Book Z", "Author C", "crime murder mystery club", Arrays.asList("Fiction", "Drama", "Adventure", "Thrill", "Comedy"), 4.5, 500, "url1");
//
//        Set<Book> books = new HashSet<>();
//        books.add(bookX);
//        books.add(bookY);
//        books.add(bookZ);
//
//        try (FileReader reader = new FileReader(stopwords)) {
//            TextTokenizer tt = new TextTokenizer(reader);
//            TFIDFSimilarityCalculator calc = new TFIDFSimilarityCalculator(books, tt);
//            Map<String, Double> tfScores = calc.computeTF(bookX);
//            System.out.println(tfScores.get("academy")); // 0.25
//            System.out.println(tfScores.get("superhero")); // 0.5
//            System.out.println(tfScores.get("club")); // 0.25
//
//            Map<String, Double> idfScores = calc.computeIDF(bookX);
//            System.out.println(idfScores.get("superhero")); // 0.17609125905568124
//
//            Map<String, Double> tfidfScores = calc.computeTFIDF(bookX);
//            System.out.println("res: " + tfidfScores.get("superhero")); // res: 0.08804562952784062
//        } catch (Exception e) {
//            System.out.println(e);
//        }

        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////

//        try (FileReader reader = new FileReader(stopwords)) {
//            TextTokenizer tt = new TextTokenizer(reader);
//            TFIDFSimilarityCalculator tfidfCalculator = new TFIDFSimilarityCalculator(Set.of(bookX, bookY), tt);
//            GenresOverlapSimilarityCalculator genresCalculator = new GenresOverlapSimilarityCalculator();
//
//            // Създаване на CompositeSimilarityCalculator
//            Map<SimilarityCalculator, Double> calculators = new HashMap<>();
//            calculators.put(tfidfCalculator, 0.7); // TF-IDF със 70% тежест
//            calculators.put(genresCalculator, 0.3); // GenresOverlap със 30% тежест
//
//            CompositeSimilarityCalculator compositeCalculator = new CompositeSimilarityCalculator(calculators);
//
//            // Изчисляване на общото сходство
//            double sim = compositeCalculator.calculateSimilarity(bookX, bookY);
//            System.out.println("Composite Similarity: " + sim);
//        } catch (Exception e) {
//            System.out.println(e);
//        }

        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        // Създаване на книги
        String[] tokensBookX = {
                "1", "Book X", "Author A", "academy superhero club superhero", "[Action, Superhero, Drama]", "4.5", "500", "url1"
        };
        String[] tokensBookY = {
                "2", "Book Y", "Author B", "superhero mission save club", "[Action, Adventure, Superhero]", "4.2", "300", "url2"
        };
        String[] tokensBookZ = {
                "3", "Book Z", "Author C", "crime murder mystery club", "[Crime, Mystery, Thriller, Drama]", "4.0", "400", "url3"
        };

        Book bookX = Book.of(tokensBookX);
        Book bookY = Book.of(tokensBookY);
        Book bookZ = Book.of(tokensBookZ);

        // Създаване на сет с книги
        Set<Book> books = new HashSet<>();
        books.add(bookX);
        books.add(bookY);
        books.add(bookZ);

        // Избиране на калкулатор за сходство (например по жанрове)
        SimilarityCalculator calculator1 = new GenresOverlapSimilarityCalculator();

        // Създаване на препоръчваща система
        BookRecommender recommender = new BookRecommender(books, calculator1);

        // Препоръка на книги за "Book X"
        SortedMap<Book, Double> recommendations = recommender.recommendBooks(bookX, 1);

        // Извеждане на резултатите
        System.out.println("Recommended books based on 'Book X':");
        recommendations.forEach((book, similarity1) -> {
            System.out.println("Book: " + book.title() + ", Similarity: " + similarity1);
        });

        String s = List.of(
                "Classics", "Childrens", "Fiction", "Fantasy", "Animals", "Middle Grade", "Audiobook").toString();
        System.out.println(s);
        System.out.println();
    }
}
