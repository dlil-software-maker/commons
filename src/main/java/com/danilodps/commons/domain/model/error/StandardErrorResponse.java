package com.danilodps.commons.domain.model.error;

import org.springframework.http.HttpStatus;

import java.util.List;

public class StandardErrorResponse {

    private HttpStatus statusCode;
    private String errorType;
    private List<StandardErrorDetails> standardErrorDetailsList;

    public StandardErrorResponse() {
    }

    public StandardErrorResponse(HttpStatus statusCode, String errorType, List<StandardErrorDetails> standardErrorDetailsList) {
        this.statusCode = statusCode;
        this.errorType = errorType;
        this.standardErrorDetailsList = standardErrorDetailsList;
    }

    public HttpStatus getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(HttpStatus statusCode) {
        this.statusCode = statusCode;
    }

    public String getErrorType() {
        return errorType;
    }

    public void setErrorType(String errorType) {
        this.errorType = errorType;
    }

    public List<StandardErrorDetails> getStandardErrorDetailsList() {
        return standardErrorDetailsList;
    }

    public void setStandardErrorDetailsList(List<StandardErrorDetails> standardErrorDetailsList) {
        this.standardErrorDetailsList = standardErrorDetailsList;
    }

    @Override
    public String toString() {
        return "StandardErrorResponse{" +
                "statusCode=" + statusCode +
                ", errorType='" + errorType + '\'' +
                ", standardErrorDetailsList=" + standardErrorDetailsList +
                '}';
    }

}
