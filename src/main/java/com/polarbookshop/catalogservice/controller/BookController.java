package com.polarbookshop.catalogservice.controller;

import com.polarbookshop.catalogservice.domain.Book;
import com.polarbookshop.catalogservice.service.BookService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("books")
public class BookController {
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public Iterable<Book> get(){
        return bookService.viewBookList();
    }

    @GetMapping("{isbn}")
    public Book getByIsbn(@PathVariable String isbn){
        return bookService.viewBookDetails(isbn);
    }

    @PostMapping
    public Book addBooktoCatalog(@Valid @RequestBody Book book){
        return bookService.addBookToCatalog(book);
    }

    @DeleteMapping("{isbn}")
    public void deleteBookByIsbn(@PathVariable String isbn){
        bookService.removeBookFromCatalog(isbn);
    }

    @PutMapping("{isbn}")
    public Book updateBook(@PathVariable String isbn,@Valid @RequestBody Book book){
        return bookService.editBookDetails(isbn,book);
    }
}
