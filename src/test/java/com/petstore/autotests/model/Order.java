package com.petstore.autotests.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Order {

  private int id;
  private int petId;
  private int quantity;
  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
  private Date shipDate;
  private OrderStatus status;
  private boolean complete;
}
