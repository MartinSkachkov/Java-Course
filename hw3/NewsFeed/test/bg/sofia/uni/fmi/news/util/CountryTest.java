package bg.sofia.uni.fmi.news.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CountryTest {

    @Test
    void testValidLowercaseCountryCode() {
        Country us = Country.fromCode("us");
        assertEquals("us", us.code(), "The country code should be 'us' when provided with 'us'.");
    }

    @Test
    void testValidUppercaseCountryCode() {
        Country gb = Country.fromCode("GB");
        assertEquals("gb", gb.code(), "The country code should be 'gb' when provided with 'GB'.");
    }

    @Test
    void testInvalidCountryCodeXX() {
        assertThrows(IllegalArgumentException.class, () ->
                Country.fromCode("xx"), "Country code 'xx' should be invalid.");
    }

    @Test
    void testInvalidCountryCodeNumeric() {
        assertThrows(IllegalArgumentException.class, () ->
                Country.fromCode("123"), "Numeric country codes should not be valid.");
    }

    @Test
    void testInvalidCountryCodeLongString() {
        assertThrows(IllegalArgumentException.class, () ->
                Country.fromCode("invalid"), "Country code 'invalid' should not be valid.");
    }

    @Test
    void testNullCountryCode() {
        assertThrows(IllegalArgumentException.class, () ->
                Country.fromCode(null), "Country code cannot be null.");
    }

    @Test
    void testEmptyCountryCode() {
        assertThrows(IllegalArgumentException.class, () ->
                Country.fromCode(""), "Country code cannot be empty.");
    }

    @Test
    void testBlankCountryCode() {
        assertThrows(IllegalArgumentException.class, () ->
                Country.fromCode("  "), "Country code cannot be blank.");
    }

}