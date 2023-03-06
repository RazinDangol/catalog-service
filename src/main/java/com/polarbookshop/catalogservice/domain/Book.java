package com.polarbookshop.catalogservice.domain;
import jakarta.validation.constraints.NotBlank;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.ToString;

import java.util.Properties;

@Builder
@ToString
public class Book {
    @NotBlank(message = "The book ISBN must be defined.")
    @Pattern(
            regexp="^([0-9]{10}|[0-9]{13})$",
            message="The ISBN format must be valid."
    )
    private  String isbn;

    @NotBlank(message="The book title must be defined.")
    private String title;

    @NotBlank(message="The book author must be defined")
    private String author;

    @NotNull(message = "The book price must be defined.")
    @Positive(message="The book price must be greater than zero.")
    private Double price;

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
