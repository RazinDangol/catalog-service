package com.polarbookshop.catalogservice.repository;

import com.polarbookshop.catalogservice.config.DataConfig;
import com.polarbookshop.catalogservice.domain.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.data.jdbc.core.JdbcAggregateTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.assertj.core.api.Assertions.assertThat;
@DataJdbcTest
@Import(DataConfig.class)
@AutoConfigureTestDatabase(
        replace=AutoConfigureTestDatabase.Replace.NONE
)
@ActiveProfiles("integration")
public class BookRepositoryJdbcTests {
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private JdbcAggregateTemplate jdbcAggregateTemplate;
    @BeforeEach
    void setup(){
        this.jdbcAggregateTemplate.deleteAll(Book.class);

    }

    @Test
    void findBookbyISBNWhenExisting(){
        String bookIsbn = "1234567890";
        Book book = Book.builder()
                .isbn(bookIsbn)
                .title("title")
                .author("author")
                .price(9.90).build();
        jdbcAggregateTemplate.insert(book);
        Optional<Book> actualBook = this.bookRepository.findByIsbn(bookIsbn);

        assertThat(actualBook).isPresent();
        assertThat(actualBook.get().getIsbn()).isEqualTo(book.getIsbn());
    }

    @Test
    void findAllBooks(){
        Book book1 = Book.builder()
                .isbn("1234567890")
                .title("title")
                .author("author")
                .price(9.90).build();
        Book book2 = Book.builder()
                .isbn("1234567880")
                .title("title")
                .author("author")
                .price(8.90).build();

        jdbcAggregateTemplate.insert(book1);
        jdbcAggregateTemplate.insert(book2);

        Iterable<Book> books = this.bookRepository.findAll();

        assertThat(StreamSupport.stream(books.spliterator(),true)
                .filter(book->book.getIsbn().equals(book1.getIsbn()) || book.getIsbn().equals(book2.getIsbn()))
                .collect(Collectors.toList())).hasSize(2);

    }

    @Test
    void findBookbyISBNWhenNotExisting(){
        Optional<Book> actualBook = this.bookRepository.findByIsbn("1234565780");
        assertThat(actualBook).isEmpty();
    }

    @Test
    void existsByIsbn(){
        String bookIsbn = "1234567890";
        Book book = Book.builder()
                .isbn(bookIsbn)
                .title("title")
                .author("author")
                .price(9.90).build();
        jdbcAggregateTemplate.insert(book);

        boolean existsByIsbn = this.bookRepository.existsByIsbn(bookIsbn);
        assertThat(existsByIsbn).isTrue();

    }

    @Test
    void notexistsByIsbn(){
        boolean notexistsByIsbn = this.bookRepository.existsByIsbn("1234567890");
        assertThat(notexistsByIsbn).isFalse();
    }

    @Test
    void deleteByIsbn(){

        String bookIsbn = "1234567590";
        Book book = Book.builder()
                .isbn(bookIsbn)
                .title("title")
                .author("author")
                .price(9.90).build();
        Book persistedBook = this.jdbcAggregateTemplate.insert(book);

        this.bookRepository.deleteByIsbn(bookIsbn);
        assertThat(jdbcAggregateTemplate.findById(persistedBook.getId(),Book.class)).isNull();
    }

    @Test
    void notdeleteByIsbn(){
        String bookIsbn = "1234567890";
        Book book = Book.builder()
                .isbn(bookIsbn)
                .title("title")
                .author("author")
                .price(9.90).build();
        Book persistedBook = jdbcAggregateTemplate.insert(book);
        this.bookRepository.deleteByIsbn("1234563332");
        assertThat(jdbcAggregateTemplate.findById(persistedBook.getId(),Book.class)).isNotNull();

    }


}
