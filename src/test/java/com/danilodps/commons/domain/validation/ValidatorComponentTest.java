package com.danilodps.commons.domain.validation;

import com.danilodps.commons.application.exceptions.*;
import com.danilodps.commons.domain.model.enums.DocumentTypeEnum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ValidatorComponent Tests")
class ValidatorComponentTest {

    @Mock
    private CpfValidator cpfValidator;

    @Mock
    private CnpjValidator cnpjValidator;

    @Mock
    private EmailValidator emailValidator;

    @InjectMocks
    private ValidatorComponent validatorComponent;

    private static final String VALID_EMAIL = "user@example.com";
    private static final String VALID_CPF = "52998224725";
    private static final String VALID_CNPJ = "11222333000181";

    @Nested
    @DisplayName("Validate method tests")
    class ValidateMethodTests {

        @Test
        @DisplayName("Should validate email and CPF when document identifier is CPF")
        void shouldValidateEmailAndCpfWhenDocumentIdentifierIsCpf() {
            String documentIdentifier = DocumentTypeEnum.CPF.getShortName();

            validatorComponent.validate(VALID_EMAIL, documentIdentifier, VALID_CPF);

            verify(emailValidator).validate(VALID_EMAIL);
            verify(cpfValidator).validate(VALID_CPF);
            verify(cnpjValidator, never()).validate(anyString());
        }

        @Test
        @DisplayName("Should validate email and CNPJ when document identifier is CNPJ")
        void shouldValidateEmailAndCnpjWhenDocumentIdentifierIsCnpj() {
            String documentIdentifier = DocumentTypeEnum.CNPJ.getShortName();

            validatorComponent.validate(VALID_EMAIL, documentIdentifier, VALID_CNPJ);

            verify(emailValidator).validate(VALID_EMAIL);
            verify(cnpjValidator).validate(VALID_CNPJ);
            verify(cpfValidator, never()).validate(anyString());
        }

        @ParameterizedTest
        @ValueSource(strings = {"", "INVALID", "cpf", "Cpf", "CPF ", " CNPJ", "DOCUMENT"})
        @DisplayName("Should default to CNPJ validation when document identifier is not exactly CPF")
        void shouldDefaultToCnpjWhenDocumentIdentifierIsNotExactCpf(String invalidIdentifier) {
            validatorComponent.validate(VALID_EMAIL, invalidIdentifier, VALID_CNPJ);

            verify(emailValidator).validate(VALID_EMAIL);
            verify(cnpjValidator).validate(VALID_CNPJ);
            verify(cpfValidator, never()).validate(anyString());
        }

        @Test
        @DisplayName("Should propagate email empty exception when email is empty")
        void shouldPropagateEmailEmptyException() {
            String emptyEmail = "";
            doThrow(new EmailEmptyException())
                    .when(emailValidator).validate(emptyEmail);

            assertThatThrownBy(() -> validatorComponent.validate(emptyEmail, DocumentTypeEnum.CPF.getShortName(), VALID_CPF))
                    .isInstanceOf(EmailEmptyException.class);

            verify(emailValidator).validate(emptyEmail);
            verify(cpfValidator).validate(VALID_CPF);
            verify(cnpjValidator, never()).validate(anyString());
        }

        @Test
        @DisplayName("Should propagate CPF validation exception when CPF is invalid")
        void shouldPropagateCpfValidationException() {
            String invalidCpf = "12345678900";
            doThrow(new InvalidCPFException(invalidCpf))
                    .when(cpfValidator).validate(invalidCpf);

            assertThatThrownBy(() -> validatorComponent.validate(VALID_EMAIL, DocumentTypeEnum.CPF.getShortName(), invalidCpf))
                    .isInstanceOf(InvalidCPFException.class);

            verify(emailValidator, never()).validate(anyString());
            verify(cpfValidator).validate(invalidCpf);
            verify(cnpjValidator, never()).validate(anyString());
        }

        @Test
        @DisplayName("Should propagate CNPJ validation exception when CNPJ is invalid")
        void shouldPropagateCnpjValidationException() {
            String invalidCnpj = "11111111111111";
            doThrow(new InvalidCNPJException(invalidCnpj))
                    .when(cnpjValidator).validate(invalidCnpj);

            assertThatThrownBy(() -> validatorComponent.validate(VALID_EMAIL, DocumentTypeEnum.CNPJ.getShortName(), invalidCnpj))
                    .isInstanceOf(InvalidCNPJException.class);

            verify(emailValidator, never()).validate(anyString());
            verify(cnpjValidator).validate(invalidCnpj);
            verify(cpfValidator, never()).validate(anyString());
        }

        @Test
        @DisplayName("Should propagate CPF empty exception when CPF is empty and identifier is CPF")
        void shouldPropagateCpfEmptyExceptionWhenCpfIsEmpty() {
            String emptyCpf = "";
            doThrow(new UserCPFEmptyException())
                    .when(cpfValidator).validate(emptyCpf);

            assertThatThrownBy(() -> validatorComponent.validate(VALID_EMAIL, DocumentTypeEnum.CPF.getShortName(), emptyCpf))
                    .isInstanceOf(UserCPFEmptyException.class);

            verify(emailValidator, never()).validate(anyString());
            verify(cpfValidator).validate(emptyCpf);
            verify(cnpjValidator, never()).validate(anyString());
        }

        @Test
        @DisplayName("Should propagate CNPJ empty exception when CNPJ is empty and identifier is CNPJ")
        void shouldPropagateCnpjEmptyExceptionWhenCnpjIsEmpty() {
            String emptyCnpj = "";
            doThrow(new StoreCNPJEmptyException())
                    .when(cnpjValidator).validate(emptyCnpj);

            assertThatThrownBy(() -> validatorComponent.validate(VALID_EMAIL, DocumentTypeEnum.CNPJ.getShortName(), emptyCnpj))
                    .isInstanceOf(StoreCNPJEmptyException.class);

            verify(emailValidator, never()).validate(anyString());
            verify(cnpjValidator).validate(emptyCnpj);
            verify(cpfValidator, never()).validate(anyString());
        }

        @ParameterizedTest
        @CsvSource({
                "CPF, 52998224725",
                "CNPJ, 11222333000181"
        })
        @DisplayName("Should call correct validator based on document identifier")
        void shouldCallCorrectValidatorBasedOnDocumentIdentifier(String identifier, String document) {
            validatorComponent.validate(VALID_EMAIL, identifier, document);

            verify(emailValidator).validate(VALID_EMAIL);

            if (DocumentTypeEnum.CPF.getShortName().equals(identifier)) {
                verify(cpfValidator).validate(document);
                verify(cnpjValidator, never()).validate(anyString());
            } else {
                verify(cnpjValidator).validate(document);
                verify(cpfValidator, never()).validate(anyString());
            }
        }
    }

    @Nested
    @DisplayName("WhichDocument method tests")
    class WhichDocumentMethodTests {

        @Test
        @DisplayName("Should validate CPF when document identifier is CPF")
        void shouldValidateCpfWhenDocumentIdentifierIsCpf() {
            validatorComponent.whichDocument(DocumentTypeEnum.CPF.getShortName(), VALID_CPF);

            verify(cpfValidator).validate(VALID_CPF);
            verify(cnpjValidator, never()).validate(anyString());
        }

        @Test
        @DisplayName("Should validate CNPJ when document identifier is CNPJ")
        void shouldValidateCnpjWhenDocumentIdentifierIsCnpj() {
            validatorComponent.whichDocument(DocumentTypeEnum.CNPJ.getShortName(), VALID_CNPJ);

            verify(cnpjValidator).validate(VALID_CNPJ);
            verify(cpfValidator, never()).validate(anyString());
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {"INVALID", "cpf", "Cpf", "CPF ", " CNPJ", "DOCUMENT", "123", "null"})
        @DisplayName("Should default to CNPJ validation when document identifier is not exactly CPF")
        void shouldDefaultToCnpjWhenDocumentIdentifierIsNotExactCpf(String invalidIdentifier) {
            validatorComponent.whichDocument(invalidIdentifier, VALID_CNPJ);

            verify(cnpjValidator).validate(VALID_CNPJ);
            verify(cpfValidator, never()).validate(anyString());
        }

        @Test
        @DisplayName("Should propagate CPF validation exception when CPF is invalid")
        void shouldPropagateCpfValidationException() {
            String invalidCpf = "12345678900";
            doThrow(new InvalidCPFException(invalidCpf))
                    .when(cpfValidator).validate(invalidCpf);

            assertThatThrownBy(() -> validatorComponent.whichDocument(DocumentTypeEnum.CPF.getShortName(), invalidCpf))
                    .isInstanceOf(InvalidCPFException.class);

            verify(cpfValidator).validate(invalidCpf);
            verify(cnpjValidator, never()).validate(anyString());
        }

        @Test
        @DisplayName("Should propagate CNPJ validation exception when CNPJ is invalid")
        void shouldPropagateCnpjValidationException() {
            String invalidCnpj = "11111111111111";
            doThrow(new InvalidCNPJException(invalidCnpj))
                    .when(cnpjValidator).validate(invalidCnpj);

            assertThatThrownBy(() -> validatorComponent.whichDocument(DocumentTypeEnum.CNPJ.getShortName(), invalidCnpj))
                    .isInstanceOf(InvalidCNPJException.class);

            verify(cnpjValidator).validate(invalidCnpj);
            verify(cpfValidator, never()).validate(anyString());
        }

        @Test
        @DisplayName("Should propagate CPF empty exception when CPF is empty and identifier is CPF")
        void shouldPropagateCpfEmptyException() {
            String emptyCpf = "";
            doThrow(new UserCPFEmptyException())
                    .when(cpfValidator).validate(emptyCpf);

            assertThatThrownBy(() -> validatorComponent.whichDocument(DocumentTypeEnum.CPF.getShortName(), emptyCpf))
                    .isInstanceOf(UserCPFEmptyException.class);

            verify(cpfValidator).validate(emptyCpf);
            verify(cnpjValidator, never()).validate(anyString());
        }

        @Test
        @DisplayName("Should propagate CNPJ empty exception when CNPJ is empty and identifier is CNPJ")
        void shouldPropagateCnpjEmptyException() {
            String emptyCnpj = "";
            doThrow(new StoreCNPJEmptyException())
                    .when(cnpjValidator).validate(emptyCnpj);

            assertThatThrownBy(() -> validatorComponent.whichDocument(DocumentTypeEnum.CNPJ.getShortName(), emptyCnpj))
                    .isInstanceOf(StoreCNPJEmptyException.class);

            verify(cnpjValidator).validate(emptyCnpj);
            verify(cpfValidator, never()).validate(anyString());
        }

        @ParameterizedTest
        @CsvSource({
                "CPF, 52998224725",
                "CNPJ, 11222333000181"
        })
        @DisplayName("Should call correct validator based on document identifier")
        void shouldCallCorrectValidator(String identifier, String document) {
            validatorComponent.whichDocument(identifier, document);

            if (DocumentTypeEnum.CPF.getShortName().equals(identifier)) {
                verify(cpfValidator).validate(document);
                verify(cnpjValidator, never()).validate(anyString());
            } else {
                verify(cnpjValidator).validate(document);
                verify(cpfValidator, never()).validate(anyString());
            }
        }
    }

    @Nested
    @DisplayName("Integration-like behavior tests")
    class IntegrationBehaviorTests {

        @Test
        @DisplayName("Should validate all valid data without throwing exceptions")
        void shouldValidateAllValidData() {
            String validEmail = "user@example.com";
            String validCpf = "52998224725";
            String validCnpj = "11222333000181";

            assertThatCode(() -> validatorComponent.validate(validEmail, DocumentTypeEnum.CPF.getShortName(), validCpf))
                    .doesNotThrowAnyException();

            assertThatCode(() -> validatorComponent.validate(validEmail, DocumentTypeEnum.CNPJ.getShortName(), validCnpj))
                    .doesNotThrowAnyException();

            verify(emailValidator, times(2)).validate(validEmail);
            verify(cpfValidator).validate(validCpf);
            verify(cnpjValidator).validate(validCnpj);
        }
    }
}