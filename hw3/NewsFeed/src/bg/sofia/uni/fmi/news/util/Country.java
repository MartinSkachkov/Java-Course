package bg.sofia.uni.fmi.news.util;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public record Country(String code) {
    private static final Map<String, Country> COUNTRY_MAP = new HashMap<>();

    static {
        initializeMap();
    }

    private static void initializeMap() {
        if (COUNTRY_MAP.isEmpty()) {
            String[] isoCountries = Locale.getISOCountries();

            for (String countryCode : isoCountries) {
                COUNTRY_MAP.put(countryCode.toLowerCase(), new Country(countryCode.toLowerCase()));
            }
        }
    }

    public static Country fromCode(String code) {
        if (code == null || code.isBlank()) {
            throw new IllegalArgumentException("Country code cannot be null or empty");
        }

        Country country = COUNTRY_MAP.get(code.toLowerCase());
        if (country == null) {
            throw new IllegalArgumentException("Invalid country code: " + code);
        }

        return country;
    }
}