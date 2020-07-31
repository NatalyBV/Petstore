package com.petstore.autotests;

import com.petstore.autotests.model.Category;
import com.petstore.autotests.model.Order;
import com.petstore.autotests.model.OrderStatus;
import com.petstore.autotests.model.Pet;
import com.petstore.autotests.model.PetStatus;
import com.petstore.autotests.model.Tag;
import com.petstore.autotests.model.User;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;

public final class Helper {

  public Helper() {
  }

  public static int getRandomId() {
    Random random = new Random();
    StringBuilder id = new StringBuilder();
    for (int i = 0; i < 7; i++) {
      id.append(random.nextInt(9));
    }
    return Integer.parseInt(id.toString());
  }

  public static PetStatus getRandomPetStatus() {
    Random random = new Random();
    return PetStatus.values()[random.nextInt(PetStatus.values().length)];
  }

  public static int getRandomOrderNumber() {
    Random random = new Random();
    return random.nextInt(10) + 1;
  }

  public static Pet createPet(String name, String categoryName) {
    ArrayList<Tag> tags = new ArrayList<>(
        Arrays.asList(Tag.builder().id(getRandomId()).name("Tag1").build(),
            Tag.builder().id(getRandomId()).name("Tag2").build()));

    ArrayList<String> petPhotos = new ArrayList<>(
        Arrays.asList("www.petPhotos.com/1.jpg", "www.petPhotos.com/2.jpg",
            "www.petPhotos.com/3.png"));

    return Pet.builder()
        .id(getRandomId())
        .name(name)
        .category(Category.builder()
            .id(getRandomId())
            .name(categoryName)
            .build()
        ).photoUrls(petPhotos)
        .tags(tags)
        .status(getRandomPetStatus()).build();
  }

  public static Pet createPetWithEmptyName() {
    ArrayList<Tag> tags = new ArrayList<>(
        Arrays.asList(Tag.builder().id(getRandomId()).name("Tag1").build(),
            Tag.builder().id(getRandomId()).name("Tag2").build()));

    ArrayList<String> petPhotos = new ArrayList<>(
        Arrays.asList("www.petPhotos.com/1.jpg", "www.petPhotos.com/2.jpg",
            "www.petPhotos.com/3.png"));

    return Pet.builder()
        .id(getRandomId())
        .name("")
        .category(Category.builder()
            .id(getRandomId())
            .name("Category")
            .build()
        ).photoUrls(petPhotos)
        .tags(tags)
        .status(getRandomPetStatus()).build();
  }

  public static ArrayList<Pet> createListOfPetsWithSpecifiedStatus(PetStatus status) {
    ArrayList<Pet> pets = new ArrayList<>();

    for (int i = 0; i < 5; i++) {
      Pet pet = Pet.builder()
          .id(getRandomId())
          .name("Animal" + i)
          .category(Category.builder()
              .id(getRandomId())
              .name("Category" + i)
              .build()
          ).photoUrls(new ArrayList<>(
              Arrays.asList("www.petPhotos.com/" + i + ".jpg",
                  "www.petPhotos.com/" + i + 10 + ".png")))
          .tags(new ArrayList<>(
              Arrays.asList(Tag.builder().id(getRandomId()).name("Tag" + i).build(),
                  Tag.builder().id(getRandomId()).name("Tag" + i + 10).build())))
          .status(status).build();

      pets.add(pet);
    }
    return pets;
  }

  public static Pet createPetWithSpecifiedStatus(PetStatus status) {
    return Pet.builder()
        .id(getRandomId())
        .name("Button")
        .category(Category.builder()
            .id(getRandomId())
            .name("Category")
            .build()
        ).photoUrls(new ArrayList<>(
            Arrays.asList("www.petPhotos.com/1.jpg",
                "www.petPhotos.com/2.png")))
        .tags(new ArrayList<>(
            Arrays.asList(Tag.builder().id(getRandomId()).name("Tag1").build(),
                Tag.builder().id(getRandomId()).name("Tag2").build())))
        .status(status).build();
  }

  public static Pet updatePet(int petId, String name, String categoryName, String tagName,
      String photoUrl) {
    ArrayList<Tag> tags = new ArrayList<>(
        Arrays.asList(Tag.builder().id(getRandomId()).name(tagName).build(),
            Tag.builder().id(getRandomId()).name("Tag2").build()));

    ArrayList<String> petPhotos = new ArrayList<>(
        Arrays.asList("www.petPhotos.com/1.jpg", photoUrl, "www.petPhotos.com/3.png"));

    return Pet.builder()
        .id(petId)
        .name(name)
        .category(Category.builder()
            .id(getRandomId())
            .name(categoryName)
            .build()
        ).photoUrls(petPhotos)
        .tags(tags).status(getRandomPetStatus()).build();
  }

  public static Order createOrder(int petId, int quantity, OrderStatus status, boolean complete) {
    return Order.builder()
        .id(getRandomOrderNumber())
        .petId(petId)
        .quantity(quantity)
        .shipDate(new Date())
        .status(status)
        .complete(complete).build();
  }

  public static User createUser(String username, String firstName, String lastName, String email,
      String password, String phone, int status) {
    return User.builder()
        .id(getRandomId())
        .username(username)
        .firstName(firstName)
        .lastName(lastName)
        .email(email)
        .password(password)
        .phone(phone)
        .userStatus(status).build();
  }

  public static List<User> createUsers() {
    List<User> users = new ArrayList<>();
    for (int i = 0; i < 5; i++) {
      User user = User.builder()
          .id(getRandomId())
          .username(i + "username" + i)
          .firstName("firstName" + i)
          .lastName("lastName" + i)
          .email("testEmailCheck" + i + "gmail.com")
          .password("p@ssword" + i)
          .phone("38091115550" + i)
          .userStatus(i).build();
      users.add(user);
    }
    return users;
  }

}
