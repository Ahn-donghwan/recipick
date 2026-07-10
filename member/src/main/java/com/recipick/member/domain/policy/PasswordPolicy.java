package com.recipick.member.domain.policy;

import com.recipick.member.domain.exception.InvalidPasswordException;

public final class PasswordPolicy {

    private static final int MIN_LENGTH = 8;
    private static final int MAX_LENGTH = 20;

    private PasswordPolicy() {
    }

    public static void validate(String password) {
        if (password == null || password.length() < MIN_LENGTH || password.length() > MAX_LENGTH) {
            throw new InvalidPasswordException("비밀번호는 8자 이상 20자 이하여야 합니다.");
        }
        if (password.chars().anyMatch(Character::isWhitespace)) {
            throw new InvalidPasswordException("비밀번호에는 공백을 포함할 수 없습니다.");
        }
        if (password.chars().noneMatch(PasswordPolicy::isEnglishLetter)) {
            throw new InvalidPasswordException("비밀번호에는 영문을 포함해야 합니다.");
        }
        if (password.chars().noneMatch(PasswordPolicy::isUppercaseEnglishLetter)) {
            throw new InvalidPasswordException("비밀번호에는 영문 대문자를 포함해야 합니다.");
        }
        if (password.chars().noneMatch(PasswordPolicy::isDigit)) {
            throw new InvalidPasswordException("비밀번호에는 숫자를 포함해야 합니다.");
        }
        if (password.chars().noneMatch(PasswordPolicy::isSpecialCharacter)) {
            throw new InvalidPasswordException("비밀번호에는 특수문자를 포함해야 합니다.");
        }
    }

    private static boolean isEnglishLetter(int character) {
        return character >= 'A' && character <= 'Z' || character >= 'a' && character <= 'z';
    }

    private static boolean isUppercaseEnglishLetter(int character) {
        return character >= 'A' && character <= 'Z';
    }

    private static boolean isDigit(int character) {
        return character >= '0' && character <= '9';
    }

    private static boolean isSpecialCharacter(int character) {
        return character >= '!' && character <= '/'
                || character >= ':' && character <= '@'
                || character >= '[' && character <= '`'
                || character >= '{' && character <= '~';
    }
}
