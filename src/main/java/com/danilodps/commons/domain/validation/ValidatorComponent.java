package com.danilodps.commons.domain.validation;

import com.danilodps.commons.domain.model.enums.DocumentTypeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ValidatorComponent {
    private static final Logger log = LoggerFactory.getLogger(ValidatorComponent.class);

    private final CpfValidator cpfValidator;
    private final CnpjValidator cnpjValidator;
    private final EmailValidator emailValidator;

    public ValidatorComponent(CpfValidator cpfValidator, CnpjValidator cnpjValidator, EmailValidator emailValidator) {
        this.cpfValidator = cpfValidator;
        this.cnpjValidator = cnpjValidator;
        this.emailValidator = emailValidator;
    }

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