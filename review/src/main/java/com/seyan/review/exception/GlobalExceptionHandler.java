package com.seyan.review.exception;

import com.seyan.reviewmonolith.exception.film.IncorrectDateRangeException;
import com.seyan.reviewmonolith.exception.user.EmailAlreadyExistsException;
import com.seyan.reviewmonolith.exception.user.UserNotFoundException;
import com.seyan.reviewmonolith.responseWrapper.ValidationErrorWrapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.rmi.UnexpectedException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private String getDateTime() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorObject> handleUserNotFoundException(UserNotFoundException ex) {
        ErrorObject errorObject = ErrorObject.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .message(ex.getMessage())
                .date(getDateTime())
                .build();
        return new ResponseEntity<>(errorObject, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IncorrectDateRangeException.class)
    public ResponseEntity<ErrorObject> handleIncorrectDateRangeException(IncorrectDateRangeException ex) {
        ErrorObject errorObject = ErrorObject.builder()
                .status(HttpStatus.UNPROCESSABLE_ENTITY.value())
                .message(ex.getMessage())
                .date(getDateTime())
                .build();
        return new ResponseEntity<>(errorObject, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ErrorObject> handleEmailAlreadyExistsException(EmailAlreadyExistsException ex) {
        ErrorObject errorObject = ErrorObject.builder()
                .status(HttpStatus.UNPROCESSABLE_ENTITY.value())
                .message(ex.getMessage())
                .date(getDateTime())
                .build();
        return new ResponseEntity<>(errorObject, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(UnexpectedException.class)
    public ResponseEntity<ErrorObject> handleUnexpectedException(UnexpectedException ex) {
        ErrorObject errorObject = ErrorObject.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(ex.getMessage())
                .date(getDateTime())
                .build();
        return new ResponseEntity<>(errorObject, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorWrapper> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<String> exceptionMessages = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .toList();

        ValidationErrorWrapper wrapper = ValidationErrorWrapper.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message("Some fields are invalid")
                .errors(exceptionMessages)
                .build();

        return new ResponseEntity<>(wrapper, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }
}
