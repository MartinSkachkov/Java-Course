package bg.sofia.uni.fmi.news.api.response;

import java.util.List;

public record NewsApiResponse(
        String status,
        String code,
        String message,
        int totalResults,
        List<Article> articles
) {
}
