package com.polarbookshop.catalogservice.demo;

import com.polarbookshop.catalogservice.domain.Book;
import com.polarbookshop.catalogservice.repository.BookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Profile("testdata")
public class BookDataLoader {
    Logger logger = LoggerFactory.getLogger(BookDataLoader.class);
    private final BookRepository bookRepository;
    public BookDataLoader(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void loadBookTestData(){
        Book book1 = Book.builder()
                .isbn("1234567890")
                .author("author")
                .title("title")
                .price(9.90).build();
        Book book2 = Book.builder()
                .isbn("1234567888")
                .author("author2")
                .title("title2")
                .price(9.90).build();
        bookRepository.save(book1);
        bookRepository.save(book2);

        logger.info("loaded data");
    }
}

