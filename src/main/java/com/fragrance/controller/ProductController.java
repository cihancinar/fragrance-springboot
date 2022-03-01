package com.fragrance.controller;

import com.fragrance.controller.dto.ModelCreateDto;
import com.fragrance.controller.dto.ModelDto;
import com.fragrance.controller.dto.ProductCreateDto;
import com.fragrance.controller.dto.ProductDto;
import com.fragrance.models.entities.*;
import com.fragrance.models.repositories.*;
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
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author ccinar
 * @created 15/02/2022
 */
@Tag(name = "Product")
@RestController
@RequiredArgsConstructor
public class ProductController {
    private final ModelMapper modelMapper;

    private final ProductRepository productRepository;
    private final ModelRepository modelRepository;

    private final IngredientRepository ingredientRepository;
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;

    @SneakyThrows
    @Operation(summary = "Create Product")
    @PostMapping(value = "/api/products", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<ProductDto> create(@Valid @ModelAttribute ProductCreateDto productDto,
                                             UriComponentsBuilder uriComponentsBuilder) {
        Product product = modelMapper.map(productDto, Product.class);

        if (productDto.getIngredients() != null && !productDto.getIngredients().isEmpty()) {
            List<Ingredient> ingredients = ingredientRepository.findAllById(productDto.getIngredients());
            product.setIngredients(Set.copyOf(ingredients));
        }

        if (productDto.getCategories() != null && !productDto.getCategories().isEmpty()) {
            List<Category> categories = categoryRepository.findAllById(productDto.getCategories());
            product.setCategories(Set.copyOf(categories));
        }

        Optional<Brand> brand = brandRepository.findById(productDto.getBrand());
        if (brand.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        product.setBrand(brand.get());

        if (productDto.getImage() != null) {
            product.setImage(productDto.getImage().getBytes());
        }

        product = productRepository.save(product);

        URI location = uriComponentsBuilder.path("/api/products/{id}")
                .buildAndExpand(product.getId()).toUri();

        ProductDto response = modelMapper.map(product, ProductDto.class);
        return ResponseEntity.created(location).body(response);
    }

    @Operation(summary = "Create Product Model")
    @PostMapping("/api/products/{id}/models")
    public ResponseEntity<ModelDto> create(@Valid @RequestBody ModelCreateDto modelDto,
                                           @PathVariable Long id,
                                           UriComponentsBuilder uriComponentsBuilder) {
        Model model = modelMapper.map(modelDto, Model.class);
        Optional<Product> product = productRepository.findById(id);
        if (product.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        model.setProduct(product.get());
        model = modelRepository.save(model);

        URI location = uriComponentsBuilder.path("/api/products/{id}/models/{id}")
                .buildAndExpand(model.getProduct().getId(), model.getId()).toUri();

        ModelDto response = modelMapper.map(model, ModelDto.class);
        return ResponseEntity.created(location).body(response);
    }

    @Operation(summary = "Retrieve Product by its id")
    @GetMapping("/api/products/{id}")
    public ResponseEntity<ProductDto> get(@PathVariable Long id) {
        return productRepository.findById(id)
                .map(product -> {
                    ProductDto response = modelMapper.map(product, ProductDto.class);
                    return ResponseEntity.ok(response);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Operation(summary = "Retrieve Product Model by its id")
    @GetMapping("/api/products/{idProduct}/models/{idModel}")
    public ResponseEntity<ModelDto> get(@PathVariable(name = "idProduct") Long idProduct,
                                        @PathVariable(name = "idModel") Long idModel) {
        return modelRepository.findByProductIdAndId(idProduct, idModel)
                .map(model -> {
                    ModelDto response = modelMapper.map(model, ModelDto.class);
                    return ResponseEntity.ok(response);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }


    @Operation(summary = "Retrieve All Products")
    @GetMapping("/api/products")
    public ResponseEntity<List<ProductDto>> list() {
        List<ProductDto> response = productRepository.findAll()
                .stream()
                .map(product -> modelMapper.map(product, ProductDto.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Retrieve All Products Model")
    @GetMapping("/api/products/{id}/models")
    public ResponseEntity<List<ModelDto>> list(@PathVariable Long id) {
        List<ModelDto> response = modelRepository.findByProductId(id)
                .stream()
                .map(model -> modelMapper.map(model, ModelDto.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Remove Product by its id")
    @DeleteMapping("/api/products/{id}")
    public ResponseEntity<Object> remove(@PathVariable Long id) {
        return productRepository.findById(id)
                .map(product -> {
                    productRepository.delete(product);
                    return ResponseEntity.noContent().build();
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @Operation(summary = "Remove Product Model by its id")
    @DeleteMapping("/api/products/{idProduct}/models/{idModel}")
    public ResponseEntity<Object> remove(@PathVariable(name = "idProduct") Long idProduct,
                                         @PathVariable(name = "idModel") Long idModel) {
        return modelRepository.findByProductIdAndId(idProduct, idModel)
                .map(model -> {
                    modelRepository.delete(model);
                    return ResponseEntity.noContent().build();
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

}
