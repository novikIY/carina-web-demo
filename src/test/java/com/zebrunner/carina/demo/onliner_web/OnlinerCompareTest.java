package com.zebrunner.carina.demo.onliner_web;

import java.util.List;
import com.zebrunner.carina.demo.gui.pages.components.ProductSummary;
import com.zebrunner.carina.demo.gui.pages.desktop.CatalogPage;
import com.zebrunner.carina.demo.gui.pages.desktop.ComparePage;
import com.zebrunner.carina.demo.gui.pages.desktop.HomePage;
import com.zebrunner.carina.demo.gui.pages.desktop.MobileCatalogPage;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.zebrunner.carina.core.IAbstractTest;
import com.zebrunner.carina.core.registrar.ownership.MethodOwner;

public class OnlinerCompareTest implements IAbstractTest {

    private static final int PHONES_TO_COMPARE = 2;

    @Test
    @MethodOwner(owner = "novikIY")
    public void testCompareTwoApplePhones() {

        HomePage homePage = new HomePage(getDriver());
        homePage.open();
        Assert.assertTrue(homePage.isPageOpened(),
                "Onliner home page was not opened");

        CatalogPage catalogPage = homePage.openCatalog();
        Assert.assertTrue(catalogPage.isPageOpened(),
                "Onliner catalog page was not opened");

        MobileCatalogPage mobilePage = catalogPage.openMobileCategory();
        Assert.assertTrue(mobilePage.isPageOpened(),
                "Mobile catalog page was not opened");

        mobilePage.filterByApple();
        Assert.assertFalse(mobilePage.getProductCards().isEmpty(),
                "No products are visible after applying the Apple filter");

        List<ProductSummary> selectedOnListing =
                mobilePage.addFirstNToCompare(PHONES_TO_COMPARE);
        Assert.assertEquals(selectedOnListing.size(), PHONES_TO_COMPARE,
                "Expected to mark " + PHONES_TO_COMPARE
                        + " phones for comparison, but only marked "
                        + selectedOnListing.size());

        ComparePage comparePage = mobilePage.openComparePage();
        Assert.assertTrue(comparePage.isPageOpened(),
                "Compare page did not open");

        List<ProductSummary> shownOnCompare = comparePage.readComparedProducts();
        Assert.assertEquals(shownOnCompare.size(), PHONES_TO_COMPARE,
                "Compare page shows " + shownOnCompare.size()
                        + " products, expected " + PHONES_TO_COMPARE);

        for (ProductSummary expected : selectedOnListing) {
            boolean foundMatch = false;
            for (ProductSummary actual : shownOnCompare) {
                if (titlesMatch(expected.getTitle(), actual.getTitle())
                        && pricesMatch(expected.getPrice(), actual.getPrice())) {
                    foundMatch = true;
                    break;
                }
            }
            Assert.assertTrue(foundMatch,
                    "Product from listing was not found on the compare page "
                            + "with matching title and price. Listing: " + expected
                            + ", compare page: " + shownOnCompare);
        }
    }

    private boolean titlesMatch(String a, String b) {
        if (a == null || b == null) {
            return false;
        }
        String aLower = a.toLowerCase();
        String bLower = b.toLowerCase();
        return aLower.contains(bLower) || bLower.contains(aLower);
    }

    private boolean pricesMatch(String a, String b) {
        return a != null && a.equals(b);
    }
}
