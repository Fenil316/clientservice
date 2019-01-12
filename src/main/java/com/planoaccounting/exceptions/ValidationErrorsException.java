package com.planoaccounting.exceptions;

import com.planoaccounting.validation.FrameworkError;

import java.util.List;

public class ValidationErrorsException extends RuntimeException {

    private List<FrameworkError> errors;

    public List<FrameworkError> getErrors() {
        return errors;
    }

    public void setErrors(List<FrameworkError> errors) {
        this.errors = errors;
    }

    public ValidationErrorsException(List<FrameworkError> errors) {
        this.errors = errors;
    }
}
