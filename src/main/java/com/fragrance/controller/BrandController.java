package com.fragrance.controller;

import com.fragrance.controller.dto.BrandCreateDto;
import com.fragrance.models.entities.Brand;
import com.fragrance.controller.dto.BrandDto;
import com.fragrance.models.repositories.BrandRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
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
@Tag(name = "Brand")
@RestController
@RequiredArgsConstructor
public class BrandController {
    private final ModelMapper modelMapper;
    private final BrandRepository brandRepository;

    @SneakyThrows
    @Operation(summary = "Create Brand")
    @PostMapping(value = "/api/brands", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<BrandDto> create(@Valid @ModelAttribute BrandCreateDto brandDto,
                                           UriComponentsBuilder uriComponentsBuilder) {
        TypeMap<BrandDto, Brand> propertyMapper = modelMapper.createTypeMap(BrandDto.class, Brand.class);
        propertyMapper.addMapping(BrandDto::getImage, Brand::setImage);

        Brand brand = modelMapper.map(brandDto, Brand.class);
        if (brandDto.getImage() != null) {
            brand.setImage(brandDto.getImage().getBytes());
        }
        brand = brandRepository.save(brand);

        URI location = uriComponentsBuilder.path("/api/brands/{id}")
                .buildAndExpand(brand.getId()).toUri();

        BrandDto response = modelMapper.map(brand, BrandDto.class);
        return ResponseEntity.created(location).body(response);
    }

    @Operation(summary = "Retrieve Brand by its id")
    @GetMapping("/api/brands/{id}")
    public ResponseEntity<BrandDto> get(@PathVariable Long id) {
        return brandRepository.findById(id)
                .map(brand -> {
                    BrandDto response = modelMapper.map(brand, BrandDto.class);
                    return ResponseEntity.ok(response);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Operation(summary = "Update Brand by its id")
    @PutMapping(value = "/api/brands/{id}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<BrandDto> put(@Valid @ModelAttribute BrandCreateDto updateBrand,
                                        @PathVariable Long id) {
        return brandRepository.findById(id)
                .map(brand -> {
                    brand.setName(updateBrand.getName());
                    brand.setDescription(updateBrand.getDescription());
                    if (updateBrand.getImage() != null) {
                        try {
                            brand.setImage(updateBrand.getImage().getBytes());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    brand = brandRepository.save(brand);
                    BrandDto response = modelMapper.map(brand, BrandDto.class);
                    return ResponseEntity.ok(response);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Operation(summary = "Retrieve All Brands")
    @GetMapping("/api/brands")
    public ResponseEntity<List<BrandDto>> list() {
        List<BrandDto> response = brandRepository.findAll()
                .stream()
                .map(brand -> modelMapper.map(brand, BrandDto.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Remove Brand by its id")
    @DeleteMapping("/api/brands/{id}")
    public ResponseEntity<Object> remove(@PathVariable Long id) {
        return brandRepository.findById(id)
                .map(brand -> {
                    brandRepository.delete(brand);
                    return ResponseEntity.noContent().build();
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
