package com.compremelhor.web.controller;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.inject.Inject;

import com.compremelhor.model.entity.Account;
import com.compremelhor.model.service.AccountService;
import com.compremelhor.web.util.JSFUtil;

@ManagedBean
@RequestScoped
public class LoginController {

	@Inject
	private AccountService accountService;
	
	private Account accountLogin;
	
	public LoginController() {
		accountLogin = new Account();
	}
	
	public String login() {
		Account ac = findAccountByUsername(accountLogin.getUsername());

		if (ac == null) {
			JSFUtil.addMessage("no.account.found.error", 
					FacesMessage.SEVERITY_ERROR);
			return "";
		}
		
		if (!matchesPassword(ac, accountLogin)) {
			JSFUtil.addMessage("incorrect.password.error", 
					FacesMessage.SEVERITY_ERROR);
			return "";
		}
		
		JSFUtil.setLoggedUser(ac);
		JSFUtil.addMessage("user.authenticated.sucessufully", FacesMessage.SEVERITY_INFO, true);
		
		return "views/home.xhtml?faces-redirect=true";
		
	}
	
	private boolean matchesPassword(Account accountFromDB, Account accounToCkeck) {
		return accountFromDB.getPassword()
				.equals(accounToCkeck.getPassword());
	}
	
	private Account findAccountByUsername(String username) {
		return accountService.find("username", username);
	}
	
	public Account getAccount() {
		return accountLogin;
	}

	public void setAccount(Account account) {
		this.accountLogin = account;
	}
}
