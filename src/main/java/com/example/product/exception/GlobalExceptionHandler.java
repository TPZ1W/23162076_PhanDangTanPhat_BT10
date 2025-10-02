package com.example.product.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        log.warn("Validation error occurred: {}", ex.getMessage());

        ValidationErrorResponse errorResponse = new ValidationErrorResponse();
        errorResponse.setMessage("Dữ liệu đầu vào không hợp lệ");
        errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        errorResponse.setError("Validation Failed");

        List<ValidationErrorResponse.FieldError> fieldErrors = new ArrayList<>();

        ex.getBindingResult().getFieldErrors().forEach(error -> {
            ValidationErrorResponse.FieldError fieldError = new ValidationErrorResponse.FieldError(
                    error.getField(),
                    error.getRejectedValue(),
                    error.getDefaultMessage());
            fieldErrors.add(fieldError);
        });

        errorResponse.setFieldErrors(fieldErrors);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ValidationErrorResponse> handleConstraintViolationException(ConstraintViolationException ex) {
        log.warn("Constraint violation occurred: {}", ex.getMessage());

        ValidationErrorResponse errorResponse = new ValidationErrorResponse();
        errorResponse.setMessage("Dữ liệu không hợp lệ");
        errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        errorResponse.setError("Constraint Violation");

        List<ValidationErrorResponse.FieldError> fieldErrors = new ArrayList<>();

        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            String propertyPath = violation.getPropertyPath().toString();
            String field = propertyPath.substring(propertyPath.lastIndexOf('.') + 1);

            ValidationErrorResponse.FieldError fieldError = new ValidationErrorResponse.FieldError(
                    field,
                    violation.getInvalidValue(),
                    violation.getMessage());
            fieldErrors.add(fieldError);
        }

        errorResponse.setFieldErrors(fieldErrors);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ValidationErrorResponse> handleGeneralException(Exception ex) {
        log.error("Unexpected error occurred: ", ex);

        ValidationErrorResponse errorResponse = new ValidationErrorResponse();
        errorResponse.setMessage("Đã xảy ra lỗi không mong muốn");
        errorResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        errorResponse.setError("Internal Server Error");
        errorResponse.setFieldErrors(new ArrayList<>());

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}