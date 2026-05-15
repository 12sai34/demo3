package screenshotspages;

import org.openqa.selenium.*;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Set;

public class HolidaysAndScreenshotUtil {

    public static void captureAndListHolidays(WebDriver driver, String parentWindow) throws Exception {


        TakesScreenshot ts = (TakesScreenshot) driver;
        File source = ts.getScreenshotAs(OutputType.FILE);

        File destination = new File(
                System.getProperty("user.dir") + "/screenshots/offers_page.png"
        );
        destination.getParentFile().mkdirs();

        Files.copy(
                source.toPath(),
                destination.toPath(),
                StandardCopyOption.REPLACE_EXISTING
        );

        System.out.println("Offers page screenshot captured");

        driver.findElement(By.cssSelector("a[href*='holiday-deals']")).click();
        Thread.sleep(3000);

        List<WebElement> packages =
                driver.findElements(By.cssSelector("span.view-btn.flR.anim"));

        String offersPage = driver.getWindowHandle();
        System.out.println("Total Packages Found: " + packages.size());

     //loop for holidays
        for (int i = 0; i < packages.size(); i++) {

            packages = driver.findElements(By.cssSelector("span.view-btn.flR.anim"));
            packages.get(i).click();
            Thread.sleep(3000);

            Set<String> allTabs = driver.getWindowHandles();

            for (String tab : allTabs) {

                if (!tab.equals(parentWindow) && !tab.equals(offersPage)) {

                    driver.switchTo().window(tab);

                    /* =====================
                       SKIP LOGIC (NO SCREENSHOT HERE)
                    ====================== */
                    if (!driver.findElements(By.id("submit")).isEmpty()) {



                        driver.close();
                        break;
                    }

                 //check valid holiday page
                    List<WebElement> titleElements =
                            driver.findElements(By.cssSelector("h2.ng-binding"));
                    List<WebElement> priceElements =
                            driver.findElements(By.cssSelector("span.price.ng-binding"));

                    if (!titleElements.isEmpty() && !priceElements.isEmpty()) {

                        String title = titleElements.get(0).getText();
                        String price = priceElements.get(0).getText();

                        System.out.println("Package: " + title);
                        System.out.println("Price  : " + price);
                        System.out.println("--------------------------------");


                       //ss for valid page
                        TakesScreenshot holidayShot = (TakesScreenshot) driver;
                        File holidaySrc = holidayShot.getScreenshotAs(OutputType.FILE);

                        String safeTitle = title.replaceAll("[^a-zA-Z0-9]", "_");
                        File holidayDest = new File(
                                System.getProperty("user.dir")
                                        + "/screenshots/holidays/" + safeTitle + ".png"
                        );

                        holidayDest.getParentFile().mkdirs();

                        Files.copy(
                                holidaySrc.toPath(),
                                holidayDest.toPath(),
                                StandardCopyOption.REPLACE_EXISTING
                        );

                        System.out.println("Screenshot saved for: " + title);

                    }
//                    System.out.println("--------------------------------");

                    driver.close();
                    break;
                }
            }

            driver.switchTo().window(offersPage);
        }
    }
}
