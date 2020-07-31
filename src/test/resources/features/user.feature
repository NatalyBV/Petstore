Feature: User
  @start_user
  Scenario: User login
    When I log in as a certain User
    Then I logged in

  @start_user
  Scenario: User logout
    Given I logged in as a certain User
    When I log out as this User
    Then I logged out

  @start_user
  @user
  Scenario: Add a new User
    Given I logged in as a certain User
    When I add a new User
    Then the User is created
    And I can see the User information

  @start_user
  @user
  Scenario: Delete a User
    Given I logged in as a certain User
    And I added a User
    When I delete this User
    Then the User is deleted
    And this User is not available anymore

  @start_user
  @user
  Scenario: Update a User
    Given I logged in as a certain User
    And I added a User
    When I update this User
    Then the operation of User updating is successful
    And the User information is updated correspondingly

  @start_user
  @user
  Scenario: Add a list of Users
    When I add a list of new Users
    Then these Users are created
    And these Users are available

  @start_user
  Scenario: Find a User with non-existing username
    When I search for a User with non-existing username
    Then I get a message that User with such username is absent

  @start_user
  Scenario: Delete a User with non-existing username
    When I try to delete a User with non-existing username
    Then I get an information that such User was not found