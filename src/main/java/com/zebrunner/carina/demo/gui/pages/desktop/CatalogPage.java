package com.zebrunner.carina.demo.gui.pages.desktop;

import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.gui.AbstractPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

public class CatalogPage extends AbstractPage {

    @FindBy(xpath = "//a[contains(@href,'mobile')]")
    private ExtendedWebElement mobileCategoryLink;

    public CatalogPage(WebDriver driver) {
        super(driver);
        setPageAbsoluteURL("https://catalog.onliner.by/");
    }

    public MobileCatalogPage openMobileCategory() {
        mobileCategoryLink.click();
        return new MobileCatalogPage(getDriver());
    }
}

