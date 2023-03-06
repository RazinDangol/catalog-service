package com.polarbookshop.catalogservice.app;

import com.polarbookshop.catalogservice.domain.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(
		webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
class CatalogServiceApplicationTests {

	@Autowired
	private WebTestClient webTestClient;

	@Test
	void whenPostRequestThenBookCreated(){
		Book expectedBook = Book.builder()
				.isbn("1234567890")
				.author("Author")
				.price(9.09)
				.title("Title").build();
		webTestClient.post()
				.uri("/books")
				.bodyValue(expectedBook)
				.exchange()
				.expectStatus().isCreated()
				.expectBody(Book.class).value(actualBook->{
					assertThat(actualBook).isNotNull();
					assertThat(actualBook.getIsbn()).isEqualTo(expectedBook.getIsbn());
				});
	}

}
