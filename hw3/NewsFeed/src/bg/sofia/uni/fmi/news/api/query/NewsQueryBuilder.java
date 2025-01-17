package bg.sofia.uni.fmi.news.api.query;

import bg.sofia.uni.fmi.news.util.Category;
import bg.sofia.uni.fmi.news.util.Country;

public class NewsQueryBuilder {
    private static final int MAX_PAGE_SIZE = 100;
    private String keywords;
    private Category category;
    private Country country;
    private String sources;
    private Integer pageSize;
    private Integer page;

    public NewsQueryBuilder withKeywords(String keywords) {
        validateKeywords(keywords);
        this.keywords = keywords;
        return this;
    }

    public NewsQueryBuilder withCategory(Category category) {
        validateCategory(category);

        if (sources != null) {
            throw new IllegalStateException("Cannot use 'category' parameter together with 'sources'");
        }

        this.category = category;
        return this;
    }

    public NewsQueryBuilder withCountry(Country country) {
        validateCountry(country);

        if (sources != null) {
            throw new IllegalStateException("Cannot use 'country' parameter together with 'sources'");
        }

        this.country = country;
        return this;
    }

    public NewsQueryBuilder withSources(String... sourceIds) {
        validateSources(sourceIds);

        if ((category != null) || (country != null)) {
            throw new IllegalStateException("Cannot use 'sources' parameter together with 'country' or 'category'");
        }

        this.sources = String.join(",", sourceIds);
        return this;
    }

    public NewsQueryBuilder withPageSize(int pageSize) {
        validatePageSize(pageSize);
        this.pageSize = pageSize;
        return this;
    }

    public NewsQueryBuilder withPage(int page) {
        validatePage(page);
        this.page = page;
        return this;
    }

    public String build() {
        validateParams();

        StringBuilder query = new StringBuilder();
        buildQuery(query);

        return query.toString();
    }

    private void buildQuery(StringBuilder query) {
        if (keywords != null && !keywords.isEmpty()) {
            query.append("?q=").append(keywords);
        } else {
            query.append("?");
        }

        if (sources != null) {
            appendParam(query, "sources", sources);
        }

        if (category != null) {
            appendParam(query, "category", category.name().toLowerCase());
        }

        if (country != null) {
            appendParam(query, "country", country.code());
        }

        if (pageSize != null) {
            appendParam(query, "pageSize", pageSize.toString());
        }

        if (page != null) {
            appendParam(query, "page", page.toString());
        }
    }

    private void validateParams() {
        if (sources != null && (category != null || country != null)) {
            throw new IllegalStateException("Cannot use 'sources' parameter together with 'country' or 'category'");
        }
    }

    private void appendParam(StringBuilder query, String name, String value) {
        if (query.toString().endsWith("?")) {
            query.append(name).append("=").append(value);
        } else {
            query.append("&").append(name).append("=").append(value);
        }
    }

    private void validateKeywords(String keywords) {
        if (keywords == null || keywords.isBlank()) {
            throw new IllegalArgumentException("Keywords cannot be null or blank");
        }
    }

    private void validateCategory(Category category) {
        if (category == null) {
            throw new IllegalArgumentException("Category cannot be null");
        }
    }

    private void validateCountry(Country country) {
        if (country == null) {
            throw new IllegalArgumentException("Country cannot be null");
        }
    }

    private void validateSources(String... sourceIds) {
        if (sourceIds == null || sourceIds.length == 0) {
            throw new IllegalArgumentException("At least one source must be provided");
        }
    }

    private void validatePageSize(int pageSize) {
        if (pageSize <= 0 || pageSize > MAX_PAGE_SIZE) {
            throw new IllegalArgumentException("pageSize must be between 1 and 100");
        }
    }

    private void validatePage(int page) {
        if (page <= 0) {
            throw new IllegalArgumentException("page must be greater than 0");
        }
    }
}
