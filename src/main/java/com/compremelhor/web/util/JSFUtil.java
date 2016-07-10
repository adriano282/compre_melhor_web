package com.compremelhor.web.util;

import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.compremelhor.model.entity.Account;

public class JSFUtil {
	
	public static void setLoggedUser(Account account) {
		FacesContext context = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) context.getExternalContext().getSession(true);
		session.setAttribute("LOGIN_USER", account);
	}
	
	public static Account getLoggedUser() {
		
		FacesContext context = FacesContext.getCurrentInstance();
		HttpSession session = (HttpSession) context.getExternalContext().getSession(true);
		
		return (Account) session.getAttribute("LOGIN_USER");
	}
	
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
}
