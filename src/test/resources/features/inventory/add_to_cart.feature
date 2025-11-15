#Author: rvm.seng@gmail.com
#This feature file covers automated BDD scenarios for verifying the "Add to Cart" functionality on the Inventory page.
@regression @inventory @cart
Feature: Add to Cart functionality on Inventory page
  As a store user
  I want to add and remove products to/from the cart
  So that the cart badge and Cart page reflect correct items and counts

  Background: 
    Given the user is logged in and on the Inventory page
    And the shopping basket be empty

  @positive @smoke @debug
  Scenario: Add a single product to the cart
    Given the inventory contains a product with name "Sauce Labs Backpack"
    When the user clicks "Add to cart" for the product "Sauce Labs Backpack"
    Then the cart badge should show "1"
    And the "Add to cart" button for that product should change to "Remove"
    And the Cart page should contain an item with name "Sauce Labs Backpack" and the correct price

  @positive
  Scenario: Add multiple distinct products to the cart
    Given the inventory contains products "Sauce Labs Backpack", "Sauce Labs Bike Light", "Sauce Labs Bolt T-Shirt"
    When the user adds "Sauce Labs Backpack", "Sauce Labs Bike Light", and "Sauce Labs Bolt T-Shirt" to the cart
    Then the cart badge should show "3"
    And the Cart page should list exactly these three items with their respective prices

  @positive
  Scenario: Remove a product from the cart (toggle behavior)
    Given "Sauce Labs Backpack" is already added to the cart
    When the user clicks "Remove" for "Sauce Labs Backpack" on the Inventory page
    Then the cart badge should update to "0"
    And the button for that product should show "Add to cart"
    And the Cart page should not contain "Sauce Labs Backpack"

  @edgecase @ui
  Scenario: Adding the same product twice has no duplicate entries (idempotent add)
    Given the inventory contains a product with name "Sauce Labs Backpack"
    When the user clicks "Add to cart" for "Sauce Labs Backpack"
    And the user clicks "Add to cart" for "Sauce Labs Backpack" again
    Then the cart badge should show "1"
    And the Cart page should contain only one entry for "Sauce Labs Backpack"

  @edgecase @order
  Scenario: Cart preserves item order added (FIFO) when navigating to Cart page
    Given the inventory contains products "Sauce Labs Backpack", "Sauce Labs Bike Light", "Sauce Labs Bolt T-Shirt"
    When the user adds "Sauce Labs Bike Light" then "Sauce Labs Backpack" then "Sauce Labs Bolt T-Shirt" to the cart
    And the user navigates to the Cart page
    Then the Cart page should list items in the order: "Sauce Labs Bike Light", "Sauce Labs Backpack", "Sauce Labs Bolt T-Shirt"

  @robustness @network
  Scenario: Add to cart handles temporary network failure gracefully
    Given network requests to the server may intermittently fail with a 5xx error
    When the user clicks "Add to cart" for "Sauce Labs Backpack" while a transient network error occurs
    Then the application should show a friendly error message or retry option
    And the cart badge should not increment incorrectly
    And eventual success on retry should result in the cart badge incrementing by 1

  @persistence
  Scenario: Items remain in cart after page reload (session persistence)
    Given the user has added "Sauce Labs Backpack" and "Sauce Labs Bike Light" to the cart
    When the user reloads the Inventory page
    Then the cart badge should still show "2"
    And buttons for those products should display "Remove"
    And the Cart page should still contain both items

  @negative
  Scenario: Cart UI does not break when inventory is dynamically updated
    Given the inventory is updated on the server (items removed/renamed) while user is on Inventory page
    When the user attempts to add an item that was removed on server side
    Then the application must not crash
    And the user should see an appropriate error or the item removed from UI
    And the cart badge must remain consistent

  @ui @accessibility
  Scenario: "Add to cart" button is accessible and descriptive
    Given the Inventory page is loaded
    Then each "Add to cart" control should have an accessible label (aria-label) describing its target product
    And screen reader users should be able to identify which product will be added

  @integration
  Scenario: Cart page displays subtotal equal to sum of item prices
    Given the user has added "Sauce Labs Backpack" priced $29.99 and "Sauce Labs Bike Light" priced $9.99 to the cart
    When the user navigates to the Cart page
    Then the Cart page subtotal should equal $39.98
    And each listed item's price should match the price shown on the Inventory page

  @security
  Scenario: Prevent tampering with client-side cart badge (UI trust boundary)
    Given the cart badge value is maintained client-side
    When a malicious script attempts to set the cart badge to an arbitrary large value
    Then the backend must validate cart contents and the Cart page must reflect server-validated item counts (no fake counts)

  @cleanup
  Scenario: Empty cart returns to initial state
    Given the user has added several items to the cart
    When the user clicks "Remove" for all items on the Cart page
    Then the cart badge should show "0"
    And the Inventory page buttons should show "Add to cart" for all items
    And the Cart page should show an empty-cart message
