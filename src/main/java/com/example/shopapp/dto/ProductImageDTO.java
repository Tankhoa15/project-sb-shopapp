package com.example.shopapp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductImageDTO {

    @Min(value = 1, message = "Product ID must be greater than 0")
    @JsonProperty("product_id")
    private Long productId;

    @Size(min = 5, max = 255, message = "Image URL must not exceed 255 characters")
    @JsonProperty("image_url")
    private String imageUrl;
}
