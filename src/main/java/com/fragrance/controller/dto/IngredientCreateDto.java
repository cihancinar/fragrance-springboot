package com.fragrance.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;

/**
 * @author ccinar
 * @created 15/02/2022
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IngredientCreateDto extends TechnicalDto {
    @NotEmpty
    private String name;
    private String description;
    private MultipartFile image;
}
