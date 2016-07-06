package com.compremelhor.web.controller;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.compremelhor.model.entity.Account;

@ManagedBean
@ViewScoped
public class AccountController {
	
	private Account accountTarget;
	
	public AccountController() {
		accountTarget = new Account();
	}
	public void doRegister() {
		System.out.println("DO register");
	}

	public Account getAccountTarget() {
		return accountTarget;
	}

	public void setAccountTarget(Account accountTarget) {
		this.accountTarget = accountTarget;
	}
}
