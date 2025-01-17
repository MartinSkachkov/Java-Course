package bg.sofia.uni.fmi.news.api.query;

import bg.sofia.uni.fmi.news.util.Category;
import bg.sofia.uni.fmi.news.util.Country;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class NewsQueryBuilderTest {

    @Test
    void testBasicQueryBuild() {
        NewsQueryBuilder builder = new NewsQueryBuilder()
                .withKeywords("bitcoin")
                .withCategory(Category.BUSINESS)
                .withCountry(Country.fromCode("us"))
                .withPageSize(20)
                .withPage(1);

        String query = builder.build();
        assertTrue(query.contains("q=bitcoin"), "Query should contain the keyword 'bitcoin'");
        assertTrue(query.contains("category=business"), "Query should contain the category 'business'");
        assertTrue(query.contains("country=us"), "Query should contain the country code 'us'");
        assertTrue(query.contains("pageSize=20"), "Query should contain the pageSize of 20");
        assertTrue(query.contains("page=1"), "Query should contain page number 1");
    }

    @Test
    void testSourcesQueryBuild() {
        NewsQueryBuilder builder = new NewsQueryBuilder()
                .withSources("bbc-news", "cnn")
                .withPageSize(10);

        String query = builder.build();
        assertTrue(query.contains("sources=bbc-news,cnn"), "Query should contain sources 'bbc-news' and 'cnn'");
        assertTrue(query.contains("pageSize=10"), "Query should contain pageSize of 10");
    }

    @Test
    void testInvalidCombinationSourcesWithCountry() {
        NewsQueryBuilder builder = new NewsQueryBuilder()
                .withSources("bbc-news");

        assertThrows(IllegalStateException.class, () ->
                        builder.withCountry(Country.fromCode("us")),
                "You can't mix country param with the sources param, but somehow did");
    }

    @Test
    void testInvalidCombinationSourcesWithCategory() {
        NewsQueryBuilder builder = new NewsQueryBuilder()
                .withSources("bbc-news");

        assertThrows(IllegalStateException.class, () ->
                        builder.withCategory(Category.BUSINESS),
                "You can't mix sources param with the category param, but somehow did");
    }

    @Test
    void testInvalidPageSizeZero() {
        NewsQueryBuilder builder = new NewsQueryBuilder();
        assertThrows(IllegalArgumentException.class, () ->
                builder.withPageSize(0), "Page size must be greater than zero.");
    }

    @Test
    void testInvalidPageSizeNegative() {
        NewsQueryBuilder builder = new NewsQueryBuilder();
        assertThrows(IllegalArgumentException.class, () ->
                builder.withPageSize(-1), "Page size cannot be negative.");
    }

    @Test
    void testInvalidPageSizeAboveMax() {
        NewsQueryBuilder builder = new NewsQueryBuilder();
        assertThrows(IllegalArgumentException.class, () ->
                builder.withPageSize(101), "Page size cannot exceed 100.");
    }

    @Test
    void testInvalidPageZero() {
        NewsQueryBuilder builder = new NewsQueryBuilder();
        assertThrows(IllegalArgumentException.class, () ->
                builder.withPage(0), "Page number must be greater than zero.");
    }

    @Test
    void testInvalidPageNegative() {
        NewsQueryBuilder builder = new NewsQueryBuilder();
        assertThrows(IllegalArgumentException.class, () ->
                builder.withPage(-1), "Page number cannot be negative.");
    }

    @Test
    void testEmptyKeywords() {
        NewsQueryBuilder builder = new NewsQueryBuilder();
        assertThrows(IllegalArgumentException.class, () ->
                builder.withKeywords(""), "Keywords cannot be empty.");
    }

    @Test
    void testBlankKeywords() {
        NewsQueryBuilder builder = new NewsQueryBuilder();
        assertThrows(IllegalArgumentException.class, () ->
                builder.withKeywords("   "), "Keywords cannot be blank.");
    }

    @Test
    void testNullCategory() {
        NewsQueryBuilder builder = new NewsQueryBuilder();
        assertThrows(IllegalArgumentException.class, () ->
                builder.withCategory(null), "Category cannot be null.");
    }

    @Test
    void testNullCountry() {
        NewsQueryBuilder builder = new NewsQueryBuilder();
        assertThrows(IllegalArgumentException.class, () ->
                builder.withCountry(null), "Country cannot be null.");
    }

    @Test
    void testNullKeywords() {
        NewsQueryBuilder builder = new NewsQueryBuilder();
        assertThrows(IllegalArgumentException.class, () ->
                builder.withKeywords(null), "Keywords cannot be null.");
    }

}