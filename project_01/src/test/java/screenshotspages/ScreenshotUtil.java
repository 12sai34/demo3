package screenshotspages;

import Base.BaseTest;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.*;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import pages.HomePage;
import pages.OffersPage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class ScreenshotUtil {

    public static String takeScreenshot(WebDriver driver, String testName) {
        try {
            File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

            String reportDir =
                    System.getProperty("user.dir") + "/reports/";
            String screenshotDir = reportDir + "screenshots/";

            new File(screenshotDir).mkdirs();

            File dest = new File(screenshotDir + testName + ".png");

            Files.copy(
                    src.toPath(),
                    dest.toPath(),
                    StandardCopyOption.REPLACE_EXISTING
            );

            return "screenshots/" + testName + ".png";

        } catch (Exception e) {
            System.out.println("Screenshot FAILED: " + e.getMessage());
            return null;
        }
    }

    public static class YatraTest extends BaseTest {

        HomePage home;
        OffersPage offers;
        String parent;

        @Test(priority = 1)
        public void openOffersPage() {

            home = new HomePage(driver);
            offers = new OffersPage(driver);

            home.closePopup();
            parent = driver.getWindowHandle();
            home.clickOffers();
            offers.switchToOffersWindow(parent);
        }

        @Test(priority = 2, dependsOnMethods = "openOffersPage")
        public void validateOffersTitle() {

            SoftAssert soft = new SoftAssert();
            soft.assertEquals(
                    offers.getTitle(),
                    "Domestic Flights Offers | Deals on Domestic Flight Booking | Yatra.com",
                    "Title mismatch"
            );
            soft.assertAll();
        }

        @Test(priority = 3, dependsOnMethods = "openOffersPage")
        public void validateBannerText() {

            SoftAssert soft = new SoftAssert();
            soft.assertEquals(
                    offers.getBanner(),
                    "Great Offers & Amazing Deals",
                    "Banner mismatch"
            );
            soft.assertAll();
        }

        @Test(priority = 4, dependsOnMethods = "openOffersPage")
        public void validateExcelAndHolidays() throws Exception {

            YatraExcelValidation.validateOffersPage(driver);
            HolidaysAndScreenshotUtil.captureAndListHolidays(driver, parent);
        }
    }

    public static class YatraExcelValidation {

        public static void validateOffersPage(WebDriver driver) throws Exception {

            String excelPath = System.getProperty("user.dir")
                    + "\\resources\\YatraTestData.xlsx";

            FileInputStream fis = new FileInputStream(excelPath);
            Workbook workbook = new XSSFWorkbook(fis);
            Sheet sheet = workbook.getSheetAt(0);

            CellStyle passStyle = workbook.createCellStyle();
            passStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
            passStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            CellStyle failStyle = workbook.createCellStyle();
            failStyle.setFillForegroundColor(IndexedColors.ROSE.getIndex());
            failStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            Row titleRow = sheet.getRow(1);
            String expectedTitle = titleRow.getCell(1).getStringCellValue();
            String actualTitle = driver.getTitle();

            titleRow.createCell(2).setCellValue(actualTitle);
            Cell titleStatus = titleRow.createCell(3);

            if (actualTitle.equals(expectedTitle)) {
                titleStatus.setCellValue("PASS");
                titleStatus.setCellStyle(passStyle);
            } else {
                titleStatus.setCellValue("FAIL");
                titleStatus.setCellStyle(failStyle);
            }

            Row bannerRow = sheet.getRow(2);
            String expectedBanner = bannerRow.getCell(1).getStringCellValue();
            String actualBanner =
                    driver.findElement(By.cssSelector("h2.wfull.bxs")).getText();

            bannerRow.createCell(2).setCellValue(actualBanner);
            Cell bannerStatus = bannerRow.createCell(3);

            if (actualBanner.equals(expectedBanner)) {
                bannerStatus.setCellValue("PASS");
                bannerStatus.setCellStyle(passStyle);
            } else {
                bannerStatus.setCellValue("FAIL");
                bannerStatus.setCellStyle(failStyle);
            }

            fis.close();
            FileOutputStream fos = new FileOutputStream(excelPath);
            workbook.write(fos);
            fos.close();
            workbook.close();
        }

        public static void cool()
        {
            System.out.println("hello");
        }
    }
}
