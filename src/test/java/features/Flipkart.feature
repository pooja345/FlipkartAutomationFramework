Feature: Filter furniture on Flipkart

  Scenario: Filter Aadya Craft beds under 5000
    Given I launch Flipkart website
    When I navigate to "Home & Furniture" and click "Furniture"
    And I apply filters with following data:
      | Category                 | Price | Brand        |
      | Bed Linen & Blankets     | 4000  | Aadya Craft  |
    Then I should see filtered results