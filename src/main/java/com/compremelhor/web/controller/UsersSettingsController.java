package com.compremelhor.web.controller;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.inject.Inject;

import com.compremelhor.model.entity.Account;
import com.compremelhor.model.service.AccountService;
import com.compremelhor.web.util.JSFUtil;

@ManagedBean
@SessionScoped
public class UsersSettingsController {
	@Inject
	private AccountService service;
	
	public boolean isAdmin() {
		Account account = JSFUtil.getLoggedUser();
		Account ac = service.find(account.getId());
		
		return ac.getRoles().stream().anyMatch(r -> r.getRoleName().equals("admin"));
	}
}
