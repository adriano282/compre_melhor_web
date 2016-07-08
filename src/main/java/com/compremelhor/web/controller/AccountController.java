package com.compremelhor.web.controller;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import com.compremelhor.model.entity.Account;
import com.compremelhor.model.service.AccountService;

@ManagedBean
@ViewScoped
public class AccountController {
	
	@Inject AccountService accountService;
	
	private Account accountTarget;
	
	private String currentPassword;
	
	public AccountController() {
		accountTarget = new Account();
	}
	
	public void doRegister() {
		System.out.println("DO register");
	}

	
	public Account getAccountTarget() { return accountTarget; }
	public void setAccountTarget(Account accountTarget) { this.accountTarget = accountTarget;}

	public String getCurrentPassword() { return currentPassword;}
	public void setCurrentPassword(String currentPassword) { this.currentPassword = currentPassword;}
}
