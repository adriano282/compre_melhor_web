package com.compremelhor.web.util;

import java.util.Map;
import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

public class JSFUtil {
	public static void addMessage(String messageProperty, Severity severity) {
		FacesContext context = FacesContext.getCurrentInstance();
		ResourceBundle bundle = context.getApplication()
				.getResourceBundle(context, "messages");
		String message = "";
		try {
			message = bundle.getString(messageProperty);
		} catch (Exception e) {
			System.out.println("No message found for: " + messageProperty);
		}
		context.addMessage(null,new FacesMessage(severity, message, message)); 
	}
	
	public static void addMessage(String messageProperty, Severity severity, boolean flash) {
		addMessage(messageProperty, severity);
		if (flash) {
			FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
		}
	}
	
	public static String getRequestParameter(String property) {
		HttpServletRequest req = 
				(HttpServletRequest) FacesContext
									.getCurrentInstance()
									.getExternalContext()
									.getRequest();
		return req.getParameter(property);	
	}
	
	public static void manageScopes(String currentController) {
		FacesContext context = FacesContext.getCurrentInstance();
		
		if (currentController != "skuController") {
			Map<String, Object> sessionMap = context
								.getExternalContext()
								.getSessionMap();
								
			
			sessionMap.forEach((s, o) -> System.out.println("Key: " + s + ", Object: " + o));
			
			if (sessionMap.get("userController") == null) {
				System.out.println("RETORNOU NULL");
				
			} else {
				System.out.println(sessionMap.get("userController"));
			}
			
		}
	}
}
