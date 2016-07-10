package com.compremelhor.web.controller;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;

@ManagedBean(eager = true)
@ApplicationScoped
public class Application {
	private String rootPath;

	public void init() {
		rootPath = FacesContext
					.getCurrentInstance()
					.getExternalContext()
					.getRequestContextPath()
					.concat("/webapp/views");
	}
	
	public String getRootPath() {return rootPath;}
	public void setRootPath(String rootPath) {this.rootPath = rootPath;}
}
