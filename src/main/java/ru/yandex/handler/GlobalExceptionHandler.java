package ru.yandex.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.yandex.exception.ExceptionType;
import ru.yandex.exception.MyException;
import ru.yandex.model.dto.ErrorResponse;

import java.util.List;

@RestControllerAdvice
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
    public ResponseEntity<List> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(List.of());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleOther(Exception ex) {
        ErrorResponse response = new ErrorResponse(
            ExceptionType.INTERNAL_ERROR.name(),
            ExceptionType.INTERNAL_ERROR.getMessage()
        );
        ex.printStackTrace();
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(response);
    }
}