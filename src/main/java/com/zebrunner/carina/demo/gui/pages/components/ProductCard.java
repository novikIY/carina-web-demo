package com.zebrunner.carina.demo.gui.pages.components;

import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.gui.AbstractUIObject;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

public class ProductCard extends AbstractUIObject {

    @FindBy(xpath = ".//h3//a")
    private ExtendedWebElement titleLink;

    @FindBy(xpath = ".//img")
    private ExtendedWebElement photo;

    @FindBy(xpath = ".//a[contains(@href, '/prices')]")
    private ExtendedWebElement priceLink;

    @FindBy(xpath = ".//a[contains(@href, '/reviews')]")
    private ExtendedWebElement rating;

    @FindBy(xpath = ".//input[@type='checkbox']")
    private ExtendedWebElement compareToggle;

    public ProductCard(WebDriver driver, SearchContext searchContext) {
        super(driver, searchContext);
    }

    public String getTitle() {
        return titleLink.getText().trim();
    }

    public String getPriceText() {
        return hasPrice() ? priceLink.getText().trim() : "";
    }

    public String getRatingText() {
        return hasRatingBlock() ? rating.getText().trim() : "";
    }

    public boolean hasTitle() {
        return titleLink.isElementPresent() && !titleLink.getText().trim().isEmpty();
    }

    public boolean hasPhoto() {
        return photo.isElementPresent()
                && photo.getAttribute("src") != null
                && !photo.getAttribute("src").isEmpty();
    }

    public boolean hasPrice() {
        return priceLink.isElementPresent() && !priceLink.getText().trim().isEmpty();
    }

    public boolean hasRatingBlock() {
        return rating.isElementPresent();
    }

    public boolean matchesExpectedTemplate() {
        return hasTitle() && hasPhoto() && hasPrice() && hasRatingBlock();
    }

    public void openProduct() {
        titleLink.click();
    }

    public void addToCompare() {
        ((JavascriptExecutor) getDriver())
                .executeScript("arguments[0].click();", compareToggle.getElement());
    }
}