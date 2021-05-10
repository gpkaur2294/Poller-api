package com.kry.poller.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.validator.routines.UrlValidator;

public class UrlInfoValidator implements ConstraintValidator<UrlConstraint, String> {

	@Override
	public boolean isValid(String url, ConstraintValidatorContext context) {
		String[] schemes = { "http", "https" };
		UrlValidator urlValidator = new UrlValidator(schemes);
		return urlValidator.isValid(url);
	}

}
