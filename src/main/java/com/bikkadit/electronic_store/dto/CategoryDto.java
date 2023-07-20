package com.bikkadit.electronic_store.dto;

import com.bikkadit.electronic_store.validate.ImageNameValid;
import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryDto {

    private String categoryId;

    @Size(min=4,message = "min 4 characters  must be required !!")
    @NotBlank(message = "title must be required !!")
    private String title;

    @NotBlank(message="category description required !!")
    private String categoryDescription;

    @ImageNameValid
    private String coverImage;
}
