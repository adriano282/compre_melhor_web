package com.compremelhor.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.compremelhor.model.entity.Account;

public class AuthenticatorFilter implements Filter {
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		
		String requestURI = httpRequest.getRequestURI();
		String contextPath = httpRequest.getContextPath();
		String requestPath = requestURI.substring(contextPath.length() +1);
		Account loggedUser = (Account) httpRequest.getSession().getAttribute("LOGIN_USER");
		
		if (!isPublicUrl(requestPath) && loggedUser == null) {
			httpResponse.sendRedirect(httpRequest.getContextPath() + "/faces/login.xhtml");
		}
		else if (requestPath.contains("account") 
				&& loggedUser != null 
				&& !loggedUser.getRoles().stream().anyMatch(r -> r.getRoleName().equals("admin"))) {
			httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN);
		}
		else {
			chain.doFilter(request, response);
		}
	}
	
	private boolean isPublicUrl(String url) {
		return !url.contains("views/") || url.startsWith("javax.faces.resource");}

	@Override
	public void destroy() {}
}
