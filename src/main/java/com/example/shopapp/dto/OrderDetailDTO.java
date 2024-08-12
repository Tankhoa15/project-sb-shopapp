package com.example.shopapp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class OrderDetailDTO {

    @Min(value = 1, message = "Order id must be greater than 0")
    @JsonProperty("order_id")
    private Long orderId;

    @Min(value = 1, message = "Product id must be greater than 0")
    @JsonProperty("product_id")
    private Long productId;

    @Min(value = 0, message = "Price must be greater than 0")
    private Float price;

    @Min(value = 1, message = "Quantity must be greater than 0")
    @JsonProperty("number_of_products")
    private int numberOfProducts;

    @Min(value = 0, message = "Total money must be greater than 0")
    @JsonProperty("total_money")
    private Float totalMoney;

    private String color;
}
