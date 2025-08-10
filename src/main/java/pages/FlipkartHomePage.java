package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class FlipkartHomePage {
    WebDriver driver;

    public FlipkartHomePage(WebDriver driver) {
        this.driver = driver;
        // Apply 30 seconds implicit wait globally for this driver instance
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));
    }

    public void closeLoginPopupIfPresent() {
        try {
           // WebElement closeBtn = driver.findElement(By.xpath("//button[contains(text(),'✕')]"));
            WebElement closeBtn = driver.findElement(By.xpath("//a[@class='_1jKL3b' and contains(text(),'Login')]"));
            closeBtn.click();
        } catch (Exception e) {
            // Popup not present
        }
    }

    public void navigateToFurniture() {
        Actions actions = new Actions(driver);
        // Locate the "Home & Furniture" menu item and hover over it
        WebElement menu = driver.findElement(By.xpath("//div[@class='bpjkJb']/span[@class='TSD49J' and contains(normalize-space(text()), 'Home & Furniture')]"));
        actions.moveToElement(menu).perform();
        // Wait for the "Furnishing" link to be clickable and then click it
        menu.click();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement furnitureLink = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@title='Furnishing']")));
        furnitureLink.click();

    }
}
