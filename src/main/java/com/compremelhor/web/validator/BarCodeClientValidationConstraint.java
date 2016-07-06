package com.compremelhor.web.validator;

import java.util.HashMap;
import java.util.Map;

import javax.validation.metadata.ConstraintDescriptor;

import org.primefaces.validate.bean.ClientValidationConstraint;

public class BarCodeClientValidationConstraint implements ClientValidationConstraint {
	public Map<String, Object> getMetadata(@SuppressWarnings("rawtypes") ConstraintDescriptor constraintDescriptor) {
		
		Map<String, Object> metadata = new HashMap<String, Object>();
		@SuppressWarnings("rawtypes")
		Map attrs = constraintDescriptor.getAttributes();
		
		String message = (String) attrs.get("message");
		
		if (message != null) {
			metadata.put("data-cc-msg", message);
		}
		return metadata;
	}
	
	public String getValidatorId() {
		return "BarCode";
	}
}
