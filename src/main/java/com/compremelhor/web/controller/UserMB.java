package com.compremelhor.web.controller;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import com.compremelhor.model.entity.Account;

@ManagedBean
@SessionScoped
public class UserMB implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private Account account;

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}
	
}
