package com.petstore.autotests.model;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Pet {

  private int id;
  private Category category;
  private String name;
  private List<String> photoUrls;
  private List<Tag> tags;
  private PetStatus status;
}
