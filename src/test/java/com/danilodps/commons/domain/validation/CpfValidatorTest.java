package com.danilodps.commons.domain.validation;

import com.danilodps.commons.application.exceptions.InvalidCPFException;
import com.danilodps.commons.application.exceptions.UserCPFEmptyException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("CpfValidator Tests")
class CpfValidatorTest {

    private CpfValidator validator;

    @BeforeEach
    void setUp() {
        validator = new CpfValidator();
    }

    @Nested
    @DisplayName("Validation with invalid inputs")
    class InvalidInputs {

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {"   ", "\t", "\n"})
        @DisplayName("Should throw UserCPFEmptyException when CPF is null, empty or blank")
        void shouldThrowUserCPFEmptyExceptionWhenCpfIsNullOrEmptyOrBlank(String invalidCpf) {
            assertThatThrownBy(() -> validator.validate(invalidCpf))
                    .isInstanceOf(UserCPFEmptyException.class);
        }

        @ParameterizedTest
        @ValueSource(strings = {
                "123",
                "123456789012",
                "123.456.789-0",
                "123.456.789-",
                "123.456.78",
                "1234567890",
                "123456789012"
        })
        @DisplayName("Should throw InvalidCPFException when CPF has invalid length")
        void shouldThrowInvalidCPFExceptionWhenCpfHasInvalidLength(String invalidCpf) {
            assertThatThrownBy(() -> validator.validate(invalidCpf))
                    .isInstanceOf(InvalidCPFException.class);
        }

        @ParameterizedTest
        @ValueSource(strings = {
                "11111111111",
                "22222222222",
                "33333333333",
                "44444444444",
                "55555555555",
                "66666666666",
                "77777777777",
                "88888888888",
                "99999999999",
                "00000000000"
        })
        @DisplayName("Should throw InvalidCPFException when all digits are identical")
        void shouldThrowInvalidCPFExceptionWhenAllDigitsAreIdentical(String invalidCpf) {
            assertThatThrownBy(() -> validator.validate(invalidCpf))
                    .isInstanceOf(InvalidCPFException.class);
        }

        @ParameterizedTest
        @ValueSource(strings = {
                "123.456.789-99",
                "111.111.111-11",
                "000.000.000-00",
                "12345678900",
                "52998224726",
                "123.456.789-00"
        })
        @DisplayName("Should throw InvalidCPFException when verifier digits are invalid")
        void shouldThrowInvalidCPFExceptionWhenVerifierDigitsAreInvalid(String invalidCpf) {
            assertThatThrownBy(() -> validator.validate(invalidCpf))
                    .isInstanceOf(InvalidCPFException.class);
        }

        @Test
        @DisplayName("Should throw InvalidCPFException when CPF contains non-digit characters besides formatting")
        void shouldThrowInvalidCPFExceptionWhenCpfContainsLetters() {
            assertThatThrownBy(() -> validator.validate("123.456.789-0A"))
                    .isInstanceOf(InvalidCPFException.class);
        }
    }

    @Nested
    @DisplayName("Validation with valid inputs")
    class ValidInputs {

        @ParameterizedTest
        @ValueSource(strings = {
                // Valid CPFs (using test/example numbers)
                "529.982.247-25",
                "52998224725",
                " 529.982.247-25 ",
                "  52998224725  ",
                "\t529.982.247-25\t",
                "111.444.777-35",
                "11144477735",
                "123.456.789-09",
                "12345678909",
                "935.411.347-80",
                "93541134780",
                "123.456.789-09"
        })
        @DisplayName("Should not throw any exception for valid CPFs")
        void shouldNotThrowExceptionForValidCpf(String validCpf) {
            assertThatCode(() -> validator.validate(validCpf))
                    .doesNotThrowAnyException();
        }

        @ParameterizedTest
        @ValueSource(strings = {
                "529.982.247-25",
                "111.444.777-35",
                "123.456.789-09"
        })
        @DisplayName("Should accept CPF with different formatting patterns")
        void shouldAcceptCpfWithDifferentFormats(String validCpf) {
            assertThatCode(() -> validator.validate(validCpf))
                    .doesNotThrowAnyException();
        }
    }

    @Nested
    @DisplayName("Edge cases and boundaries")
    class EdgeCases {

        @Test
        @DisplayName("Should handle CPF with leading zeros correctly")
        void shouldHandleCpfWithLeadingZeros() {
            assertThatCode(() -> validator.validate("525.431.810-40"))
                    .doesNotThrowAnyException();
        }

        @ParameterizedTest
        @ValueSource(strings = {
                "000.000.001-91",
                "00000000191"
        })
        @DisplayName("Should validate CPF with zeros in the middle correctly")
        void shouldValidateCpfWithZerosInMiddle(String validCpf) {
            assertThatCode(() -> validator.validate(validCpf))
                    .doesNotThrowAnyException();
        }
    }
}