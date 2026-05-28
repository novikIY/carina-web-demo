package com.zebrunner.carina.demo.gui.pages.desktop;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.zebrunner.carina.demo.gui.pages.components.ProductSummary;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.gui.AbstractPage;

public class ComparePage extends AbstractPage {

    @FindBy(xpath = "//h1[contains(., 'Сравнение')]")
    private ExtendedWebElement pageHeader;

    public ComparePage(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isPageOpened() {
        return pageHeader.isElementPresent(15);
    }

    public List<ProductSummary> readComparedProducts() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ignored) {
            Thread.currentThread().interrupt();
        }

        List<WebElement> titleLinks = getDriver().findElements(By.xpath(
                "//a[contains(@href, '/mobile/') "
                        + "and not(contains(@href, '/prices')) "
                        + "and not(contains(@href, '/reviews'))]"));

        List<WebElement> priceLinks = getDriver().findElements(By.xpath(
                "//a[contains(@href, '/prices')]"));

        Set<String> seenUrls = new LinkedHashSet<>();
        List<ProductSummary> result = new ArrayList<>();

        for (WebElement titleEl : titleLinks) {
            String href;
            String title;
            try {
                href = titleEl.getAttribute("href");
                title = titleEl.getText().trim();
            } catch (Exception ignored) {
                continue;
            }
            if (href == null || href.isEmpty() || !seenUrls.add(href)) {
                continue;
            }
            if (title.isEmpty()) {
                seenUrls.remove(href);
                continue;
            }
            String price = findPriceForProduct(priceLinks, href);
            result.add(new ProductSummary(title, price, ""));
        }
        return result;
    }

    private String findPriceForProduct(List<WebElement> priceLinks, String productHref) {
        String expectedPriceHref = productHref + "/prices";
        for (WebElement priceEl : priceLinks) {
            try {
                if (expectedPriceHref.equals(priceEl.getAttribute("href"))) {
                    return priceEl.getText().trim();
                }
            } catch (Exception ignored) {
            }
        }
        return "";
    }
}
