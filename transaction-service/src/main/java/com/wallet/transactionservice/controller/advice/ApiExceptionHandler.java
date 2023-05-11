package com.wallet.transactionservice.controller.advice;

import com.wallet.transactionservice.exception.AccessDeniedException;
import com.wallet.transactionservice.exception.ApiError;
import com.wallet.transactionservice.exception.MoneyIsNotEnoughException;
import com.wallet.userservice.exception.DuplicateUserException;
import com.wallet.userservice.exception.EntityNotFoundException;
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

import javax.validation.ConstraintViolationException;
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


    @ExceptionHandler(value = {AccessDeniedException.class})
    public ResponseEntity<ApiError> accessDeniedException(AccessDeniedException exception) {
        return ResponseEntity.status(NOT_FOUND)
                .body(new ApiError(NOT_FOUND, exception.getLocalizedMessage(), Collections.emptyList()));
    }
    @ExceptionHandler(value = {MoneyIsNotEnoughException.class})
    public ResponseEntity<ApiError> moneyIsNotEnoughException(MoneyIsNotEnoughException exception) {
        return ResponseEntity.status(NOT_FOUND)
                .body(new ApiError(NOT_FOUND, exception.getLocalizedMessage(), Collections.emptyList()));
    }
}
