package ru.yandex.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ExceptionType {

    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "Post %s not found"),
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "Comment %s not found"),

    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "Invalid request: %s"),
    VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "Validation failed: %s"),
    INVALID_PAGINATION(HttpStatus.BAD_REQUEST, "Invalid pagination parameters: %s %s"),
    COMMENT_POST_MISMATCH(HttpStatus.BAD_REQUEST, "Comment %s does not belong to post %s"),
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");

    private final HttpStatus status;
    private final String message;

    public String format(Object... args) {
        return String.format(message, args);
    }
}