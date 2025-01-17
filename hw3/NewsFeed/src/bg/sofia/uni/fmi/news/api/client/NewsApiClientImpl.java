package bg.sofia.uni.fmi.news.api.client;

import bg.sofia.uni.fmi.news.api.config.NewsApiKeyConfig;
import bg.sofia.uni.fmi.news.api.exception.NewsApiException;
import bg.sofia.uni.fmi.news.api.query.NewsQueryBuilder;
import bg.sofia.uni.fmi.news.api.response.NewsApiResponse;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class NewsApiClientImpl implements NewsApiClient {
    private static final int SERVER_SIDE_ERROR = 500;
    private static final int HTTP_OK_RESPONSE = 200;
    private static final String BASE_URL = "https://newsapi.org/v2/top-headlines";
    private final HttpClient httpClient;
    private final NewsApiKeyConfig config;
    private final Gson gson;

    public NewsApiClientImpl(NewsApiKeyConfig config, HttpClient httpClient) {
        this.config = config;
        this.httpClient = httpClient;
        this.gson = new Gson();
    }

    public NewsApiClientImpl(NewsApiKeyConfig config) {
        this(config, HttpClient.newHttpClient());
    }

    @Override
    public NewsApiResponse getTopHeadlines(NewsQueryBuilder queryBuilder) throws NewsApiException {
        String url = BASE_URL + queryBuilder.build() + "&apiKey=" + config.apiKey();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        HttpResponse<String> response;
        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new NewsApiException(SERVER_SIDE_ERROR, "Request failed", e);
        }

        if (response.statusCode() != HTTP_OK_RESPONSE) {
            throw new NewsApiException(response.statusCode(), "Error response from News API: " + response.body());
        }

        return gson.fromJson(response.body(), NewsApiResponse.class);
    }
}
