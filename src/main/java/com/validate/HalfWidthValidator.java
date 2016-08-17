package com.validate;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


public class HalfWidthValidator
	implements ConstraintValidator<HalfWidth,String>{
	private Pattern pattern;

	@Override
	public void initialize(com.validate.HalfWidth constraintAnnotation) {
		pattern = Pattern.compile("[0-9a-zA-Z]+$");
	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if(value.length() == 0){
			return true;
		}
		Matcher matcher = pattern.matcher(value);
		return matcher.find();
	}
}
