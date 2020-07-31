package com.petstore.autotests.steps;

import static com.petstore.autotests.Helper.createListOfPetsWithSpecifiedStatus;
import static com.petstore.autotests.Helper.createPet;
import static com.petstore.autotests.Helper.createPetWithEmptyName;
import static com.petstore.autotests.Helper.getRandomPetStatus;
import static io.restassured.RestAssured.given;
import static net.serenitybdd.core.Serenity.sessionVariableCalled;
import static net.serenitybdd.core.Serenity.setSessionVariable;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.core.Every.everyItem;

import com.petstore.autotests.Helper;
import com.petstore.autotests.model.Pet;
import com.petstore.autotests.model.PetStatus;
import com.petstore.autotests.conf.Configuration;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.http.Header;
import io.restassured.response.Response;
import java.io.File;
import java.util.ArrayList;
import net.serenitybdd.core.Serenity;
import net.serenitybdd.screenplay.actors.OnStage;
import net.serenitybdd.screenplay.actors.OnlineCast;

public class PetStepDefinitions {

  Response response;
  private final String NON_EXISTING_PET_ID = "99999999";
  private final String INVALID_PET_ID = "abc";
  private static ArrayList<Integer> testData = new ArrayList<>();

  // Session variables:
  private final String SESSION_PET_ID = "id";
  private final String SESSION_NAME = "name";
  private final String SESSION_UPDATED_NAME = "updatedName";
  private final String SESSION_CATEGORY_NAME = "categoryName";
  private final String SESSION_TAG_NAME = "tagName";
  private final String SESSION_PHOTO = "sessionPhoto";
  private final String SESSION_STATUS = "status";

  @Before("@start_pet")
  public void setTheStage() {
    OnStage.setTheStage(new OnlineCast());
  }

  @After("@pet")
  public void tearDown() {
    for (int petId : testData) {
      given(Configuration.defaultPetSpec()).when()
          .delete("{petId}", petId);
    }
  }

  @Given("I add a new Pet")
  public void addNewPet() {
    Pet pet = createPet("Bob", "Category");
    int petId = pet.getId();
    testData.add(petId);
    setSessionVariable(SESSION_PET_ID).to(petId);
    String petName = pet.getName();
    setSessionVariable(SESSION_NAME).to(petName);

    response = given(Configuration.defaultPetSpec())
        .body(pet).when()
        .post();
  }

  @When("I add a new Pet to the Store")
  public void addNewPetToTheStore() {
    Pet pet = createPet("Bob", "Category");
    int petId = pet.getId();
    testData.add(petId);
    setSessionVariable(SESSION_PET_ID).to(petId);
    String petName = pet.getName();
    setSessionVariable(SESSION_NAME).to(petName);

    response = given(Configuration.defaultPetSpec())
        .body(pet).when()
        .post();
  }

  @Then("the Pet is created")
  public void petIsCreated() {
    int petId = sessionVariableCalled(SESSION_PET_ID);
    String petName = sessionVariableCalled(SESSION_NAME);
    response.then().statusCode(200)
        .and().body("id", equalTo(petId))
        .and().body("name", equalTo(petName));
  }

  @And("I can see the Pet information")
  public void getCreatedPet() {
    int petId = sessionVariableCalled(SESSION_PET_ID);
    String petName = sessionVariableCalled(SESSION_NAME);

    given(Configuration.defaultPetSpec()).when().
        get("{petId}", petId).
        then().
        statusCode(200).and().body("name", equalTo(petName));
  }

  @Given("I add Pets with corresponding status")
  public void addPets() {
    PetStatus status = getRandomPetStatus();
    setSessionVariable(SESSION_STATUS).to(status.toString());
    ArrayList<Pet> pets = createListOfPetsWithSpecifiedStatus(status);
    for (Pet pet : pets) {
      given(Configuration.defaultPetSpec())
          .body(pet).when()
          .post();
      testData.add(pet.getId());
    }
  }

  @When("I search for Pets by the same status")
  public void searchByStatus() {
    response = given(Configuration.defaultPetSpec())
        .queryParam("status", Serenity.<String>sessionVariableCalled(SESSION_STATUS)).when()
        .get("findByStatus");
  }

  @Then("I get the list of Pets with corresponding status")
  public void getPetListByStatus() {
    response.then().statusCode(200).and()
        .body("$", everyItem(hasEntry("status", sessionVariableCalled(SESSION_STATUS))));
  }

  @When("I delete this Pet")
  public void deletePet() {
    int petId = sessionVariableCalled(SESSION_PET_ID);
    response = given(Configuration.defaultPetSpec()).when()
        .delete("{petId}", petId);
  }

  @Then("the Pet is deleted")
  public void petIsDeleted() {
    int petId = sessionVariableCalled(SESSION_PET_ID);
    response.then().statusCode(200)
        .and().body("message", equalTo(String.valueOf(petId)));
  }

  @And("this Pet is not available anymore")
  public void checkDeletedPet() {
    int petId = sessionVariableCalled(SESSION_PET_ID);
    given(Configuration.defaultPetSpec()).when().
        get("{petId}", petId).
        then().
        statusCode(404).and()
        .body("message", equalTo("Pet not found"));
  }

  @When("I update this Pet")
  public void updatePet() {
    int petId = sessionVariableCalled(SESSION_PET_ID);
    String newName = "Bobby";
    String categoryName = "New category";
    String tagName = "New Tag";
    String photoUrl = "www.petPhotos.com/Bobby.jpg";
    Pet updatedPet = Helper.updatePet(petId, newName, categoryName, tagName, photoUrl);
    String status = updatedPet.getStatus().toString();

    setSessionVariable(SESSION_UPDATED_NAME).to(newName);
    setSessionVariable(SESSION_CATEGORY_NAME).to(categoryName);
    setSessionVariable(SESSION_TAG_NAME).to(tagName);
    setSessionVariable(SESSION_PHOTO).to(photoUrl);
    setSessionVariable(SESSION_STATUS).to(status);

    response = given(Configuration.defaultPetSpec())
        .body(updatedPet).when()
        .put();
  }

  @Then("the operation of Pet updating is successful")
  public void petIsUpdated() {
    int petId = sessionVariableCalled(SESSION_PET_ID);
    response.then().statusCode(200)
        .and().body("id", equalTo(petId));
  }

  @And("the Pet is updated with corresponding information")
  public void getUpdatedPet() {
    int petId = sessionVariableCalled(SESSION_PET_ID);
    String updatedName = sessionVariableCalled(SESSION_UPDATED_NAME);
    String updatedCategoryName = sessionVariableCalled(SESSION_CATEGORY_NAME);
    String updatedTagName = sessionVariableCalled(SESSION_TAG_NAME);
    String updatedPhotoURL = sessionVariableCalled(SESSION_PHOTO);
    String status = sessionVariableCalled(SESSION_STATUS);

    given(Configuration.defaultPetSpec()).when().
        get("{petId}", petId).
        then().
        statusCode(200)
        .and().body("name", equalTo(updatedName))
        .and().body("category.name", equalTo(updatedCategoryName))
        .and().body("photoUrls", hasItem(updatedPhotoURL))
        .and().body("tags.name", hasItem(updatedTagName))
        .and().body("status", equalTo(status)
    );
  }

  @When("I update this Pet with new name and status")
  public void updatePetUsingFormData() {
    int petId = sessionVariableCalled(SESSION_PET_ID);
    String newName = "King Bob";
    String status = getRandomPetStatus().toString();

    setSessionVariable(SESSION_UPDATED_NAME).to(newName);
    setSessionVariable(SESSION_STATUS).to(status);

    response = given(Configuration.defaultPetSpec())
        .formParam("name", newName)
        .formParam("status", status)
        .contentType("application/x-www-form-urlencoded")
        .when()
        .post("{petId}", petId);
  }

  @Then("the operation of Pet updating using form data is successful")
  public void petIsUpdatedWithFormData() {
    int petId = sessionVariableCalled(SESSION_PET_ID);
    response.then().statusCode(200)
        .and().body("message", equalTo(String.valueOf(petId)));
  }

  @And("the Pet is updated with corresponding name and status")
  public void getUpdatedWithNameAndStatusPet() {
    int petId = sessionVariableCalled(SESSION_PET_ID);
    String updatedName = sessionVariableCalled(SESSION_UPDATED_NAME);
    String status = sessionVariableCalled(SESSION_STATUS);

    given(Configuration.defaultPetSpec()).when().
        get("{petId}", petId).
        then().
        statusCode(200)
        .and().body("name", equalTo(updatedName))
        .and().body("status", equalTo(status)
    );
  }

  @When("I upload an image for a Pet")
  public void uploadImageForPet() {
    int petId = sessionVariableCalled(SESSION_PET_ID);
    File file = new File("./src/test/resources/Dog.jpg");
    response = given(Configuration.defaultPetSpec())
        .header(new Header("content-type", "multipart/form-data"))
        .multiPart("file", file, "image/jpeg")
        .formParam("additionalMetadata", "Test")
        .when()
        .post("{petId}/uploadImage", petId);
  }

  @Then("I see the information about uploaded file, it's size and additional metadata information")
  public void getUpdatedWithImagePet() {
    response.then().
        statusCode(200)
        .and().body("message", containsString("additionalMetadata: Test")).
        and().body("message", containsString("File uploaded to ./Dog.jpg")).
        and().body("message", containsString("bytes")
    );
  }

  @When("I search for a Pet with non-existing id")
  public void searchForPetWithNonExistingId() {
    response = given(Configuration.defaultPetSpec()).when()
        .get("{petId}", NON_EXISTING_PET_ID);
  }

  @When("I search for a Pet with invalid id")
  public void searchForPetWithInvalidId() {
    response = given(Configuration.defaultPetSpec()).when()
        .get("{petId}", INVALID_PET_ID);
  }

  @When("I try to delete a Pet with non-existing id")
  public void deletePetWithNonExistingId() {
    response = given(Configuration.defaultPetSpec()).when()
        .delete("{petId}", NON_EXISTING_PET_ID);
  }

  @When("I try to delete a Pet with invalid id")
  public void deletePetWithInvalidId() {
    response = given(Configuration.defaultPetSpec()).when()
        .delete("{petId}", INVALID_PET_ID);
  }

  @Then("I get an information that such Pet was not found")
  public void petNotFound() {
    response.then().statusCode(404);
  }

  @Then("I get a message that Pet with such id is absent")
  public void nonExistingIdSupplied() {
    response.then().statusCode(404).and()
        .body("message", equalTo("Pet not found"));
  }

  @Then("I get a message that invalid ID was supplied")
  public void invalidIdSupplied() {
    response.then().statusCode(404).and()
        .body("message", equalTo("java.lang.NumberFormatException: For input string: \"abc\""));
  }

  @When("I add a new Pet without name")
  public void addNewPetWithoutName() {
    Pet pet = createPetWithEmptyName();

    response = given(Configuration.defaultPetSpec())
        .body(pet).when()
        .post();
  }

  // Issue: a Pet can be created without required 'name' field
  @Then("I get a message that Pet name cannot be empty")
  public void nameCannotBeEmpty() {
    response.then().statusCode(400).and()
        .body("message", equalTo("Name cannot be empty"));
  }

  @When("I try to update the Pet with non-existing id using name and status")
  public void updatePetWithNonExistingIdUsingFormData() {
    String newName = "King Bobby";
    String status = getRandomPetStatus().toString();

    setSessionVariable(SESSION_UPDATED_NAME).to(newName);
    setSessionVariable(SESSION_STATUS).to(status);

    response = given(Configuration.defaultPetSpec())
        .formParam("name", newName)
        .formParam("status", status)
        .contentType("application/x-www-form-urlencoded")
        .when()
        .post("{petId}", NON_EXISTING_PET_ID);
  }

  @Then("I get a message that Pet with such id not found")
  public void nonExistingIdSuppliedForUpdating() {
    response.then().statusCode(404).and()
        .body("message", equalTo("not found"));
  }
}
