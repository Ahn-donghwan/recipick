package com.recipick.member.domain.exception;

public class InvalidPasswordException extends IllegalArgumentException {

    public InvalidPasswordException(String message) {
        super(message);
    }
}
