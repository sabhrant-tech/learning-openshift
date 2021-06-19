package org.sabhrant.tech.springboot.web.app.openshift;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.sabhrant.tech.springboot.web.app.openshift.entity.Book;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class SpringBootBootstrapLiveTest {

	private static final String API_ROOT = "http://localhost:8081/api/books";
	
	@Test
	public void whenGetAllBooks_ThenOK() {
		Response response = RestAssured.get(API_ROOT);
		assertEquals(HttpStatus.OK.value(), response.getStatusCode());
	}
	
	@Test
	public void whenGetBookByTitle_ThenOK() {
		Book book = createRandomBook();
		createBookAsUri(book);
		Response response = RestAssured.get(API_ROOT + "/title/" + book.getTitle());
		assertEquals(HttpStatus.OK.value(), response.getStatusCode());
		assertTrue(response.as(List.class).size() > 0);
	}
	
	@Test
	public void whenGetCreatedBookById_thenOK() {
		Book book = createRandomBook();
		String location = createBookAsUri(book);
		Response response = RestAssured.get(location);
		assertEquals(HttpStatus.OK.value(), response.getStatusCode());
		assertEquals(book.getTitle(), response.jsonPath().get("title"));
	}
	
	@Test
	public void whenGetNotExistBookById_thenNotFound() {
		Response response = RestAssured.get(API_ROOT + "/" + RandomStringUtils.randomNumeric(4));
	    assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCode());
	}
	
	@Test
	public void whenCreateNewBook_thenCreated() {
		Book book = createRandomBook();
		Response response = RestAssured.given()
			      .contentType(MediaType.APPLICATION_JSON_VALUE)
			      .body(book)
			      .post(API_ROOT);
		assertEquals(HttpStatus.CREATED.value(), response.getStatusCode());
	}
	
	@Test
	public void whenInvalidBook_thenError() {
		Book book = createRandomBook();
		book.setAuthor(null);
		Response response = RestAssured.given()
			      .contentType(MediaType.APPLICATION_JSON_VALUE)
			      .body(book)
			      .post(API_ROOT);
		assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode());
	}
	
	@Test
	public void whenUpdateCreatedBook_thenUpdated() {
		Book book = createRandomBook();
		String location = createBookAsUri(book);
		book.setId(Long.parseLong(location.split("/api/books/")[1]));
		book.setAuthor("newAuthor");
		Response response = RestAssured.given()
			      .contentType(MediaType.APPLICATION_JSON_VALUE)
			      .body(book)
			      .put(location);
		
		assertEquals(HttpStatus.OK.value(), response.getStatusCode());
		
		response = RestAssured.get(location);
		
		assertEquals(HttpStatus.OK.value(), response.getStatusCode());
		assertEquals("newAuthor", response.jsonPath().get("author"));
	}
	
	@Test
	public void whenDeleteCreatedBook_thenOK() {
		Book book = createRandomBook();
		String location = createBookAsUri(book);
		Response response = RestAssured.delete(location);
		
		assertEquals(HttpStatus.OK.value(), response.getStatusCode());
		
		response = RestAssured.get(location);
		assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCode());
	}
	
	private Book createRandomBook() {
		Book book = new Book();
		book.setAuthor(RandomStringUtils.randomAlphabetic(10));
		book.setTitle(RandomStringUtils.randomAlphabetic(15));
		return book;
	}
	
	private String createBookAsUri(Book book) {
		Response response = RestAssured.given()
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.body(book)
				.post(API_ROOT);
		return API_ROOT + "/" + response.jsonPath().get("id");
	}
}
