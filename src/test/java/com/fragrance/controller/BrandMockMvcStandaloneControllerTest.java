package com.fragrance.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fragrance.AbstractMockMvcStandaloneControllerTest;
import com.fragrance.models.entities.Brand;
import com.fragrance.controller.dto.BrandDto;
import com.fragrance.models.repositories.BrandRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

/**
 * @author ccinar
 * @created 15/02/2022
 */
class BrandMockMvcStandaloneControllerTest extends AbstractMockMvcStandaloneControllerTest {

    @Mock
    private BrandRepository brandRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private BrandController brandController;

    private JacksonTester<BrandDto> jsonBrandDto;

    @BeforeEach
    public void setup() {
        JacksonTester.initFields(this, new ObjectMapper());
        mockMvc = MockMvcBuilders.standaloneSetup(brandController)
                //.setControllerAdvice(new ExceptionHandler())
                //.addFilters(new Filter())
                .build();
    }


    @Test
    void createOk() throws Exception {
        // given
        BrandDto brandDto = new BrandDto();
        brandDto.setName("Boss");

        Brand brand = new Brand();
        brand.setName("Boss");

        given(brandRepository.save(any())).willReturn(brand);
        given(modelMapper.map(any(), eq(Brand.class))).willReturn(brand);
        given(modelMapper.map(any(), eq(BrandDto.class))).willReturn(brandDto);

        // when
        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders
                        .post("/api/brands")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBrandDto.write(brandDto).getJson())
        ).andDo(print()).andReturn().getResponse();

        // then
        assertThat(response.getStatus()).isEqualTo(HttpStatus.CREATED.value());
    }

    /*
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
    */
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
        // given
        // when
        MockHttpServletResponse response = mockMvc.perform(
                MockMvcRequestBuilders
                        .get("/api/brands/999999")
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