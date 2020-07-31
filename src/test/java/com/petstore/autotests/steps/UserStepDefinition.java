package com.petstore.autotests.steps;

import static com.petstore.autotests.Helper.createUser;
import static com.petstore.autotests.Helper.createUsers;
import static io.restassured.RestAssured.given;
import static net.serenitybdd.core.Serenity.sessionVariableCalled;
import static net.serenitybdd.core.Serenity.setSessionVariable;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

import com.petstore.autotests.model.User;
import com.petstore.autotests.conf.Configuration;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import java.util.ArrayList;
import java.util.List;
import net.serenitybdd.screenplay.actors.OnStage;
import net.serenitybdd.screenplay.actors.OnlineCast;

public class UserStepDefinition {

  Response response;
  private static String USERNAME = Configuration.getUSERNAME();
  private static String PASSWORD = Configuration.getPASSWORD();
  private static String NON_EXISTING_USERNAME = "nonExistingUsername";
  private static ArrayList<String> testData = new ArrayList<>();

  // Session variables:
  private final String SESSION_USER_ID = "id";
  private final String SESSION_USERNAME = "username";
  private final String SESSION_PASSWORD = "password";
  private final String SESSION_FIRST_NAME = "firstName";
  private final String SESSION_LAST_NAME = "lastName";
  private final String SESSION_EMAIL = "email";
  private final String SESSION_PHONE = "phone";
  private final String SESSION_STATUS = "status";

  @Before("@start_user")
  public void setTheStage() {
    OnStage.setTheStage(new OnlineCast());
  }

  @After("@user")
  public void tearDown() {
    for (String username : testData) {
      given(Configuration.defaultUserSpec())
          .delete("{username}", username);
    }
  }

  @Given("I logged in as a certain User")
  public void loggedInUser() {
    response = given(Configuration.defaultUserSpec())
        .queryParam("username", USERNAME)
        .queryParam("password", PASSWORD).when()
        .get("login");
  }

  @When("I log in as a certain User")
  public void logInUser() {
    response = given(Configuration.defaultUserSpec())
        .queryParam("username", USERNAME)
        .queryParam("password", PASSWORD).when()
        .get("login");
  }

  @Then("I logged in")
  public void userIsLoggedIn() {
    response.then().statusCode(200).and()
        .body("message", containsString("logged in user session"));
  }

  @When("I log out as this User")
  public void logOutUser() {
    response = given(Configuration.defaultUserSpec())
        .queryParam("username", USERNAME)
        .queryParam("password", PASSWORD).when()
        .get("logout");
  }

  @Then("I logged out")
  public void userIsLoggedOut() {
    response.then().statusCode(200).and()
        .body("message", equalTo("ok"));
  }

  @When("I add a new User")
  public void addNewUser() {
    String username = "testUsername";
    String firstName = "Sam";
    String lastName = "Smith";
    String email = "testEmail123@gmail.com";
    String password = "p@ss123";
    String phone = "+38094765982";
    int status = 0;
    User user = createUser(username, firstName, lastName, email, password, phone, status);
    int userId = user.getId();
    setSessionVariable(SESSION_USER_ID).to(userId);
    testData.add(username);
    setSessionVariable(SESSION_USERNAME).to(username);
    setSessionVariable(SESSION_FIRST_NAME).to(firstName);
    setSessionVariable(SESSION_LAST_NAME).to(lastName);
    setSessionVariable(SESSION_EMAIL).to(email);
    setSessionVariable(SESSION_PASSWORD).to(password);
    setSessionVariable(SESSION_PHONE).to(phone);
    setSessionVariable(SESSION_STATUS).to(status);

    response = given(Configuration.defaultUserSpec())
        .body(user).when()
        .post();
  }

  @And("I added a User")
  public void addSomeUser() {
    String username = "userTest1";
    String firstName = "Ann";
    String lastName = "Jones";
    String email = "myTestEmail@gmail.com";
    String password = "checkP@ss1";
    String phone = "+380560978935";
    int status = 1;

    User user = createUser(username, firstName, lastName, email, password, phone, status);
    testData.add(username);
    setSessionVariable(SESSION_USERNAME).to(username);
    int userId = user.getId();
    setSessionVariable(SESSION_USER_ID).to(userId);

    response = given(Configuration.defaultUserSpec())
        .body(user).when()
        .post();
  }

  @Then("the User is created")
  public void userIsCreated() {
    int userId = sessionVariableCalled(SESSION_USER_ID);
    response.then().statusCode(200)
        .and().body("message", equalTo(String.valueOf(userId)));
  }

  @And("I can see the User information")
  public void getCreatedUser() {
    int userId = sessionVariableCalled(SESSION_USER_ID);
    String username = sessionVariableCalled(SESSION_USERNAME);
    String firstName = sessionVariableCalled(SESSION_FIRST_NAME);
    String lastName = sessionVariableCalled(SESSION_LAST_NAME);
    String email = sessionVariableCalled(SESSION_EMAIL);
    String password = sessionVariableCalled(SESSION_PASSWORD);
    String phone = sessionVariableCalled(SESSION_PHONE);
    int status = sessionVariableCalled(SESSION_STATUS);

    given(Configuration.defaultUserSpec()).when().
        get("{username}", username).
        then().
        statusCode(200)
        .and().body("id", equalTo(userId))
        .and().body("username", equalTo(username))
        .and().body("firstName", equalTo(firstName))
        .and().body("lastName", equalTo(lastName))
        .and().body("email", equalTo(email))
        .and().body("password", equalTo(password))
        .and().body("phone", equalTo(phone))
        .and().body("userStatus", equalTo(status));
  }

  @When("I delete this User")
  public void deleteUser() {
    String username = sessionVariableCalled(SESSION_USERNAME);
    response = given(Configuration.defaultUserSpec()).when()
        .delete("{username}", username);
  }

  @Then("the User is deleted")
  public void userIsDeleted() {
    String username = sessionVariableCalled(SESSION_USERNAME);
    response.then().statusCode(200)
        .and().body("message", equalTo(username));
  }

  @And("this User is not available anymore")
  public void checkDeletedUser() {
    String username = sessionVariableCalled(SESSION_USERNAME);
    given(Configuration.defaultUserSpec()).when().
        get("{username}", username).
        then().
        statusCode(404).and()
        .body("message", equalTo("User not found"));
  }

  @When("I update this User")
  public void updateUser() {
    int id = sessionVariableCalled(SESSION_USER_ID);
    String username = sessionVariableCalled(SESSION_USERNAME);
    String firstName = "Anna";
    String lastName = "Brown";
    String email = "testUpdated1@gmail.com";
    String password = "updatedP@ss1";
    String phone = "+380747093424";
    int status = 0;

    User updatedUser
        = new User(id, username, firstName, lastName, email, password, phone, status);

    setSessionVariable(SESSION_FIRST_NAME).to(firstName);
    setSessionVariable(SESSION_LAST_NAME).to(lastName);
    setSessionVariable(SESSION_EMAIL).to(email);
    setSessionVariable(SESSION_PASSWORD).to(password);
    setSessionVariable(SESSION_PHONE).to(phone);
    setSessionVariable(SESSION_STATUS).to(status);

    response = given(Configuration.defaultUserSpec())
        .body(updatedUser).when()
        .put("{username}", username);
  }

  @Then("the operation of User updating is successful")
  public void userIsUpdated() {
    int userId = sessionVariableCalled(SESSION_USER_ID);
    response.then().statusCode(200)
        .and().body("message", equalTo(String.valueOf(userId)));
  }

  @And("the User information is updated correspondingly")
  public void getUpdatedUser() {
    int userId = sessionVariableCalled(SESSION_USER_ID);
    String username = sessionVariableCalled(SESSION_USERNAME);
    String firstName = sessionVariableCalled(SESSION_FIRST_NAME);
    String lastName = sessionVariableCalled(SESSION_LAST_NAME);
    String email = sessionVariableCalled(SESSION_EMAIL);
    String password = sessionVariableCalled(SESSION_PASSWORD);
    String phone = sessionVariableCalled(SESSION_PHONE);
    int status = sessionVariableCalled(SESSION_STATUS);

    given(Configuration.defaultUserSpec()).when().
        get("{username}", username).
        then().
        statusCode(200)
        .and().body("id", equalTo(userId))
        .and().body("username", equalTo(username))
        .and().body("firstName", equalTo(firstName))
        .and().body("lastName", equalTo(lastName))
        .and().body("email", equalTo(email))
        .and().body("password", equalTo(password))
        .and().body("phone", equalTo(phone))
        .and().body("userStatus", equalTo(status));
  }

  @When("I add a list of new Users")
  public void addNewUsers() {
    List<User> users = createUsers();
    for (User user : users) {
      testData.add(user.getUsername());
    }

    response = given(Configuration.defaultUserSpec())
        .body(users)
        .when()
        .post("createWithList");
  }

  @Then("these Users are created")
  public void usersAreCreated() {
    response.then().statusCode(200)
        .and().body("message", equalTo("ok"));
  }

  @And("these Users are available")
  public void getCreatedUsers() {
    for (int i = 0; i < 5; i++) {
      given(Configuration.defaultUserSpec()).when().
          get("{username}", i + "username" + i).
          then().
          statusCode(200)
          .and().body("username", equalTo(i + "username" + i))
          .and().body("firstName", equalTo("firstName" + i))
          .and().body("lastName", equalTo("lastName" + i))
          .and().body("email", equalTo("testEmailCheck" + i + "gmail.com"))
          .and().body("password", equalTo("p@ssword" + i))
          .and().body("phone", equalTo("38091115550" + i))
          .and().body("userStatus", equalTo(i));
    }
  }

  @When("I search for a User with non-existing username")
  public void searchForUserWithNonExistingUsername() {
    response = given(Configuration.defaultUserSpec()).when().
        get("{username}", NON_EXISTING_USERNAME);
  }

  @Then("I get a message that User with such username is absent")
  public void nonExistingUsernameSupplied() {
    response.then().statusCode(404).and()
        .body("message", equalTo("User not found"));
  }

  @When("I try to delete a User with non-existing username")
  public void deleteUserWithNonExistingUsername() {
    response = given(Configuration.defaultUserSpec()).when()
        .delete("{username}", NON_EXISTING_USERNAME);
  }

  @Then("I get an information that such User was not found")
  public void userNotFound() {
    response.then().statusCode(404);
  }
}
