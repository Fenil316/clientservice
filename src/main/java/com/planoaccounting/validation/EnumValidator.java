package com.planoaccounting.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.EnumSet;
import java.util.Set;
import java.util.stream.Collectors;

public class EnumValidator implements ConstraintValidator<ValidateEnum, String> {
    private Set<Enum> allowedValues;

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void initialize(ValidateEnum targetEnum) {
        Class<? extends Enum> enumSelected = targetEnum.targetClassType();
        allowedValues = (Set<Enum>) EnumSet.allOf(enumSelected).stream().map(e -> ((Enum<? extends Enum<?>>) e))
                .collect(Collectors.toSet());
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value == null || allowedValues.contains(value)? true : false;
    }
}