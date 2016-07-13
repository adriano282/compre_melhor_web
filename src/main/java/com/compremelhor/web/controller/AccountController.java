package com.compremelhor.web.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.inject.Inject;

import com.compremelhor.model.entity.Account;
import com.compremelhor.model.entity.Partner;
import com.compremelhor.model.exception.InvalidEntityException;
import com.compremelhor.model.service.AccountService;
import com.compremelhor.web.util.JSFUtil;

@ManagedBean
@ViewScoped
public class AccountController {
	
	@Inject AccountService accountService;
	
	private List<Partner> partners;
	
	private Account accountTarget;
	private List<Account> accounts;
	private List<Account> filteredAccounts;
	
	private String currentPassword;
	
	@PostConstruct
	public void init() {
		accountTarget = new Account();
		refreshAccount();
	}
	
	private void refreshAccount() {
		accounts = accountService.findAll();
	}
	public void delete(Integer id) {
		if (id != null) {
			Account ac = accountService.find(id);
			try {
				accountService.remove(ac);
				refreshAccount();
			} catch (Exception e) {
				JSFUtil.addErrorMessage("account.remove.error");
				return;
			}
			JSFUtil.addInfoMessage("account.removed.successfully");		
		}
	}
	
	public String doRegister() {
		try {
			Account ac;
			if ((ac = JSFUtil.getLoggedUser()) != null) {
				accountTarget.setPartner(ac.getPartner());
				accountService.create(accountTarget);
			}
			
			
		} catch (InvalidEntityException e) {
			tryResolveErrorMessage(e, "account.register.error");
			return "";
		}
		JSFUtil.addInfoMessage("account.created.successfully", true);
		return "list?faces-redirect=true";
	}
	
	private boolean machesPassword(String oldPassword, String newPassword) {
		return oldPassword.equals(newPassword);
	}
	
	public String changePassword() {
		try {
			Account ac = accountService.find(accountTarget.getId());
			if (ac == null || !machesPassword(currentPassword, ac.getPassword())) {
				JSFUtil.addErrorMessage("account.password.wrong.error", "form:oldPassword");
				return "";
			}
			
			ac.setPassword(accountTarget.getPassword());
			accountService.edit(ac);			
		} catch (InvalidEntityException e) {
			tryResolveErrorMessage(e, "account.change.password.error");
			return "";
		}
		JSFUtil.addInfoMessage("account.changed.passoword.successfully", true);
		return "list?faces-redirect=true";
	}

	public String edit() {
		try {
			Account ac = accountService.find(accountTarget.getId());
			if (ac != null) {
				ac.setUsername(accountTarget.getUsername());
				accountService.edit(ac);	
			}
			
		} catch (InvalidEntityException e) {
			tryResolveErrorMessage(e, "account.change.error");
			return "";
		}
		JSFUtil.addInfoMessage("account.changed.successfully", true);
		return "list?faces-redirect=true";
	}
	
	public String onEditPage() {
		String acId = JSFUtil.getRequestParameter("id");
		Integer id;
		try {
			id = Integer.valueOf(acId);
		} catch (Exception e) {
			e.printStackTrace();
			return "list?faces-redirect=true";
		}
		accountTarget = accountService.find(id);
		if (accountTarget == null) {
			JSFUtil.addErrorMessage("account.not.found.error");
		}
		return "";
	}

	public String openChangePasswordForm(Integer id) {
		return "change_password?faces-redirect=true&id="+id;
	}
	public String openEditForm(Integer id) {
		return "edit?faces-redirect=true&id="+id;
	}
	
	private void tryResolveErrorMessage(InvalidEntityException e, String defaultMessage) {
		String errorMessage = defaultMessage;
		if (e.getMessage() != null && !e.getMessage().isEmpty()) {
			List<String> errors;
			
			if (e.getMessage().contains("#")) {
				errors = Arrays.asList(e.getMessage().split("#"));
			} 
			else {
				errors = new ArrayList<>();
				errors.add(e.getMessage());
			}
			
			for (String s : errors) {
				try { 
					String field = s.split("\\.")[1];
					String componentName = "form:".concat(field);
					JSFUtil.addErrorMessage(s,  componentName);
					return;
				} catch (Exception r) {r.printStackTrace();}
			}
		}
		JSFUtil.addErrorMessage(errorMessage);
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

	public List<Account> getAccounts() {
		return accounts;
	}

	public void setAccounts(List<Account> accounts) {
		this.accounts = accounts;
	}

	public List<Account> getFilteredAccounts() {
		return filteredAccounts;
	}

	public void setFilteredAccounts(List<Account> filteredAccounts) {
		this.filteredAccounts = filteredAccounts;
	}
}
