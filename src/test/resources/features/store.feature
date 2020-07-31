Feature: Store
  @start_order
  @store
  Scenario: Place an Order for a Pet
    Given a Pet added to a Store
    When I place an Order for this Pet
    Then the Order is created
    And I can see the Order information

  @start_order
  @store
  Scenario: Delete an order for a Pet
    Given a Pet added to a Store
    And an Order is placed for this Pet
    When I delete the purchase Order
    Then the Order is deleted
    And this Order is not available anymore

  @start_order
  Scenario: Find an order with incorrect id
    When I try to find a purchase Order with incorrect id
    Then I get a message that invalid ID was supplied for Order