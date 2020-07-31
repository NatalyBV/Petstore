package com.petstore.autotests.conf;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public final class Configuration {
  private static String baseUri = "https://petstore.swagger.io";

  private Configuration() {
  }

  private static final String USERNAME = System.getenv("PET_STORE_USERNAME");
  private static final String PASSWORD = System.getenv("PET_STORE_PASSWORD");

  public static String getUSERNAME() {
    return USERNAME;
  }

  public static String getPASSWORD() {
    return PASSWORD;
  }

  public static RequestSpecification defaultPetSpec() {
    return new RequestSpecBuilder()
        .setBaseUri(baseUri)
        .setBasePath("v2/pet")
        .setContentType(ContentType.JSON).build();
  }

  public static RequestSpecification defaultOrderSpec() {
    return new RequestSpecBuilder()
        .setBaseUri(baseUri)
        .setBasePath("v2/store")
        .setContentType(ContentType.JSON).build();
  }

  public static RequestSpecification defaultUserSpec() {
    return new RequestSpecBuilder()
        .setBaseUri(baseUri)
        .setBasePath("v2/user")
        .setContentType(ContentType.JSON).build();
  }

}
