package com.zebrunner.carina.demo.gui.pages.desktop;

import java.util.ArrayList;
import java.util.List;
import com.zebrunner.carina.demo.gui.pages.components.ProductCard;
import com.zebrunner.carina.demo.gui.pages.components.ProductSummary;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.gui.AbstractPage;

public class MobileCatalogPage extends AbstractPage {

    @FindBy(xpath = "//a[contains(@href, '/prices')]"
            + "/ancestor::*[descendant::h3][1]")
    private List<ProductCard> productCards;

    @FindBy(xpath = "(//*[normalize-space(text())='Производитель']"
            + "/following::*[normalize-space(.)='Apple' "
            + "and not(self::a) and not(ancestor::a)])[1]")
    private ExtendedWebElement appleBrandCheckbox;

    @FindBy(xpath = "//div[contains(@class, 'auth-popup')]")
    private ExtendedWebElement authPopup;

    @FindBy(xpath = "//div[contains(@class, 'auth-popup')]"
            + "//*[contains(@class, 'close')]")
    private ExtendedWebElement authPopupClose;

    @FindBy(xpath = "(//a[contains(@href, '/compare')])[1]")
    private ExtendedWebElement compareButton;

    public MobileCatalogPage(WebDriver driver) {
        super(driver);
        setPageAbsoluteURL("https://catalog.onliner.by/mobile");
    }

    public List<ProductCard> getProductCards() {
        return productCards;
    }

    public void filterByApple() {
        dismissAuthPopupIfPresent();
        appleBrandCheckbox.scrollTo();
        appleBrandCheckbox.click();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();
        }
    }

    private void dismissAuthPopupIfPresent() {
        if (authPopup.isElementPresent(2)) {
            authPopupClose.click();
            authPopup.waitUntilElementDisappear(5);
        }
    }

    public ProductDetailsPage openProductByTitle(String titleSubstring) {
        ProductSummary expected = readSummaryFromListing(titleSubstring);
        if (expected == null) {
            throw new RuntimeException(
                    "Product matching '" + titleSubstring + "' was not found on the listing");
        }
        for (ProductCard card : getProductCards()) {
            if (card.getTitle().toLowerCase().contains(titleSubstring.toLowerCase())) {
                card.openProduct();
                ProductDetailsPage details = new ProductDetailsPage(getDriver());
                details.setExpectedSummary(expected);
                return details;
            }
        }
        throw new RuntimeException(
                "Product matching '" + titleSubstring + "' disappeared between scan and click");
    }

    public ProductSummary readSummaryFromListing(String titleSubstring) {
        for (ProductCard card : getProductCards()) {
            if (card.getTitle().toLowerCase().contains(titleSubstring.toLowerCase())) {
                return new ProductSummary(
                        card.getTitle(),
                        card.getPriceText(),
                        card.getRatingText());
            }
        }
        return null;
    }

    public List<ProductSummary> addFirstNToCompare(int count) {
        List<ProductSummary> added = new ArrayList<>();
        List<ProductCard> cards = getProductCards();

        int toAdd = Math.min(count, cards.size());
        for (int i = 0; i < toAdd; i++) {
            ProductCard card = cards.get(i);
            added.add(new ProductSummary(
                    card.getTitle(),
                    card.getPriceText(),
                    card.getRatingText()));
            card.addToCompare();
        }
        return added;
    }

    public ComparePage openComparePage() {
        try {
            Thread.sleep(1500);
        } catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();
        }
        compareButton.scrollTo();
        dismissAuthPopupIfPresent();

        ((JavascriptExecutor) getDriver())
                .executeScript("arguments[0].click();", compareButton.getElement());
        return new ComparePage(getDriver());
    }
}





