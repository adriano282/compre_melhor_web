package com.compremelhor.web.controller;

import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.inject.Inject;

import com.compremelhor.model.entity.Account;
import com.compremelhor.model.entity.Partner;
import com.compremelhor.model.exception.InvalidEntityException;
import com.compremelhor.model.service.AccountService;
import com.compremelhor.web.util.JSFUtil;

@ManagedBean
@SessionScoped
public class AccountController {
	
	@Inject AccountService accountService;
	
	private List<Partner> partners;
	
	private Account accountTarget;
	
	private String currentPassword;
	
	public AccountController() {
		accountTarget = new Account();
		JSFUtil.manageScopes("accountController");
	}
	
	public void doRegister() {
		try {
			accountService.create(accountTarget);
		} catch (InvalidEntityException e) {
			JSFUtil.addMessage("account.register.error", FacesMessage.SEVERITY_ERROR);
			return;
		}
		JSFUtil.addMessage("account.created.successfully", FacesMessage.SEVERITY_INFO);
	}

	
	public Account getAccountTarget() { return accountTarget; }
	public void setAccountTarget(Account accountTarget) { this.accountTarget = accountTarget;}

	public String getCurrentPassword() { return currentPassword;}
	public void setCurrentPassword(String currentPassword) { this.currentPassword = currentPassword;}

	public List<Partner> getPartners() {
		return partners;
	}

	public void setPartners(List<Partner> partners) {
		this.partners = partners;
	}
}
