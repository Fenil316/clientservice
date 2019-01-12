package com.planoaccounting.validation;


import com.planoaccounting.exceptions.UnexpectedException;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class RequestValidator {

    private static final String IS_VALID = "isValid";

    private static Validator validator;

    static {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    public static <T> List<FrameworkError> validRequest(T request) {
        List<FrameworkError> errors = new ArrayList<>();
        Set<ConstraintViolation<T>> violations = validator.validate(request);
        if(!violations.isEmpty()) {
            violations.forEach(violation -> {
                errors.add(new FrameworkError(violation.getPropertyPath().toString(), violation.getMessage()));
            });
        }

        try {
            return (List<FrameworkError>) request.getClass().getDeclaredMethod(IS_VALID, new Class[]{request.getClass(), List.class}).invoke(request, request, errors);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new UnexpectedException("");
        }
    }
}
