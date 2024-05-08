package com.silchenko.arsen.testexampleforclearsolutions.exception;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestControllerAdvice(basePackages = "com.silchenko.arsen.testexampleforclearsolutions.controller")
public class CustomGlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<Object> invalidArgument(MethodArgumentNotValidException ex) {
        return buildResponseEntity(new UserApiErrorResponse(BAD_REQUEST,
                LocalDateTime.now(), getErrorsMessage(ex.getBindingResult())));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleUserNotFoundException(ResourceNotFoundException ex) {
        return buildResponseEntity(new UserApiErrorResponse(NOT_FOUND,
                LocalDateTime.now(), ex.getMessage()));
    }

    @ExceptionHandler(InvalidArgumentException.class)
    public ResponseEntity<Object> handleInsufficientAgeException(InvalidArgumentException ex) {
        return buildResponseEntity(new UserApiErrorResponse(BAD_REQUEST,
                LocalDateTime.now(), ex.getMessage()));
    }

    private ResponseEntity<Object> buildResponseEntity(UserApiErrorResponse userApiError) {
        return new ResponseEntity<>(userApiError, userApiError.status());
    }

    private Map<String, String> getErrorsMessage(BindingResult bindingResult) {
        return bindingResult.getAllErrors().stream()
                .filter(e -> e instanceof FieldError)
                .collect(Collectors.groupingBy(e -> ((FieldError)e).getField(),
                        Collectors.mapping(DefaultMessageSourceResolvable::getDefaultMessage,
                                Collectors.joining())));
    }
}
