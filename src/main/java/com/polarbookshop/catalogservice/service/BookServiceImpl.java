package com.polarbookshop.catalogservice.service;

import com.polarbookshop.catalogservice.Exception.BookAlreadyExistsException;
import com.polarbookshop.catalogservice.Exception.BookNotFoundException;
import com.polarbookshop.catalogservice.domain.Book;
import com.polarbookshop.catalogservice.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookServiceImpl implements BookService{
    @Autowired
    private BookRepository bookRepository;

    @Override
    public Iterable<Book> viewBookList() {
        return this.bookRepository.findAll();
    }

    @Override
    public Book viewBookDetails(String isbn) {
        return bookRepository.findByIsbn(isbn)
                .orElseThrow(()->new BookNotFoundException(isbn));
    }

    @Override
    public Book addBookToCatalog(Book book) {
        if(bookRepository.existsByIsbn(book.getIsbn())){
            throw new BookAlreadyExistsException(book.getIsbn());
        }
        return bookRepository.save(book);
    }

    @Override
    public void removeBookFromCatalog(String isbn) {
    bookRepository.deleteByIsbn(isbn);
    }

    @Override
    public Book editBookDetails(String isbn, Book book) {
        return bookRepository.findByIsbn(isbn)
                .map(existingBook->{
                    Book bookToUpdate = Book.builder()
                            .author(existingBook.getAuthor())
                            .isbn(existingBook.getIsbn())
                            .price(existingBook.getPrice()).build();
                return bookRepository.save(bookToUpdate);
                })
                .orElseGet(()->addBookToCatalog(book));

    }
}
