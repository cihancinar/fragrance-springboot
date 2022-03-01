package com.fragrance.controller;

import com.fragrance.models.entities.Brand;
import com.fragrance.AbstractWebServerControllerTest;
import com.fragrance.controller.dto.BrandDto;
import com.fragrance.models.repositories.BrandRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author ccinar
 * @created 15/02/2022
 */
class BrandWebServerControllerTest extends AbstractWebServerControllerTest {

    @Autowired
    private BrandRepository brandRepository;

    @Test
    void createOk() {
        // when
        BrandDto brandDto = new BrandDto();
        brandDto.setName("Boss");

        ResponseEntity<BrandDto> response =
                restTemplate.postForEntity("/api/brands", brandDto, BrandDto.class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    void createBadRequest() {
        // when
        BrandDto brandDto = new BrandDto();

        ResponseEntity<BrandDto> response =
                restTemplate.postForEntity("/api/brands", brandDto, BrandDto.class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void getOk() {
        // given
        Brand brand = new Brand();
        brand.setId(1L);
        brand.setName("Boss");
        brandRepository.save(brand);

        BrandDto brandDto = new BrandDto();
        brandDto.setId(1L);
        brandDto.setName("Boss");

        // when
        ResponseEntity<BrandDto> response =
                restTemplate.getForEntity("/api/brands/1", BrandDto.class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(brandDto);
    }

    @Test
    void getNotFound() {
        // when
        ResponseEntity<BrandDto> response = restTemplate.getForEntity("/api/brands/1", BrandDto.class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNull();

    }

    @Test
    void list() {
        // given
        Brand brand = new Brand();
        brand.setId(1L);
        brand.setName("Boss");
        brandRepository.save(brand);

        // when
        ResponseEntity<List<BrandDto>> response = restTemplate.getForEntity("/api/brands", (Class) List.class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertFalse(response.getBody().isEmpty());
    }

    @Test
    void listEmpty() {
        // when
        ResponseEntity<List<BrandDto>> response = restTemplate.getForEntity("/api/brands", (Class) List.class);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertTrue(response.getBody().isEmpty());
    }

}