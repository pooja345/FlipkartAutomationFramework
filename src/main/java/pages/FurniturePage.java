package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class FurniturePage {
    WebDriver driver;

    public FurniturePage(WebDriver driver) {
        this.driver = driver;
    }

    public void applyCategory(String category) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        String categoriesHeaderXpath = "//span[normalize-space()='CATEGORIES']";
        String categoryLinkXpath = "//a[@title='" + category + "']";
        String showButtonXpath = "//span[normalize-space(text())='Show']";

        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(categoriesHeaderXpath)));
            System.out.println("CATEGORIES section found.");
        } catch (TimeoutException e) {
            System.out.println("CATEGORIES section not found within timeout.");
            return;
        }

        try {
            if (!driver.findElements(By.xpath(categoryLinkXpath)).isEmpty()) {
                WebElement categoryLink = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(categoryLinkXpath)));
                categoryLink.click();
                wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//h1[@class='BUOuZu']")));
                System.out.println(category + " clicked directly.");
            } else {
                if (!driver.findElements(By.xpath(showButtonXpath)).isEmpty()) {
                    WebElement showButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(showButtonXpath)));
                    showButton.click();
                    System.out.println("'Show' clicked.");

                    if (!driver.findElements(By.xpath(categoryLinkXpath)).isEmpty()) {
                        WebElement categoryLink = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(categoryLinkXpath)));
                        categoryLink.click();
                        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//h1[@class='BUOuZu']")));
                        System.out.println(category + " clicked after expanding.");
                    } else {
                        System.out.println(category + " not available even after expanding.");
                    }
                } else {
                    System.out.println("'Show' button not found. Cannot expand categories.");
                }
            }
        } catch (Exception e) {
            System.out.println("Error while applying category: " + e.getMessage());
        }
    }


    public void applyPrice(String maxPrice) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        JavascriptExecutor js = (JavascriptExecutor) driver;

        // Scroll into view before clicking
        Consumer<WebElement> scrollAndClick = (WebElement element) -> {
            js.executeScript("arguments[0].scrollIntoView({block: 'center', inline: 'nearest'});", element);
            element.click();
        };

        // Helper method to safely click dropdown and select option
        BiConsumer<By, By> safeClickDropdown = (dropdownLocator, optionLocator) -> {
            int attempts = 0;
            boolean clicked = false;

            while (attempts < 3 && !clicked) {
                try {
                    WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(dropdownLocator));
                    scrollAndClick.accept(dropdown);

                    WebElement option = wait.until(ExpectedConditions.elementToBeClickable(optionLocator));
                    scrollAndClick.accept(option);

                    wait.until(webDriver -> js.executeScript("return document.readyState").equals("complete"));
                    clicked = true;
                } catch (ElementClickInterceptedException e) {
                    System.out.println("Click intercepted, retrying... attempt " + (attempts + 1));
                    try { Thread.sleep(500); } catch (InterruptedException ignored) {}
                } catch (TimeoutException e) {
                    throw new RuntimeException("Dropdown or option not found: " + dropdownLocator, e);
                }
                attempts++;
            }

            if (!clicked) {
                throw new RuntimeException("Failed to click dropdown after 3 attempts: " + dropdownLocator);
            }
        };

        // Verify Price label is visible
        WebElement priceLabel = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//span[normalize-space()='Price']")));
        System.out.println("Verified Price label is present: " + priceLabel.isDisplayed());

        // Select min price
        safeClickDropdown.accept(
                By.xpath("//div[@class='suthUA']//select"),
                By.xpath("//div[@class='suthUA']//select/option[@value='500']")
        );

        // Select max price
        safeClickDropdown.accept(
                By.xpath("//div[@class='tKgS7w']//select"),
                By.xpath("//div[@class='tKgS7w']//select/option[@value='" + maxPrice + "']")
        );

        System.out.println("✅ Price filter applied successfully — Max price: " + maxPrice);
    }

}