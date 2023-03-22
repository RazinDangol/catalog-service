package com.polarbookshop.catalogservice.service;

import com.polarbookshop.catalogservice.Exception.BookAlreadyExistsException;
import com.polarbookshop.catalogservice.Exception.BookNotFoundException;
import com.polarbookshop.catalogservice.domain.Book;
import com.polarbookshop.catalogservice.repository.BookRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.assertj.core.api.Assertions.assertThat;

public class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookServiceImpl bookService;

    @BeforeEach
    void init(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testviewBookListShouldReturnAllbooks(){
        Book book1 = Book.builder()
                .isbn("1234567890")
                .author("Author")
                .title("title")
                .price(9.90).build();
        Book book2 = Book.builder()
                .isbn("1234567890")
                .author("Author")
                .title("title")
                .price(9.90).build();
        ArrayList<Book> bookList = new ArrayList<Book>();
        bookList.add(book1);
        bookList.add(book2);

        Mockito.when(this.bookRepository.findAll()).thenReturn(bookList);
        Iterable<Book> actualResult = this.bookService.viewBookList();

        assertThat(StreamSupport.stream(actualResult.spliterator(),true)
                .filter(book-> book.getIsbn().equals(book1.getIsbn())|| book.getIsbn().equals(book2.getIsbn()))
                .collect(Collectors.toList())).hasSize(2);
    }


    @Test
    void testViewBookDetailsByIsbnFound(){
        Book book1 = Book.builder()
                .isbn("1234567890")
                .author("Author")
                .title("title")
                .price(9.90).build();
        Mockito.when(this.bookRepository.findByIsbn(ArgumentMatchers.anyString())).thenReturn(Optional.of(book1));
       Book actualBook = this.bookService.viewBookDetails("1234567890");
        assertThat(actualBook.getIsbn()).isEqualTo("1234567890");
    }

    @Test
    void testViewBookDetailsByIsbnNotFound(){
        Assertions.assertThrows(BookNotFoundException.class, ()->
                this.bookService.viewBookDetails("1234567890"));
    }

    @Test
    void testAddBookToCatalogWhennotExists(){
        Book book1 = Book.builder()
                .isbn("1234567890")
                .author("Author")
                .title("title")
                .price(9.90).build();
        Mockito.when(this.bookRepository.existsByIsbn(ArgumentMatchers.anyString())).thenReturn(false);
        Mockito.when(this.bookRepository.save(ArgumentMatchers.any())).thenReturn(book1);

        Book actualBook = this.bookService.addBookToCatalog(book1);
        assertThat(actualBook.getIsbn()).isEqualTo(book1.getIsbn());
    }

    @Test
    void testAddBookToCatalogWhenExists(){
        Book book1 = Book.builder()
                .isbn("1234567890")
                .author("Author")
                .title("title")
                .price(9.90).build();
        Mockito.when(this.bookRepository.existsByIsbn(ArgumentMatchers.anyString())).thenReturn(true);

        Assertions.assertThrows(BookAlreadyExistsException.class, ()->
                this.bookService.addBookToCatalog(book1));

    }

    @Test
    void testEditBookDetailsWhenBookExists(){
        Book existingBook = Book.builder()
                .id(123L)
                .isbn("1234567890")
                .author("Author")
                .title("title")
                .price(9.90).build();

        Book updatedBook = Book.builder()
                .isbn("1234567890")
                .author("Author updated")
                .title("title updated")
                .price(10.22).build();

        Mockito.when(this.bookRepository.findByIsbn(ArgumentMatchers.any())).thenReturn(Optional.of(existingBook));
        ArgumentCaptor<Book> bookArgumentCaptor = ArgumentCaptor.forClass(Book.class);
        this.bookService.editBookDetails("1234567890",updatedBook);
//        Mockito.verify(this.bookRepository,Mockito.atMostOnce()).save(bookArgumentCaptor.capture());
//        Book actualBook=bookArgumentCaptor.getValue();
//        assertThat(actualBook.getId()).isEqualTo(existingBook.getId());
//        assertThat(actualBook.getAuthor()).isEqualTo(updatedBook.getAuthor());
//        assertThat(actualBook.getTitle()).isEqualTo(updatedBook.getTitle());
//        assertThat(actualBook.getPrice()).isEqualTo(updatedBook.getPrice());
    }
}
