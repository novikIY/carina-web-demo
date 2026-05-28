package com.zebrunner.carina.demo.gui.pages.desktop;

import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.gui.AbstractPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

public class HomePage extends AbstractPage {

    @FindBy(xpath = "//a[contains(@class, 'b-main-navigation__link') "
            + "and normalize-space(.)='Каталог']")
    private ExtendedWebElement catalogLink;

    public HomePage(WebDriver driver) {
        super(driver);
    }

    public CatalogPage openCatalog() {
        catalogLink.click();
        return new CatalogPage(getDriver());
    }
}