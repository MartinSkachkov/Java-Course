package bg.sofia.uni.fmi.news.api.client;

import bg.sofia.uni.fmi.news.api.config.NewsApiKeyConfig;
import bg.sofia.uni.fmi.news.api.exception.NewsApiException;
import bg.sofia.uni.fmi.news.api.query.NewsQueryBuilder;
import bg.sofia.uni.fmi.news.api.response.Article;
import bg.sofia.uni.fmi.news.api.response.NewsApiResponse;
import bg.sofia.uni.fmi.news.util.ApiKeyLoader;
import bg.sofia.uni.fmi.news.util.Category;
import bg.sofia.uni.fmi.news.util.Country;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class NewsApiClientTest {
    @Mock
    private HttpClient httpClient;

    @Mock
    private HttpResponse<String> httpResponse;

    private NewsApiClient newsApiClient;

    @BeforeEach
    void setUp() {
        try (MockedStatic<ApiKeyLoader> mockedApiKeyLoader = mockStatic(ApiKeyLoader.class)) {
            mockedApiKeyLoader.when(ApiKeyLoader::loadApiKey).thenReturn("123");
            newsApiClient = new NewsApiClientImpl(new NewsApiKeyConfig(ApiKeyLoader.loadApiKey()), httpClient);
        }
    }

    @Test
    void testSuccessfulApiCall() throws Exception {
        String successResponse = """
                {
                    "status": "ok",
                    "totalResults": 1,
                    "articles": [{
                        "source": {"id": "test-source", "name": "Test Source"},
                        "author": "Test Author",
                        "title": "Test Title",
                        "description": "Test Description",
                        "url": "http://test.com",
                        "urlToImage": "http://test.com/image.jpg",
                        "publishedAt": "2024-01-17T12:00:00Z",
                        "content": "Test Content"
                    }]
                }
                """;

        when(httpResponse.statusCode()).thenReturn(200);
        when(httpResponse.body()).thenReturn(successResponse);
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenReturn(httpResponse);

        NewsQueryBuilder query = new NewsQueryBuilder()
                .withCountry(Country.fromCode("us"))
                .withCategory(Category.TECHNOLOGY);

        NewsApiResponse response = newsApiClient.getTopHeadlines(query);

        assertNotNull(response, "Response should not be null");
        assertEquals("ok", response.status(), "The status should be 'ok'");
        assertEquals(1, response.totalResults(), "Total results should be 1");
        assertEquals(1, response.articles().size(), "There should be exactly 1 article in the response");

        Article article = response.articles().getFirst();
        assertEquals("Test Title", article.title(), "The article title should be 'Test Title'");
        assertEquals("Test Author", article.author(), "The article author should be 'Test Author'");
    }

    @Test
    void testErrorАpiCall() throws Exception {
        String errorResponse = """
                {
                    "status": "error",
                    "code": "apiKeyInvalid",
                    "message": "Your API key is invalid"
                }
                """;

        when(httpResponse.statusCode()).thenReturn(401);
        when(httpResponse.body()).thenReturn(errorResponse);
        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenReturn(httpResponse);

        NewsQueryBuilder query = new NewsQueryBuilder()
                .withCountry(Country.fromCode("us"));

        assertThrows(NewsApiException.class, () -> newsApiClient.getTopHeadlines(query),
                "Calling the API with invalid API key should throw a NewsApiException");
    }
}
