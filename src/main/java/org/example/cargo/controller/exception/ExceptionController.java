package org.example.cargo.controller.exception;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class ExceptionController extends ResponseEntityExceptionHandler {


    @ExceptionHandler(value = {GeneralRuntimeException.class})
    public ResponseEntity<ErrorDTO> handleGeneralRuntimeException(GeneralRuntimeException e) {
        return new ResponseEntity<>(
                new ErrorDTO(
                        LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME),
                        e.getMessage(),
                        e.getHttpStatus().name()
                ), e.getHttpStatus()
        );
    }
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();

        // Add general information about the error
        body.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Validation Error");
        body.put("message", "One or more fields have validation errors");

        // Collect field-specific error details
        List<FieldErrorDTO> fieldErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(fieldError -> new FieldErrorDTO(
                        fieldError.getField(),                  // Which field has an error
                        fieldError.getDefaultMessage(),         // Validation message for that field
                        fieldError.getRejectedValue(),          // The rejected value (what was sent)
                        fieldError.getCode()                    // Error code (e.g., "NotNull", "Size")
                ))
                .collect(Collectors.toList());

        body.put("fieldErrors", fieldErrors);

        return new ResponseEntity<>(body, headers, status);
    }
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(ValidationException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An unexpected error occurred: " + ex.getMessage());
    }


    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<String> handleConstraintViolation(ConstraintViolationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid request parameters: " + ex.getMessage());
    }
    @ExceptionHandler(ClassCastException.class)
    public ResponseEntity<Map<String, String>> handleClassCastException(ClassCastException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "Type casting issue");
        errorResponse.put("message", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Malformed JSON or Type Mismatch");

        String specificError = ex.getMessage();
        Throwable mostSpecificCause = ex.getMostSpecificCause();
        if (mostSpecificCause != null) {
            specificError = mostSpecificCause.getMessage();
        }

        body.put("message", specificError);

        return new ResponseEntity<>(body, headers, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, String>> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "Type mismatch for parameter");

        errorResponse.put("field", ex.getName());
        if (ex.getRequiredType() != null) {
            errorResponse.put("expectedType", ex.getRequiredType().getSimpleName());
        }
        errorResponse.put("rejectedValue", ex.getValue() != null ? ex.getValue().toString() : "null");
        errorResponse.put("message", ex.getMessage()); 

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, String>> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "Data Integrity Violation");

        // Customize this message based on the most likely causes
        if (ex.getMostSpecificCause() != null && ex.getMostSpecificCause().getMessage() != null &&
            ex.getMostSpecificCause().getMessage().contains("base_user_email_key")) {
            errorResponse.put("message", "A user with this email already exists.");
        } else {
            errorResponse.put("message", "Database constraint violation: " + ex.getMostSpecificCause().getMessage());
        }

        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT); // 409 Conflict
    }


    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FieldErrorDTO implements Serializable {
        private String field;          // The name of the field with the error
        private String message;        // The validation error message
        private Object rejectedValue;  // The value that was rejected (if available)
        private String errorCode;      // The type of validation error (e.g., "NotNull", "Size")
    }

    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ErrorDTO implements Serializable {
        private String date;
        private String message;
        private String status;
    }
}
