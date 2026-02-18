package com.danilodps.commons.application.exceptions;

import com.danilodps.commons.domain.model.error.StandardErrorDetails;
import com.danilodps.commons.domain.model.error.StandardErrorResponse;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.List;

public class BusinessValidationException extends RuntimeException {

    private transient StandardErrorResponse standardErrorResponse;

    public BusinessValidationException(String message) {
        super(message);
    }

    public BusinessValidationException(StandardErrorDetails standardErrorDetails, HttpStatus httpStatus){
        createExceptionBody(standardErrorDetails, httpStatus);
    }

    public BusinessValidationException(List<StandardErrorDetails> standardErrorDetails, HttpStatus httpStatus){
        createExceptionBody(standardErrorDetails, httpStatus);
    }

    private void createExceptionBody(StandardErrorDetails standardErrorDetails, HttpStatus httpStatus){
        this.standardErrorResponse.setStatusCode(httpStatus);
        this.standardErrorResponse.setErrorType("Business Validation Error");
        this.standardErrorResponse.setStandardErrorDetailsList(Collections.singletonList(standardErrorDetails));
    }

    private void createExceptionBody(List<StandardErrorDetails> standardErrorDetails, HttpStatus httpStatus){
        this.standardErrorResponse.setStatusCode(httpStatus);
        this.standardErrorResponse.setErrorType("Business Validation Error");
        this.standardErrorResponse.setStandardErrorDetailsList(standardErrorDetails);
    }
}
