package com.fragrance.controller;

import com.fragrance.controller.dto.CategoryCreateDto;
import com.fragrance.models.entities.Category;
import com.fragrance.controller.dto.CategoryDto;
import com.fragrance.models.repositories.CategoryRepository;
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
@Tag(name = "Category")
@RestController
@RequiredArgsConstructor
public class CategoryController {
    private final ModelMapper modelMapper;
    private final CategoryRepository categoryRepository;

    @SneakyThrows
    @Operation(summary = "Create Category")
    @PostMapping(value = "/api/categories", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<CategoryDto> create(@Valid @ModelAttribute CategoryCreateDto categoryDto,
                                              UriComponentsBuilder uriComponentsBuilder) {
        Category category = modelMapper.map(categoryDto, Category.class);
        if (categoryDto.getImage() != null) {
            category.setImage(categoryDto.getImage().getBytes());
        }
        category = categoryRepository.save(category);

        URI location = uriComponentsBuilder.path("/api/categories/{id}")
                .buildAndExpand(category.getId()).toUri();

        CategoryDto response = modelMapper.map(category, CategoryDto.class);
        return ResponseEntity.created(location).body(response);
    }

    @Operation(summary = "Retrieve Category by its id")
    @GetMapping("/api/categories/{id}")
    public ResponseEntity<CategoryDto> get(@PathVariable Long id) {
        return categoryRepository.findById(id)
                .map(category -> {
                    CategoryDto response = modelMapper.map(category, CategoryDto.class);
                    return ResponseEntity.ok(response);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Operation(summary = "Update Category by its id")
    @PutMapping(value = "/api/categories/{id}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<CategoryDto> put(@Valid @ModelAttribute CategoryCreateDto categoryDto,
                                           @PathVariable Long id) {
        return categoryRepository.findById(id)
                .map(category -> {
                    category.setName(categoryDto.getName());
                    category.setDescription(categoryDto.getDescription());
                    if (categoryDto.getImage() != null) {
                        try {
                            category.setImage(categoryDto.getImage().getBytes());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    category = categoryRepository.save(category);
                    CategoryDto response = modelMapper.map(category, CategoryDto.class);
                    return ResponseEntity.ok(response);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Operation(summary = "Retrieve All Categories")
    @GetMapping("/api/categories")
    public ResponseEntity<List<CategoryDto>> list() {
        List<CategoryDto> response = categoryRepository.findAll()
                .stream()
                .map(category -> modelMapper.map(category, CategoryDto.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Remove Category by its id")
    @DeleteMapping("/api/categories/{id}")
    public ResponseEntity<Object> remove(@PathVariable Long id) {
        return categoryRepository.findById(id)
                .map(category -> {
                    categoryRepository.delete(category);
                    return ResponseEntity.noContent().build();
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
