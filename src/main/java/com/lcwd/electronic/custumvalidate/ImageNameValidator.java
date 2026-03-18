package com.lcwd.electronic.custumvalidate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ImageNameValidator implements ConstraintValidator<ImageNameValid, String> {
    Logger logger = LoggerFactory.getLogger(ImageNameValidator.class);

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
//trim()+isEmpty() =isBlank() method in java11
        logger.info("value {}",value);

        return !value.trim().isEmpty();
    }
}
