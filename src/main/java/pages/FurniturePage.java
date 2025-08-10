package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

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

        // Verify Price label
        try {
            WebElement priceLabel = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//span[normalize-space()='Price']")));
            System.out.println("Verified Price label is present: " + priceLabel.isDisplayed());
        } catch (TimeoutException e) {
            System.out.println("Price label not found — skipping price filter.");
            return;
        }

        // Select min price if dropdown exists
        try {
            WebElement minDropdown = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//div[@class='suthUA']//select[@fdprocessedid='d8w5dk']")));
            minDropdown.click();
            WebElement minOption = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//div[@class='suthUA']//select[@fdprocessedid='d8w5dk']/option[@value='" + maxPrice + "']")));
            minOption.click();
            wait.until(webDriver -> js.executeScript("return document.readyState").equals("complete"));
        } catch (TimeoutException e) {
            System.out.println("Min price dropdown not found — skipping min price selection.");
        }

        // Select max price if dropdown exists
        try {
            WebElement maxDropdown = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//div[@class='tKgS7w']//select[@fdprocessedid='10spxh']")));
            maxDropdown.click();
            WebElement maxOption = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//div[@class='tKgS7w']//select[@fdprocessedid='10spxh']/option[@value='" + maxPrice + "']")));
            maxOption.click();
            wait.until(webDriver -> js.executeScript("return document.readyState").equals("complete"));
        } catch (TimeoutException e) {
            System.out.println("Max price dropdown not found — skipping max price selection.");
        }
    }
}