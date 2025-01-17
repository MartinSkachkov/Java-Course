package bg.sofia.uni.fmi.news.api.config;

public record NewsApiKeyConfig(String apiKey) {
    public NewsApiKeyConfig {
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalArgumentException("API key cannot be null or empty");
        }
    }
}
