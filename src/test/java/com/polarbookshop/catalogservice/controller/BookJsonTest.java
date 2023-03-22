package com.polarbookshop.catalogservice.controller;

import com.polarbookshop.catalogservice.config.PolarProperties;
import com.polarbookshop.catalogservice.domain.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest

@EnableConfigurationProperties(value= PolarProperties.class)
public class BookJsonTest {
    @Autowired
    private JacksonTester<Book> json;

    @Test
    void testSerialize() throws Exception{
        Book book = Book.builder()
                .isbn("1234567890")
                .price(9.90)
                .author("Authro")
                .title("Title").build();
        JsonContent<Book> jsonContent = json.write(book);

        assertThat(jsonContent).extractingJsonPathStringValue("@.isbn")
                .isEqualTo(book.getIsbn());
        assertThat(jsonContent).extractingJsonPathStringValue("@.author")
                .isEqualTo(book.getAuthor());
        assertThat(jsonContent).extractingJsonPathNumberValue("@.price")
                .isEqualTo(book.getPrice());
        assertThat(jsonContent).extractingJsonPathStringValue("@.title")
                .isEqualTo(book.getTitle());
    }

    @Test
    void testDeserialize() throws Exception{
        Book book = Book.builder()
                .isbn("1234567890")
                .price(9.90)
                .author("Author")
                .title("Title").build();
        String content = """
                {
                    "isbn": "1234567890",
                    "title": "Title",
                    "author": "Author",
                    "price": 9.90    
                    }           
                """;
        assertThat(json.parse(content)).usingRecursiveComparison()
                .isEqualTo(book);
    }
}
