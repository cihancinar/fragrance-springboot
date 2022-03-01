package com.fragrance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

/**
 * @author ccinar
 * @created 15/02/2022
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AbstractWebServerControllerTest {
    @Autowired
    protected TestRestTemplate restTemplate;
}
