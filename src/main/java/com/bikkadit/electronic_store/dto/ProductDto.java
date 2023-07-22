package com.bikkadit.electronic_store.dto;


import com.bikkadit.electronic_store.validate.ImageNameValid;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.validation.constraints.NotBlank;
import java.util.Date;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDto {

    private String productId;

    @NotBlank(message = "title must be required")
    @Size(min=4,message = "Minimum 4 characters required !!")
    private String title;

    @NotBlank(message = "product description required !!")
    private String description;

    private Double price;
    private Double discountedPrice;
    private Integer quantity;
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "dd-MM-yyyy")
    private Date addedDate;
    private boolean live;
    private boolean stock;
    @ImageNameValid
    private String productImage;
}
