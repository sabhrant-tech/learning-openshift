package org.sabhrant.tech.springboot.web.app.openshift;

import org.hibernate.exception.ConstraintViolationException;
import org.sabhrant.tech.springboot.web.app.openshift.exception.BookIdMismatchException;
import org.sabhrant.tech.springboot.web.app.openshift.exception.BookNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

	public RestExceptionHandler() {
		super();
	}

	@ExceptionHandler({ BookNotFoundException.class })
	protected ResponseEntity<Object> handleNotFound(Exception exception, WebRequest request) {
		return handleExceptionInternal(exception, "Book not found", new HttpHeaders(), HttpStatus.NOT_FOUND, request);
	}

	@ExceptionHandler({ BookIdMismatchException.class, ConstraintViolationException.class,
			DataIntegrityViolationException.class })
	protected ResponseEntity<Object> handleBadRequest(Exception exception, WebRequest request) {
		return handleExceptionInternal(exception, exception.getLocalizedMessage(), new HttpHeaders(),
				HttpStatus.BAD_REQUEST, request);
	}
}
