package com.danilodps.commons.domain.validation;

import com.danilodps.commons.application.exceptions.InvalidCNPJException;
import com.danilodps.commons.application.exceptions.StoreCNPJEmptyException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("CnpjValidator Tests")
class CnpjValidatorTest {

    private CnpjValidator validator;

    @BeforeEach
    void setUp() {
        validator = new CnpjValidator();
    }

    @Nested
    @DisplayName("Validation with invalid inputs")
    class InvalidInputs {

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {"   ", "\t", "\n"})
        @DisplayName("Should throw StoreCNPJEmptyException when CNPJ is null, empty or blank")
        void shouldThrowStoreCNPJEmptyExceptionWhenCnpjIsNullOrEmptyOrBlank(String invalidCnpj) {
            assertThatThrownBy(() -> validator.validate(invalidCnpj))
                    .isInstanceOf(StoreCNPJEmptyException.class);
        }

        @ParameterizedTest
        @ValueSource(strings = {
                "123",
                "123456789012345",
                "12.345.678/9012-3",
                "12.345.678/9012-",
                "12.345.678/901"
        })
        @DisplayName("Should throw InvalidCNPJException when CNPJ has invalid length")
        void shouldThrowInvalidCNPJExceptionWhenCnpjHasInvalidLength(String invalidCnpj) {
            assertThatThrownBy(() -> validator.validate(invalidCnpj))
                    .isInstanceOf(InvalidCNPJException.class);
        }

        @ParameterizedTest
        @ValueSource(strings = {
                "11111111111111",
                "22222222222222",
                "33333333333333",
                "44444444444444",
                "55555555555555",
                "66666666666666",
                "77777777777777",
                "88888888888888",
                "99999999999999",
                "00000000000000"
        })
        @DisplayName("Should throw InvalidCNPJException when all digits are identical")
        void shouldThrowInvalidCNPJExceptionWhenAllDigitsAreIdentical(String invalidCnpj) {
            assertThatThrownBy(() -> validator.validate(invalidCnpj))
                    .isInstanceOf(InvalidCNPJException.class);
        }

        @ParameterizedTest
        @ValueSource(strings = {
                "12.345.678/9012-99",
                "11.111.111/1111-11",
                "00.000.000/0000-00",
                "12345678901234",
                "11222333000199",
                "12.345.678/0001-99"
        })
        @DisplayName("Should throw InvalidCNPJException when verifier digits are invalid")
        void shouldThrowInvalidCNPJExceptionWhenVerifierDigitsAreInvalid(String invalidCnpj) {
            assertThatThrownBy(() -> validator.validate(invalidCnpj))
                    .isInstanceOf(InvalidCNPJException.class);
        }
    }

    @Nested
    @DisplayName("Validation with valid inputs")
    class ValidInputs {

        @ParameterizedTest
        @ValueSource(strings = {
                // Real CNPJs (using test/example numbers)
                "11.222.333/0001-81",
                "11.222.333/0001-81",
                "11222333000181",
                " 11.222.333/0001-81 ",
                "  11222333000181  ",
                "\t11.222.333/0001-81\t",
                "12.345.678/0001-95",
                "12345678000195",
                "00.000.000/0001-91",
                "00000000000191",
        })
        @DisplayName("Should not throw any exception for valid CNPJs")
        void shouldNotThrowExceptionForValidCnpj(String validCnpj) {
            assertThatCode(() -> validator.validate(validCnpj))
                    .doesNotThrowAnyException();
        }
    }
}