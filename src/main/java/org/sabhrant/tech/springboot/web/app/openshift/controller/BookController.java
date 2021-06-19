package org.sabhrant.tech.springboot.web.app.openshift.controller;

import java.util.List;

import org.sabhrant.tech.springboot.web.app.openshift.entity.Book;
import org.sabhrant.tech.springboot.web.app.openshift.exception.BookIdMismatchException;
import org.sabhrant.tech.springboot.web.app.openshift.exception.BookNotFoundException;
import org.sabhrant.tech.springboot.web.app.openshift.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/books")
public class BookController {

	@Autowired
	private BookRepository repository;
	
	@GetMapping
	public Iterable<Book> findAll() {
		return repository.findAll();
	}
	
	@GetMapping("/title/{bookTitle}")
	public List<Book> findByTitle(@PathVariable String bookTitle) {
		return repository.findByTitle(bookTitle);
	}
	
	@GetMapping("/{id}")
	public Book findOne(@PathVariable Long id) {
		return repository.findById(id).orElseThrow(BookNotFoundException::new);
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Book create(@RequestBody Book book) {
		return repository.save(book);
	}
	
	@DeleteMapping("/{id}")
	public void delete(@PathVariable Long id) {
		repository.findById(id).orElseThrow(BookNotFoundException::new);
		repository.deleteById(id);
	}
	
	@PutMapping("/{id}")
	public Book updateBook(@RequestBody Book book, @PathVariable Long id) {
		if (book.getId() != id) {
			throw new BookIdMismatchException();
		}
		repository.findById(id).orElseThrow(BookNotFoundException::new);
		return repository.save(book);
	}
}
