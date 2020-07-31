package com.petstore.autotests.steps;

import static com.petstore.autotests.Helper.createOrder;
import static com.petstore.autotests.Helper.createPetWithSpecifiedStatus;
import static io.restassured.RestAssured.given;
import static net.serenitybdd.core.Serenity.sessionVariableCalled;
import static net.serenitybdd.core.Serenity.setSessionVariable;
import static org.hamcrest.Matchers.equalTo;

import com.petstore.autotests.model.Order;
import com.petstore.autotests.model.OrderStatus;
import com.petstore.autotests.model.Pet;
import com.petstore.autotests.model.PetStatus;
import com.petstore.autotests.conf.Configuration;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.TimeZone;
import net.serenitybdd.screenplay.actors.OnStage;
import net.serenitybdd.screenplay.actors.OnlineCast;

public class OrderStepDefinition {

  Response response;
  private final String INVALID_ORDER_ID = "abc";
  private static ArrayList<Integer> petsTestData = new ArrayList<>();
  private static ArrayList<Integer> ordersTestData = new ArrayList<>();

  // Session variables:
  private final String SESSION_PET_ID = "petId";
  private final String SESSION_ORDER_ID = "orderId";
  private final String SESSION_QUANTITY = "quantity";
  private final String SESSION_DATE = "date";
  private final String SESSION_STATUS = "status";
  private final String SESSION_COMPLETE = "complete";

  @Before("@start_order")
  public void setTheStage() {
    OnStage.setTheStage(new OnlineCast());
  }

  @After("@store")
  public void tearDown() {
    for (int petId : petsTestData) {
      given(Configuration.defaultPetSpec()).delete("{petId}", petId);
    }
    for (int orderId : ordersTestData) {
      given(Configuration.defaultOrderSpec()).delete("order/{orderId}", orderId);
    }
  }

  @Given("a Pet added to a Store")
  public void findPetToBuy() {
    Pet pet = createPetWithSpecifiedStatus(PetStatus.available);
    given(Configuration.defaultPetSpec())
        .body(pet).when()
        .post();
    int petId = pet.getId();
    petsTestData.add(petId);
    setSessionVariable(SESSION_PET_ID).to(petId);
  }

  @When("I place an Order for this Pet")
  public void addOrder() {
    final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
    sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

    int petId = sessionVariableCalled(SESSION_PET_ID);
    int quantity = 1;
    OrderStatus status = OrderStatus.placed;
    boolean complete = true;
    Order order = createOrder(petId, quantity, status, complete);
    int orderId = order.getId();
    ordersTestData.add(orderId);
    setSessionVariable(SESSION_ORDER_ID).to(orderId);
    setSessionVariable(SESSION_QUANTITY).to(quantity);
    setSessionVariable(SESSION_DATE).to(sdf.format(order.getShipDate()));
    setSessionVariable(SESSION_STATUS).to(status);
    setSessionVariable(SESSION_COMPLETE).to(complete);

    response = given(Configuration.defaultOrderSpec())
        .body(order).when()
        .post("order");
  }

  @Then("the Order is created")
  public void orderIsCreated() {
    int petId = sessionVariableCalled(SESSION_PET_ID);
    int orderId = sessionVariableCalled(SESSION_ORDER_ID);
    int quantity = sessionVariableCalled(SESSION_QUANTITY);
    String date = sessionVariableCalled(SESSION_DATE);
    String status = sessionVariableCalled(SESSION_STATUS).toString();
    boolean complete = sessionVariableCalled(SESSION_COMPLETE);
    response.then().statusCode(200)
        .and().body("id", equalTo(orderId))
        .and().body("petId", equalTo(petId))
        .and().body("quantity", equalTo(quantity))
        .and().body("shipDate", equalTo(date))
        .and().body("status", equalTo(status))
        .and().body("complete", equalTo(complete));
  }

  @And("I can see the Order information")
  public void getCreatedOrder() {
    int orderId = sessionVariableCalled(SESSION_ORDER_ID);
    int petId = sessionVariableCalled(SESSION_PET_ID);
    int quantity = sessionVariableCalled(SESSION_QUANTITY);
    String date = sessionVariableCalled(SESSION_DATE);
    String status = sessionVariableCalled(SESSION_STATUS).toString();
    boolean complete = sessionVariableCalled(SESSION_COMPLETE);
    given(Configuration.defaultOrderSpec()).when().
        get("order/{orderId}", orderId).
        then().
        statusCode(200)
        .and().body("id", equalTo(orderId))
        .and().body("petId", equalTo(petId))
        .and().body("quantity", equalTo(quantity))
        .and().body("shipDate", equalTo(date))
        .and().body("status", equalTo(status))
        .and().body("complete", equalTo(complete));
  }

  @And("an Order is placed for this Pet")
  public void addNewOrder() {
    int petId = sessionVariableCalled(SESSION_PET_ID);
    int quantity = 1;
    OrderStatus status = OrderStatus.placed;
    Order order = createOrder(petId, quantity, status, true);
    int orderId = order.getId();
    ordersTestData.add(orderId);
    setSessionVariable(SESSION_ORDER_ID).to(orderId);

    response = given(Configuration.defaultOrderSpec())
        .body(order).when()
        .post("order");
  }

  @When("I delete the purchase Order")
  public void deleteOrder() {
    int orderId = sessionVariableCalled(SESSION_ORDER_ID);
    response = given(Configuration.defaultOrderSpec()).when()
        .delete("order/{orderId}", orderId);
  }

  @Then("the Order is deleted")
  public void orderIsDeleted() {
    int orderId = sessionVariableCalled(SESSION_ORDER_ID);
    response.then().statusCode(200)
        .and().body("message", equalTo(String.valueOf(orderId)));
  }

  @And("this Order is not available anymore")
  public void checkDeletedOrder() {
    int orderId = sessionVariableCalled(SESSION_ORDER_ID);
    given(Configuration.defaultOrderSpec()).when().
        get("{orderId}", orderId).
        then().
        statusCode(404);
  }

  @When("I try to find a purchase Order with incorrect id")
  public void getCreatedOrderWithIncorrectId() {
    response = given(Configuration.defaultOrderSpec()).when().
        get("order/{orderId}", INVALID_ORDER_ID);
  }

  @Then("I get a message that invalid ID was supplied for Order")
  public void orderNotFound() {
    response.then().statusCode(404);
  }


}