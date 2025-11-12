#Author: rvm.seng@gmail.com
#This feature file covers automated BDD scenarios for verifying the product sorting functionality on the Inventory page.

@regression @inventory @sorting
Feature: Product sorting on Inventory page
  As a user of the store
  I want products to be sorted correctly when I choose a sort option
  So that I can find products by name or price in the chosen order

  Background: 
    Given the user is authenticated
    And the user is on the Inventory page

  # Scenario outline for the four main sorts
  @positive @critical
  Scenario Outline: Products are sorted correctly when selecting a sort option
    When the user selects the sort option "<SortOptionLabel>"
    Then the product list should be ordered by "<OrderType>"

    Examples: 
      | SortOptionLabel     | OrderType        |
      | Name (A to Z)       | name-ascending   |
      | Name (Z to A)       | name-descending  |
      | Price (low to high) | price-ascending  |
      | Price (high to low) | price-descending |

  # Persistence: sort selection reset after page reload
  @persistence @reset_to_default
  Scenario: Sort option resets to the default after page reload
    When the user selects the sort option "Price (high to low)"
    And the user reloads the Inventory page
    Then the sort option should reset to the default value "Name (A to Z)"
