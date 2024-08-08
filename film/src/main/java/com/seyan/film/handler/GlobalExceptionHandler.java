package com.seyan.film.handler;

import com.seyan.film.exception.FilmNotFoundException;
import com.seyan.film.exception.IncorrectDateRangeException;
import com.seyan.film.exception.ProfileNotFoundException;
import com.seyan.film.exception.SortingParametersException;
import com.seyan.film.responsewrapper.ValidationErrorWrapper;
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

    @ExceptionHandler(FilmNotFoundException.class)
    public ResponseEntity<ErrorObject> handleFilmNotFoundException(FilmNotFoundException ex) {
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
                .status(HttpStatus.BAD_REQUEST.value())
                .message(ex.getMessage())
                .date(getDateTime())
                .build();
        return new ResponseEntity<>(errorObject, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ProfileNotFoundException.class)
    public ResponseEntity<ErrorObject> handleProfileNotFoundException(ProfileNotFoundException ex) {
        ErrorObject errorObject = ErrorObject.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .message(ex.getMessage())
                .date(getDateTime())
                .build();
        return new ResponseEntity<>(errorObject, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(SortingParametersException.class)
    public ResponseEntity<ErrorObject> handleSortingParametersException(SortingParametersException ex) {
        ErrorObject errorObject = ErrorObject.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(ex.getMessage())
                .date(getDateTime())
                .build();
        return new ResponseEntity<>(errorObject, HttpStatus.BAD_REQUEST);
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
