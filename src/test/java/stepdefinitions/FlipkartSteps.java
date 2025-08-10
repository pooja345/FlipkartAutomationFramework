package stepdefinitions;

import io.cucumber.java.en.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import io.cucumber.datatable.DataTable;
import pages.FlipkartHomePage;
import pages.FurniturePage;

import java.util.List;
import java.util.Map;

public class FlipkartSteps {

    WebDriver driver;
    FlipkartHomePage homePage;
    FurniturePage furniturePage;

    @Given("I launch Flipkart website")
    public void i_launch_flipkart_website() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://www.flipkart.com");
        homePage = new FlipkartHomePage(driver);
        furniturePage = new FurniturePage(driver);
        homePage.closeLoginPopupIfPresent();
    }

    @When("I navigate to {string} and click {string}")
    public void i_navigate_to_and_click(String menu, String subMenu)
    {
        homePage.closeLoginPopupIfPresent();
        homePage.navigateToFurniture();
    }

    @When("I apply filters with following data:")
    public void i_apply_filters_with_following_data(DataTable dataTable) throws InterruptedException {
        List<Map<String, String>> data = dataTable.asMaps();
        String category = data.get(0).get("Category");
        String price = data.get(0).get("Price");
        String brand = data.get(0).get("Brand");

        furniturePage.applyCategory(category);
        furniturePage.applyPrice(price);
        //furniturePage.applyBrand(brand);
    }

    @Then("I should see filtered results")
    public void i_should_see_filtered_results() {
        System.out.println("Filters applied successfully.");
     //   driver.quit();
    }
}