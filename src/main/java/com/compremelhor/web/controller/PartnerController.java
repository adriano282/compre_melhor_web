package com.compremelhor.web.controller;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.inject.Inject;

import com.compremelhor.model.entity.Partner;
import com.compremelhor.model.service.PartnerService;

@ManagedBean
@SessionScoped
public class PartnerController {
	
	@Inject private PartnerService partnerService;
	private List<Partner> partners;

	public PartnerController() {
		listPartners();
	}
	
	public void listPartners() {
		if (partnerService == null) {
			partners = new ArrayList<>();
			System.out.println("IS null");
			return;
		}
		partners = partnerService.findAll();
	}
	
//	------------------ Getters and Setters -------------------------
	
	public List<Partner> getPartners() { return partners; }
	public void setPartners(List<Partner> partners) { this.partners = partners;}
}
