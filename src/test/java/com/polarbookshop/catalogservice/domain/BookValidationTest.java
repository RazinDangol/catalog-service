package com.polarbookshop.catalogservice.domain;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.assertj.core.api.Assertions.assertThat;


class BookValidationTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void whenAllFieldsCorrectThenValidationSucceeds() {
        Book book = Book.builder()
                .isbn("1234567890")
                .title("Title")
                .price(9.9)
                .author("Author").build();
        Set<ConstraintViolation<Book>> violations = validator.validate(book);
        assertThat(violations).isEmpty();
    }

    @Test
    void whenIsbnDefinedButIncorrectThenValidationFails() {
        Book book = Book.builder()
                .isbn("1234asd7890")
                .title("Title")
                .price(9.9)
                .author("Author").build();
        Set<ConstraintViolation<Book>> violations = validator.validate(book);
        assertThat(violations.iterator().next().getMessage())
                .isEqualTo("The ISBN format must be valid.");
    }

    @Test
    void whenBookisNullThenValidationFails() {
        Book book = Book.builder()
                .isbn("1234567890")
                .price(9.9)
                .author("Author").build();
        Set<ConstraintViolation<Book>> violations = validator.validate(book);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("The book title must be defined.");
    }

    @Test
    void whenIsbnAuthorNotDefinedThenValidationFails(){
        Book book = Book.builder()
                .title("Title")
                .price(9.9)
                .build();
        Set<ConstraintViolation<Book>> violations = validator.validate(book);
        assertThat(violations).hasSize(2);
        assertThat(StreamSupport.stream(violations.spliterator(),true).
                filter(violation -> violation.getMessage().equalsIgnoreCase("The book author must be defined") || violation.getMessage().equalsIgnoreCase("The book ISBN must be defined."))
                .collect(Collectors.toList())).hasSize(2);
    }

    @Test
    void whenBookPriceisNegativeThenValidationFails(){
        Book book = Book.builder()
                .isbn("1234567890")
                .title("Title")
                .price(-0.6)
                .author("Author")
                .build();
        Set<ConstraintViolation<Book>>violations = validator.validate(book);
        assertThat(violations.iterator().next().getMessage().equalsIgnoreCase("The book price must be greater than zero."));
    }
}