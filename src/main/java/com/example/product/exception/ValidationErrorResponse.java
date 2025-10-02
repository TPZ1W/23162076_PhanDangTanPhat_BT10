package com.example.product.exception;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ValidationErrorResponse {
    private String message;
    private LocalDateTime timestamp;
    private int status;
    private String error;
    private List<FieldError> fieldErrors;

    public ValidationErrorResponse() {
        this.timestamp = LocalDateTime.now();
    }

    @Data
    public static class FieldError {
        private String field;
        private Object rejectedValue;
        private String message;

        public FieldError(String field, Object rejectedValue, String message) {
            this.field = field;
            this.rejectedValue = rejectedValue;
            this.message = message;
        }
    }
}