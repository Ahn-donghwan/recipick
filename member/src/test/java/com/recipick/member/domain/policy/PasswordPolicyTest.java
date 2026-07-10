package com.recipick.member.domain.policy;

import com.recipick.member.domain.exception.InvalidPasswordException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PasswordPolicyTest {

    // 모든 비밀번호 정책을 만족하면 예외가 발생하지 않아야 한다.
    @Test
    void acceptsPasswordThatMeetsAllConditions() {
        assertDoesNotThrow(() -> PasswordPolicy.validate("PASSWORD1!"));
    }

    // 비밀번호는 최소 8자 이상이어야 한다.
    @Test
    void rejectsPasswordShorterThanEightCharacters() {
        assertThrows(InvalidPasswordException.class, () -> PasswordPolicy.validate("Pass1!"));
    }

    // 비밀번호는 최대 20자를 초과할 수 없다.
    @Test
    void rejectsPasswordLongerThanTwentyCharacters() {
        assertThrows(InvalidPasswordException.class, () -> PasswordPolicy.validate("Password123456789012!"));
    }

    // 한글은 영문으로 인정하지 않으며, 비밀번호에는 영문이 포함되어야 한다.
    @Test
    void rejectsPasswordWithoutEnglishLetter() {
        assertThrows(InvalidPasswordException.class, () -> PasswordPolicy.validate("비밀번호123!"));
    }

    // 영문이 포함되어 있어도 대문자가 하나 이상 없으면 거부한다.
    @Test
    void rejectsPasswordWithoutUppercaseEnglishLetter() {
        assertThrows(InvalidPasswordException.class, () -> PasswordPolicy.validate("password1!"));
    }

    // 비밀번호에는 숫자가 하나 이상 포함되어야 한다.
    @Test
    void rejectsPasswordWithoutNumber() {
        assertThrows(InvalidPasswordException.class, () -> PasswordPolicy.validate("Password!"));
    }

    // 비밀번호에는 ASCII 특수문자가 하나 이상 포함되어야 한다.
    @Test
    void rejectsPasswordWithoutSpecialCharacter() {
        assertThrows(InvalidPasswordException.class, () -> PasswordPolicy.validate("Password1"));
    }

    // 한글 같은 비 ASCII 문자를 특수문자로 잘못 인정해서는 안 된다.
    @Test
    void doesNotTreatNonAsciiCharacterAsSpecialCharacter() {
        assertThrows(InvalidPasswordException.class, () -> PasswordPolicy.validate("Password1한"));
    }

    // 비밀번호 중간을 포함해 어떤 위치에도 공백이 들어갈 수 없다.
    @Test
    void rejectsPasswordContainingWhitespace() {
        assertThrows(InvalidPasswordException.class, () -> PasswordPolicy.validate("Pass word1!"));
    }

    // 비밀번호는 필수 입력값이므로 null을 허용하지 않는다.
    @Test
    void rejectNullPassword() {
        assertThrows(InvalidPasswordException.class, () -> PasswordPolicy.validate(null));
    }
}
