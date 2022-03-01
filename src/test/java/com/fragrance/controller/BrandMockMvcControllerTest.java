package com.fragrance.controller;

import com.fragrance.models.entities.Brand;
import com.fragrance.AbstractMockMvcControllerTest;
import com.fragrance.controller.dto.BrandDto;
import com.fragrance.models.repositories.BrandRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

/**
 * @author ccinar
 * @created 15/02/2022
 */
class BrandMockMvcControllerTest extends AbstractMockMvcControllerTest {

    @Autowired
    private JacksonTester<Brand> jsonBrand;

    @Autowired
    private BrandRepository brandRepository;

    @Test
    void createOk() throws Exception {
        // given
        BrandDto brand = new BrandDto();
        brand.setName("Boss");

        // when
        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/api/brands")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(brand))
        ).andDo(print()).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
    }

    @Test
    void createBadRequest() throws Exception {
        // given
        BrandDto brand = new BrandDto();

        // when
        MockHttpServletResponse response = mockMvc.perform(MockMvcRequestBuilders
                .post("/api/brands")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(brand))
        ).andDo(print()).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        //assertThat(response.get()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void getOk() throws Exception {
        // given
        Brand brand = new Brand();
        brand.setId(1L);
        brand.setName("Boss");
        brandRepository.save(brand);

        // when
        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/api/brands/1")
                        .accept(MediaType.APPLICATION_JSON)
        ).andDo(print()).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        /*assertThat(response.getContentAsString()).isEqualTo(
                jsonBrand.write(brand.get()).getJson()
        );*/
    }

    @Test
    void getNotFound() throws Exception {
        // when
        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/api/brands/1")
                        .accept(MediaType.APPLICATION_JSON)
        ).andDo(print()).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void list() {
    }

    @Test
    void remove() {
    }
}