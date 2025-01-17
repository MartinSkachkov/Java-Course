package bg.sofia.uni.fmi.news.api.client;

import bg.sofia.uni.fmi.news.api.exception.NewsApiException;
import bg.sofia.uni.fmi.news.api.query.NewsQueryBuilder;
import bg.sofia.uni.fmi.news.api.response.NewsApiResponse;

/**
 * Main interface for interacting with the News API.
 * Provides synchronous method for fetching news articles.
 */
public interface NewsApiClient {
    /**
     * Executes the news query asynchronously.
     *
     * @param queryBuilder The query builder containing search parameters
     * @return A NewsApiResponse containing the API response
     * @throws NewsApiException if the request fails
     */
    NewsApiResponse getTopHeadlines(NewsQueryBuilder queryBuilder) throws NewsApiException;
}
