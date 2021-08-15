package com.cwi.cooperative.voting.exceptions;

import java.util.Date;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
	@ExceptionHandler(value = { Exception.class })
	public ResponseEntity<Object> handleAnyException(Exception ex, WebRequest req) {
		String errorMessage = ex.getLocalizedMessage();
		if (errorMessage == null) {
			errorMessage = ex.toString();
		}
		CustomError message = new CustomError(new Date(), errorMessage);
		return new ResponseEntity<Object>(message, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
	}	
	@ExceptionHandler(value = { ChallengeException.class })
    public ResponseEntity<Object> handleAnyChallengeException(ChallengeException ex, WebRequest req) {
		String errorMessage = ex.getLocalizedMessage();
		if (errorMessage == null) {
			errorMessage = ex.toString();
		}
		CustomError message = new CustomError(new Date(), errorMessage);
		return new ResponseEntity<Object>(message, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
	}
}