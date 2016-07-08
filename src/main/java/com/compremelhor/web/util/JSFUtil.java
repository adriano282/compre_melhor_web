package com.compremelhor.web.util;

import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;

public class JSFUtil {
	public static void addMessage(String messageProperty, Severity severity) {
		FacesContext context = FacesContext.getCurrentInstance();
		ResourceBundle bundle = context.getApplication()
				.getResourceBundle(context, "msg");
		String message = bundle.getString(messageProperty);
		context.addMessage(null,new FacesMessage(severity, 
						message, message)); 
	}
}
