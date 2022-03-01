package com.fragrance.controller;

import com.fragrance.controller.dto.IngredientCreateDto;
import com.fragrance.models.entities.Ingredient;
import com.fragrance.controller.dto.IngredientDto;
import com.fragrance.models.repositories.IngredientRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ccinar
 * @created 15/02/2022
 */
@Tag(name = "Ingredient")
@RestController
@RequiredArgsConstructor
public class IngredientController {
    private final ModelMapper modelMapper;
    private final IngredientRepository ingredientRepository;

    @SneakyThrows
    @Operation(summary = "Create Ingredient")
    @PostMapping(value = "/api/ingredients", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<IngredientDto> create(@Valid @ModelAttribute IngredientCreateDto ingredientDto,
                                                UriComponentsBuilder uriComponentsBuilder) {
        Ingredient ingredient = modelMapper.map(ingredientDto, Ingredient.class);
        if (ingredientDto.getImage() != null) {
            ingredient.setImage(ingredientDto.getImage().getBytes());
        }
        ingredient = ingredientRepository.save(ingredient);

        URI location = uriComponentsBuilder.path("/api/ingredients/{id}")
                .buildAndExpand(ingredient.getId()).toUri();

        IngredientDto response = modelMapper.map(ingredient, IngredientDto.class);
        return ResponseEntity.created(location).body(response);
    }

    @Operation(summary = "Retrieve Ingredient by its id")
    @GetMapping("/api/ingredients/{id}")
    public ResponseEntity<IngredientDto> get(@PathVariable Long id) {
        return ingredientRepository.findById(id)
                .map(ingredient -> {
                    IngredientDto response = modelMapper.map(ingredient, IngredientDto.class);
                    return ResponseEntity.ok(response);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Operation(summary = "Update Ingredient by its id")
    @PutMapping(value = "/api/ingredients/{id}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<IngredientDto> put(@Valid @ModelAttribute IngredientCreateDto ingredientDto,
                                             @PathVariable Long id) {
        return ingredientRepository.findById(id)
                .map(ingredient -> {
                    ingredient.setName(ingredientDto.getName());
                    ingredient.setDescription(ingredientDto.getDescription());
                    if (ingredientDto.getImage() != null) {
                        try {
                            ingredient.setImage(ingredientDto.getImage().getBytes());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    ingredient = ingredientRepository.save(ingredient);
                    IngredientDto response = modelMapper.map(ingredient, IngredientDto.class);
                    return ResponseEntity.ok(response);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Operation(summary = "Retrieve All Ingredients")
    @GetMapping("/api/ingredients")
    public ResponseEntity<List<IngredientDto>> list() {
        List<IngredientDto> response = ingredientRepository.findAll()
                .stream()
                .map(ingredient -> modelMapper.map(ingredient, IngredientDto.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Remove Ingredient by its id")
    @DeleteMapping("/api/ingredients/{id}")
    public ResponseEntity<Object> remove(@PathVariable Long id) {
        return ingredientRepository.findById(id)
                .map(ingredient -> {
                    ingredientRepository.delete(ingredient);
                    return ResponseEntity.noContent().build();
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
