package ru.yandex.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.yandex.exception.ExceptionType;
import ru.yandex.exception.MyException;
import ru.yandex.model.dto.ErrorResponse;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MyException.class)
    public ResponseEntity<ErrorResponse> handleMyException(MyException ex) {
        ExceptionType type = ex.getType();

        ErrorResponse response = new ErrorResponse(
            type.name(),
            ex.getMessage()
        );

        return ResponseEntity
            .status(type.getStatus())
            .body(response);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {

        ErrorResponse response = new ErrorResponse(
            ExceptionType.INVALID_REQUEST.name(),
            String.format("Invalid value '%s' for parameter '%s'", ex.getValue(), ex.getName())
        );

        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleOther(Exception ex) {
        ErrorResponse response = new ErrorResponse(
            ExceptionType.INTERNAL_ERROR.name(),
            "Internal server error"
        );
        log.error("Unhandled exception", ex);
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(response);
    }
}