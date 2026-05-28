package com.zebrunner.carina.demo.onliner_web;

import java.util.List;
import com.zebrunner.carina.demo.gui.pages.components.ProductCard;
import com.zebrunner.carina.demo.gui.pages.components.ProductSummary;
import com.zebrunner.carina.demo.gui.pages.desktop.CatalogPage;
import com.zebrunner.carina.demo.gui.pages.desktop.HomePage;
import com.zebrunner.carina.demo.gui.pages.desktop.MobileCatalogPage;
import com.zebrunner.carina.demo.gui.pages.desktop.ProductDetailsPage;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.zebrunner.carina.core.IAbstractTest;
import com.zebrunner.carina.core.registrar.ownership.MethodOwner;

public class OnlinerCatalogTest implements IAbstractTest {

    private static final String TARGET_MODEL = "iPhone 17";

    @Test
    @MethodOwner(owner = "novikIY")
    public void testIPhone17CatalogFlow() {

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

        List<ProductCard> cards = mobilePage.getProductCards();
        Assert.assertFalse(cards.isEmpty(),
                "Mobile catalog rendered no product cards");

        for (ProductCard card : cards) {
            Assert.assertTrue(card.matchesExpectedTemplate(),
                    "Card '" + card.getTitle() + "' does not follow the "
                            + "expected template [title/photo/price/rating]");
        }

        mobilePage.filterByApple();
        List<ProductCard> appleCards = mobilePage.getProductCards();
        Assert.assertFalse(appleCards.isEmpty(),
                "No products are visible after applying the Apple brand filter");
        for (ProductCard card : appleCards) {
            Assert.assertTrue(
                    card.getTitle().toLowerCase().contains("apple")
                            || card.getTitle().toLowerCase().contains("iphone"),
                    "Listing contains a non-Apple product after Apple filter "
                            + "was applied: " + card.getTitle());
        }

        ProductSummary listingSummary = mobilePage.readSummaryFromListing(TARGET_MODEL);
        Assert.assertNotNull(listingSummary,
                "Expected to find '" + TARGET_MODEL + "' on the listing, "
                        + "but no matching card was found");

        ProductDetailsPage detailsPage = mobilePage.openProductByTitle(TARGET_MODEL);
        Assert.assertTrue(detailsPage.isPageOpened(),
                "Product details page for '" + TARGET_MODEL + "' was not opened");

        ProductSummary detailsSummary = detailsPage.readActualSummary();

        Assert.assertTrue(
                detailsSummary.getTitle().toLowerCase()
                        .contains(TARGET_MODEL.toLowerCase()),
                "Details page title '" + detailsSummary.getTitle()
                        + "' does not contain expected model '" + TARGET_MODEL + "'");

        Assert.assertEquals(detailsSummary.getPrice(), listingSummary.getPrice(),
                "Price on the details page does not match the listing. "
                        + "Listing: '" + listingSummary.getPrice()
                        + "', details: '" + detailsSummary.getPrice() + "'");

        if (!listingSummary.getRating().isEmpty()
                && !detailsSummary.getRating().isEmpty()) {
            String listingRating = extractFirstNumber(listingSummary.getRating());
            String detailsRating = extractFirstNumber(detailsSummary.getRating());
            Assert.assertEquals(detailsRating, listingRating,
                    "Rating on the details page does not match the listing. "
                            + "Listing: '" + listingSummary.getRating()
                            + "', details: '" + detailsSummary.getRating() + "'");
        }
    }

    private String extractFirstNumber(String raw) {
        if (raw == null) {
            return "";
        }
        java.util.regex.Matcher m = java.util.regex.Pattern
                .compile("\\d+[.,]\\d")
                .matcher(raw);
        return m.find() ? m.group().replace(',', '.') : "";
    }
}

