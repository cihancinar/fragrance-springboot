package com.fragrance.controller.dto;

import com.fragrance.models.entities.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * @author ccinar
 * @created 15/02/2022
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductCreateDto extends TechnicalDto {
    @NotEmpty
    private String name;
    private String description;
    private Set<String> tags;
    private Set<Gender> genders;
    private MultipartFile image;
    @NotNull
    private Long brand;
    private Set<Long> ingredients;
    private Set<Long> categories;
}
