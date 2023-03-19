package com.polarbookshop.catalogservice.controller;

import com.polarbookshop.catalogservice.Exception.BookNotFoundException;
import com.polarbookshop.catalogservice.app.AppConfig;
import com.polarbookshop.catalogservice.config.PolarProperties;
import com.polarbookshop.catalogservice.service.BookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)

@EnableConfigurationProperties(value= PolarProperties.class)
class BookControllerMvcTest {
    @Autowired
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @Test
    void whenGetBookNotExistingThenShouldReturn404() throws Exception{
        String isbn = "1234567890";
        given(bookService.viewBookDetails(isbn)).willThrow(BookNotFoundException.class);

        mockMvc.perform(MockMvcRequestBuilders.get("/books/"+isbn))
                .andExpect(status().isNotFound());

    }

}