package com.seyan.activity.handler;

import com.seyan.activity.exception.ActivityDeleteException;
import com.seyan.activity.exception.ActivityNotFoundException;
import com.seyan.activity.responsewrapper.ValidationErrorWrapper;
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

    @ExceptionHandler(ActivityNotFoundException.class)
    public ResponseEntity<ErrorObject> handleActivityNotFoundException(ActivityNotFoundException ex) {
        ErrorObject errorObject = ErrorObject.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .message(ex.getMessage())
                .date(getDateTime())
                .build();
        return new ResponseEntity<>(errorObject, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ActivityDeleteException.class)
    public ResponseEntity<ErrorObject> handleActivityDeleteException(ActivityDeleteException ex) {
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
