package bg.sofia.uni.fmi.news.api.config;

import bg.sofia.uni.fmi.news.util.ApiKeyLoader;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mockStatic;

public class NewsApiKeyConfigTest {

    @Test
    void testValidApiKey() {
        try (MockedStatic<ApiKeyLoader> mockedApiKeyLoader = mockStatic(ApiKeyLoader.class)) {
            mockedApiKeyLoader.when(ApiKeyLoader::loadApiKey).thenReturn("123");

            NewsApiKeyConfig config = new NewsApiKeyConfig(ApiKeyLoader.loadApiKey());
            assertEquals("123", config.apiKey(), "API key should be '123' after loading");
        }
    }

    @Test
    void testNullApiKey() {
        assertThrows(IllegalArgumentException.class, () ->
                        new NewsApiKeyConfig(null),
                "Creating a NewsApiKeyConfig with a null API key should throw an IllegalArgumentException");
    }

    @Test
    void testEmptyApiKey() {
        assertThrows(IllegalArgumentException.class, () ->
                        new NewsApiKeyConfig(""),
                "Creating a NewsApiKeyConfig with an empty API key should throw an IllegalArgumentException");
    }

    @Test
    void testBlankApiKey() {
        assertThrows(IllegalArgumentException.class, () ->
                        new NewsApiKeyConfig("   "),
                "Creating a NewsApiKeyConfig with a blank API key should throw an IllegalArgumentException");
    }

}
