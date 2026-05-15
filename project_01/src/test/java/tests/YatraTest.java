

package tests;

import Base.BaseTest;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import pages.HomePage;
import pages.OffersPage;
import screenshotspages.HolidaysAndScreenshotUtil;

public class YatraTest extends BaseTest {
    HomePage home;
    OffersPage offers;
    String parent;

    @Test(
            priority = 1
    )
    public void openOffersPage() {
        this.home = new HomePage(this.driver);
        this.offers = new OffersPage(this.driver);
        this.home.closePopup();
        this.parent = this.driver.getWindowHandle();
        this.home.clickOffers();
        this.offers.switchToOffersWindow(this.parent);
    }

    @Test(
            priority = 2,
            dependsOnMethods = {"openOffersPage"}
    )
    public void validateOffersTitle() {
        SoftAssert soft = new SoftAssert();
        soft.assertEquals(this.offers.getTitle(), "Domestic Flights Offers | Deals on Domestic Flight Booking | Yatra.com", "Title mismatch");
        soft.assertAll();
    }

    @Test(
            priority = 3,
            dependsOnMethods = {"openOffersPage"}
    )
    public void validateBannerText() {
        SoftAssert soft = new SoftAssert();
        soft.assertEquals(this.offers.getBanner(), "Great Offers & Amazing Deals", "Banner mismatch");
        soft.assertAll();
    }

    @Test(
            priority = 4,
            dependsOnMethods = {"openOffersPage"}
    )
    public void validateExcelAndHolidays() throws Exception {
        YatraExcelValidation.validateOffersPage(this.driver);
        HolidaysAndScreenshotUtil.captureAndListHolidays(this.driver, this.parent);
    }
}
