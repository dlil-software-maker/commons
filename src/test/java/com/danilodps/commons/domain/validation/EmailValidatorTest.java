package com.danilodps.commons.domain.validation;

import com.danilodps.commons.application.exceptions.EmailEmptyException;
import com.danilodps.commons.application.exceptions.InvalidEmailException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("EmailValidator Tests")
class EmailValidatorTest {

    private EmailValidator validator;

    @BeforeEach
    void setUp() {
        validator = new EmailValidator();
    }

    @Nested
    @DisplayName("Validation with invalid inputs")
    class InvalidInputs {

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {"   ", "\t", "\n"})
        @DisplayName("Should throw EmailEmptyException when email is null, empty or blank")
        void shouldThrowEmailEmptyExceptionWhenEmailIsNullOrEmptyOrBlank(String invalidEmail) {
            assertThatThrownBy(() -> validator.validate(invalidEmail))
                    .isInstanceOf(EmailEmptyException.class);
        }

        @Nested
        @DisplayName("Structural validation failures")
        class StructuralFailures {

            @ParameterizedTest
            @ValueSource(strings = {
                    "email@.domain.com",
                    "user@.com",
                    "test@.domain",
                    "name@.co.uk"
            })
            @DisplayName("Should throw InvalidEmailException when email contains '@.' pattern")
            void shouldThrowInvalidEmailExceptionWhenEmailContainsAtDot(String invalidEmail) {
                assertThatThrownBy(() -> validator.validate(invalidEmail))
                        .isInstanceOf(InvalidEmailException.class);
            }

            @ParameterizedTest
            @ValueSource(strings = {
                    "user@domain.",
                    "name@domain.co.",
                    "test@domain.com.",
                    "email@domain."
            })
            @DisplayName("Should throw InvalidEmailException when email ends with dot")
            void shouldThrowInvalidEmailExceptionWhenEmailEndsWithDot(String invalidEmail) {
                assertThatThrownBy(() -> validator.validate(invalidEmail))
                        .isInstanceOf(InvalidEmailException.class);
            }

            @ParameterizedTest
            @ValueSource(strings = {
                    ".user@domain.com",
                    ".name@domain.co",
                    ".test@domain.com",
                    ".email@domain.com"
            })
            @DisplayName("Should throw InvalidEmailException when email starts with dot")
            void shouldThrowInvalidEmailExceptionWhenEmailStartsWithDot(String invalidEmail) {
                assertThatThrownBy(() -> validator.validate(invalidEmail))
                        .isInstanceOf(InvalidEmailException.class);
            }

            @ParameterizedTest
            @ValueSource(strings = {
                    "user@",
                    "name@domain",
                    "@domain.com",
                    "user@.com",
                    "@",
                    "user@domain@com"
            })
            @DisplayName("Should throw InvalidEmailException when email has invalid @ position")
            void shouldThrowInvalidEmailExceptionWhenEmailHasInvalidAtPosition(String invalidEmail) {
                assertThatThrownBy(() -> validator.validate(invalidEmail))
                        .isInstanceOf(InvalidEmailException.class);
            }
        }

        @Nested
        @DisplayName("Pattern matching failures")
        class PatternMatchingFailures {

            @ParameterizedTest
            @ValueSource(strings = {
                    "user@domain,com",
                    "user@domain;com",
                    "user@domain:com",
                    "user@domain|com",
                    "user@domain\\com"
            })
            @DisplayName("Should throw InvalidEmailException when email contains invalid special characters")
            void shouldThrowInvalidEmailExceptionWhenEmailContainsInvalidSpecialChars(String invalidEmail) {
                assertThatThrownBy(() -> validator.validate(invalidEmail))
                        .isInstanceOf(InvalidEmailException.class);
            }
        }
    }

    @Nested
    @DisplayName("Validation with valid inputs")
    class ValidInputs {

        @ParameterizedTest
        @ValueSource(strings = {
                "user@example.com",
                "user.name@example.com",
                "user-name@example.com",
                "user_name@example.com",
                "user+name@example.com",
                "user@example.co.uk",
                "user@example.com.br",
                "user@subdomain.example.com",
                "user123@example.com",
                "123user@example.com",
                "user@example.info",
                "user@example.io",
                "user@example.xyz",
                "user@example.travel",
                "user@example.museum",
                "user.name+tag@example.com",
                "user-name+filter@example.co.uk",
                "user_name@sub.domain.example.com",
                "a@b.com",
                "user@domain.abc",
                "user@domain.ab",
                "user@domain.abcdef",
                "user.name@domain.com"
        })
        @DisplayName("Should not throw any exception for valid emails")
        void shouldNotThrowExceptionForValidEmail(String validEmail) {
            assertThatCode(() -> validator.validate(validEmail))
                    .doesNotThrowAnyException();
        }

        @ParameterizedTest
        @ValueSource(strings = {
                "user@example.com",
                "user.name@example.co.uk",
                "user+tag@subdomain.example.com"
        })
        @DisplayName("Should accept emails with various valid special characters in local part")
        void shouldAcceptEmailsWithValidSpecialCharacters(String validEmail) {
            assertThatCode(() -> validator.validate(validEmail))
                    .doesNotThrowAnyException();
        }

        @ParameterizedTest
        @ValueSource(strings = {
                "user@example.com",
                " user@example.com ",
                "  user@example.com  ",
                "\tuser@example.com\t",
                "user@example.com\n"
        })
        @DisplayName("Should accept emails with leading/trailing whitespace")
        void shouldAcceptEmailsWithWhitespace(String validEmail) {
            assertThatCode(() -> validator.validate(validEmail))
                    .doesNotThrowAnyException();
        }
    }

    @Nested
    @DisplayName("Edge cases and special scenarios")
    class EdgeCases {

        @Test
        @DisplayName("Should validate email with numeric domain")
        void shouldValidateEmailWithNumericDomain() {
            assertThatCode(() -> validator.validate("user@123.com"))
                    .doesNotThrowAnyException();
        }

        @ParameterizedTest
        @ValueSource(strings = {
                "!#$%&'*+/=?`{|}~^@example.com",
                "user+tag@example.com",
                "user_name@example.com",
                "user-name@example.com"
        })
        @DisplayName("Should accept emails with all allowed special characters")
        void shouldAcceptEmailsWithAllowedSpecialChars(String validEmail) {
            assertThatCode(() -> validator.validate(validEmail))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should validate email with very long local part")
        void shouldValidateEmailWithLongLocalPart() {
            String localPart = "a".repeat(64);
            String validEmail = localPart + "@example.com";
            assertThatCode(() -> validator.validate(validEmail))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Should validate email with very long domain")
        void shouldValidateEmailWithLongDomain() {
            String domain = "subdomain." + "a".repeat(50) + ".com";
            String validEmail = "user@" + domain;
            assertThatCode(() -> validator.validate(validEmail))
                    .doesNotThrowAnyException();
        }
    }
}