package org.sabhrant.tech.springboot.web.app.openshift.repository;

import java.util.List;

import org.sabhrant.tech.springboot.web.app.openshift.entity.Book;
import org.springframework.data.repository.CrudRepository;

public interface BookRepository extends CrudRepository<Book, Long> {

	List<Book> findByTitle(String title);
}
