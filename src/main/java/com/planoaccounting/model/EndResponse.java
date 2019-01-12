package com.planoaccounting.model;

import com.planoaccounting.validation.FrameworkError;

import java.util.ArrayList;
import java.util.List;

public class EndResponse<T> {

    private T payload;
    private List<FrameworkError> validationErrors = new ArrayList<>();
    private List<FrameworkError> errors = new ArrayList<>();

    public T getPayload() {
        return payload;
    }

    public void setPayload(T payload) {
        this.payload = payload;
    }

    public List<FrameworkError> getValidationErrors() {
        return validationErrors;
    }

    public void setValidationErrors(List<FrameworkError> validationErrors) {
        this.validationErrors = validationErrors;
    }

    public List<FrameworkError> getErrors() {
        return errors;
    }

    public void setErrors(List<FrameworkError> errors) {
        this.errors = errors;
    }
}
