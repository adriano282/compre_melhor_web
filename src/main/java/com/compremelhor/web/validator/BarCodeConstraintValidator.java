package com.compremelhor.web.validator;

import java.util.regex.Pattern;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class BarCodeConstraintValidator implements ConstraintValidator<BarCode, String> {
	private Pattern pattern;
	private static final String BARCODE_PATTERN = "^\\d{13}$";
	
	public void initialize(BarCode bc) {
		pattern = Pattern.compile(BARCODE_PATTERN);
	}
	
	public boolean isValid(String value, ConstraintValidatorContext cvc) {
		if (value == null) return true;
		
		if (!pattern.matcher(value.toString()).matches()) return false;
		
		int i = 1;
		int sumOddNumbers = 0;
		int resultEvenNumbers = 0;
		
		for (String s: value.split("")) {
			if (i == 13) break;
			
			if (i++ % 2 != 0) 
				sumOddNumbers += Integer.valueOf(s);
			else 
				resultEvenNumbers += Integer.valueOf(s) * 3;
		}
		
		int result = resultEvenNumbers + sumOddNumbers;
		
		Integer verifier = new Integer(0);
		
		while (result++ % 10 != 0) {
			verifier++;
		}
		
		return verifier.equals(Integer.valueOf(value.split("")[12]));
	}
}
