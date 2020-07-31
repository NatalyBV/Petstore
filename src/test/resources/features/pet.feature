Feature: Pet
  @start_pet
  @pet
  Scenario: Add a new Pet to the Store
    When I add a new Pet to the Store
    Then the Pet is created
    And I can see the Pet information

  @start_pet
  @pet
  Scenario: Delete a Pet
    Given I add a new Pet
    When I delete this Pet
    Then the Pet is deleted
    And this Pet is not available anymore

  @start_pet
  @pet
  Scenario: Update a Pet
    Given I add a new Pet
    When I update this Pet
    Then the operation of Pet updating is successful
    And the Pet is updated with corresponding information

  @start_pet
  @pet
  Scenario: Update a Pet with form data
    Given I add a new Pet
    When I update this Pet with new name and status
    Then the operation of Pet updating using form data is successful
    And the Pet is updated with corresponding name and status

  @start_pet
  @pet
  Scenario: Upload an image for a Pet
    Given I add a new Pet
    When I upload an image for a Pet
    Then I see the information about uploaded file, it's size and additional metadata information

  @start_pet
  @pet
  Scenario: Search for a Pet by status
    Given I add Pets with corresponding status
    When I search for Pets by the same status
    Then I get the list of Pets with corresponding status

  @start_pet
  Scenario: Delete a Pet with non-existing id
    When I try to delete a Pet with non-existing id
    Then I get an information that such Pet was not found

  @start_pet
  Scenario: Delete a Pet with invalid id
    When I try to delete a Pet with invalid id
    Then I get a message that invalid ID was supplied

  @start_pet
  Scenario: Find a Pet with non-existing id
    When I search for a Pet with non-existing id
    Then I get a message that Pet with such id is absent

  @start_pet
  Scenario: Find a Pet with invalid id
    When I search for a Pet with invalid id
    Then I get a message that invalid ID was supplied

  @skip_scenario
  @start_pet
  Scenario: Add a new Pet without name
    When I add a new Pet without name
    Then I get a message that Pet name cannot be empty

  @start_pet
  Scenario: Update a Pet with non-existing id using form data
    When I try to update the Pet with non-existing id using name and status
    Then I get a message that Pet with such id not found
