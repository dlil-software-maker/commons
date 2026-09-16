package com.danilodps.commons.domain.validation;

import com.danilodps.commons.application.exceptions.EmailEmptyException;
import com.danilodps.commons.application.exceptions.InvalidEmailException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class EmailValidator {
    private static final Logger log = LoggerFactory.getLogger(EmailValidator.class);

    private static final String ERROR_EMAIL = "Erro. Email inválido";
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+(?:\\.[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+)*@" + "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";

    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    public void validate(String email) {
        if (isNullOrEmpty(email)) {
            log.error(ERROR_EMAIL);
            throw new EmailEmptyException();
        }

        String trimmedEmail = email.trim();

        if (hasInvalidStructure(trimmedEmail)) {
            log.error(ERROR_EMAIL);
            throw new InvalidEmailException(email);
        }

        if (!matchesPattern(trimmedEmail)) {
            log.error(ERROR_EMAIL);
            throw new InvalidEmailException(email);
        }
    }

    private boolean isNullOrEmpty(String email) {
        return email == null || email.trim().isEmpty();
    }

    private boolean hasInvalidStructure(String email) {
        return email.contains("@.") ||
                email.endsWith(".") ||
                email.startsWith(".") ||
                email.indexOf('@') < 1;
    }

    private boolean matchesPattern(String email) {
        return EMAIL_PATTERN.matcher(email).matches();
    }
}
