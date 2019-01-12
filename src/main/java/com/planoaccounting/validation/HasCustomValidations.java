package com.planoaccounting.validation;

import java.util.List;

public interface HasCustomValidations<T> {
    public List<FrameworkError> isValid(T request, List<FrameworkError> errorsList);
}
