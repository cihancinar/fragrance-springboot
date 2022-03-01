package com.fragrance;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;

/**
 * @author ccinar
 * @created 15/02/2022
 */
@ExtendWith(MockitoExtension.class)
public class AbstractMockMvcStandaloneControllerTest {
    protected MockMvc mockMvc;
}
