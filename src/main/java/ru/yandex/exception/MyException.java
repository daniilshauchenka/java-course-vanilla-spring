package ru.yandex.exception;

import lombok.Getter;

@Getter
public class MyException extends RuntimeException {

    private final ExceptionType type;

    public MyException(ExceptionType type) {
        super(type.getMessage());
        this.type = type;
    }

    public MyException(ExceptionType type, Object... args) {
        super(type.format(args));
        this.type = type;
    }
}