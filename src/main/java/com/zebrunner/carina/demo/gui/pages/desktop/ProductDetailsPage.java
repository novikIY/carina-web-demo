package com.zebrunner.carina.demo.gui.pages.desktop;

import com.zebrunner.carina.demo.gui.pages.components.ProductSummary;
import com.zebrunner.carina.webdriver.gui.AbstractPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;

public class ProductDetailsPage extends AbstractPage {

    @FindBy(xpath = "//h1")
    private ExtendedWebElement title;

    @FindBy(xpath = "(//a[contains(@href, '/prices')])[1]")
    private ExtendedWebElement price;

    @FindBy(xpath = "(//a[contains(@href, '/reviews') and contains(., ',')])[1]")
    private ExtendedWebElement rating;

    private ProductSummary expectedSummary;

    public ProductDetailsPage(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isPageOpened() {
        return title.isElementPresent(10);
    }

    public void setExpectedSummary(ProductSummary expectedSummary) {
        this.expectedSummary = expectedSummary;
    }

    public ProductSummary getExpectedSummary() {
        return expectedSummary;
    }

    public String getTitle() {
        return title.getText().trim();
    }

    public String getPriceText() {
        return price.isElementPresent() ? price.getText().trim() : "";
    }

    public String getRatingText() {
        return rating.isElementPresent() ? rating.getText().trim() : "";
    }

    public ProductSummary readActualSummary() {
        return new ProductSummary(getTitle(), getPriceText(), getRatingText());
    }
}




