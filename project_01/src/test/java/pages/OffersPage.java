package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.Set;

public class OffersPage {

    WebDriver driver;

    By bannerText = By.cssSelector("h2.wfull.bxs");

    public OffersPage(WebDriver driver) {
        this.driver = driver;
    }

    public void switchToOffersWindow(String parentWindow) {
        Set<String> windows = driver.getWindowHandles();
        for (String win : windows) {
            if (!win.equals(parentWindow)) {
                driver.switchTo().window(win);
                break;
            }
        }
    }

    public String getTitle() {
        return driver.getTitle();
    }

    public String getBanner() {
        return driver.findElement(bannerText).getText();
    }
}