package com.wallet.userservice.controller.advice;

import com.wallet.userservice.exception.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        List<String> errors = new ArrayList<String>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }
        for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
        }

        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getMessage(), errors);
        return handleExceptionInternal(ex, apiError, headers, apiError.getStatus(), request);
    }

    @ExceptionHandler(value = {DuplicateUserException.class})
    public ResponseEntity<ApiError> handleEmailRepeatingException(DuplicateUserException exception) {
        return ResponseEntity.status(CONFLICT)
                .body(new ApiError(CONFLICT, exception.getMessage(), Collections.emptyList()));
    }

    @ExceptionHandler(value = {EntityNotFoundException.class})
    public ResponseEntity<ApiError> handleEntityNotFoundException(EntityNotFoundException exception) {
        return ResponseEntity.status(NOT_FOUND)
                .body(new ApiError(NOT_FOUND, exception.getMessage(), Collections.emptyList()));
    }

    @ExceptionHandler(value = {IncorrectEmailException.class})
    public ResponseEntity<ApiError> handleIncorrectEmailException(IncorrectEmailException exception) {
        return ResponseEntity.status(NOT_FOUND)
                .body(new ApiError(NOT_FOUND, exception.getMessage(), Collections.emptyList()));
    }

    @ExceptionHandler(value = {IncorrectPasswordException.class})
    public ResponseEntity<ApiError> handleIncorrectPasswordException(IncorrectPasswordException exception) {
        return ResponseEntity.status(NOT_FOUND)
                .body(new ApiError(NOT_FOUND, exception.getMessage(), Collections.emptyList()));
    }

}
