package com.danilodps.commons.domain.validation;

import com.danilodps.commons.domain.model.enums.DocumentTypeEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ValidatorComponent {

    private final CpfValidator cpfValidator;
    private final CnpjValidator cnpjValidator;
    private final EmailValidator emailValidator;

    public void validate(String email, String documentIdentifier, String document) {
        whichDocument(documentIdentifier, document);
        emailValidator.validate(email);
    }

    public void whichDocument(String documentIdentifier, String document) {
        if (documentIdentifier == null) {
            log.warn("Document identifier is null, defaulting to CNPJ validation");
            cnpjValidator.validate(document);
            return;
        }

        if (DocumentTypeEnum.CPF.getShortName().equals(documentIdentifier)) {
            log.info("Validando CPF");
            cpfValidator.validate(document);
        } else {
            log.info("Validando CNPJ");
            cnpjValidator.validate(document);
        }
    }
}