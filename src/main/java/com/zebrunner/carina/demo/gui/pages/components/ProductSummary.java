package com.zebrunner.carina.demo.gui.pages.components;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProductSummary {

    private final String title;
    private final String price;
    private final String rating;

    public ProductSummary(String title, String price, String rating) {
        this.title = normalize(title);
        this.price = normalizePrice(price);
        this.rating = normalize(rating);
    }

    public String getTitle() {
        return title;
    }

    public String getPrice() {
        return price;
    }

    public String getRating() {
        return rating;
    }

    private static String normalize(String value) {
        if (value == null) {
            return "";
        } else {
            return value.trim().replaceAll("\\s+", " ");
        }
    }

    private static String normalizePrice(String value) {
        if (value == null) {
            return "";
        }
        String cleaned = value.replace("\u00A0", " ");
        Matcher m = Pattern
                .compile("\\d+(?:\\s\\d+)*[.,]\\d{2}")
                .matcher(cleaned);
        if (m.find()) {
            return m.group().replaceAll("\\s+", " ").trim();
        }
        return cleaned
                .replaceAll("(?i)^\\s*от\\s+", "")
                .trim()
                .replaceAll("\\s+", " ");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (this.getClass() != o.getClass()) return false;
        ProductSummary that = (ProductSummary) o;
        if (that.hashCode() != this.hashCode()) return false;
        if (!Objects.equals(this.title, that.title)) return false;
        if (!Objects.equals(this.price, that.price)) return false;
        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, price);
    }

    @Override
    public String toString() {
        return "ProductSummary{title='" + title + "', price='" + price + "', rating='" + rating + "'}";
    }
}
