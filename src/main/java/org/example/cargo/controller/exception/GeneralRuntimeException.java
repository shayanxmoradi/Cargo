package org.example.cargo.controller.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public class GeneralRuntimeException extends RuntimeException {
    private final String message;
    private final HttpStatus httpStatus;
}
