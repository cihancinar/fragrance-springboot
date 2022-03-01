package com.fragrance.controller.dto;

import com.fragrance.models.entities.enums.Capacity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

/**
 * @author ccinar
 * @created 15/02/2022
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModelDto extends TechnicalDto {
    @NotEmpty
    private String name;
    private String description;
    private double price;
    private double capacityMl;
    private Capacity capacityName;
    private byte[] image;
    @Valid
    private ProductDto product;
}
