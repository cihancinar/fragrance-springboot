package com.fragrance.controller.dto;

import com.fragrance.models.entities.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.Set;

/**
 * @author ccinar
 * @created 15/02/2022
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto extends TechnicalDto {
    @NotEmpty
    private String name;
    private String description;
    private Set<String> tags;
    private Set<Gender> genders;
    private byte[] image;

    private BrandDto brand;
    private Set<IngredientDto> ingredients;
    private Set<CategoryDto> categories;
}
