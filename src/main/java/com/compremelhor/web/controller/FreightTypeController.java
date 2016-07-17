package com.compremelhor.web.controller;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import com.compremelhor.model.entity.Account;
import com.compremelhor.model.entity.FreightType;
import com.compremelhor.model.exception.InvalidEntityException;
import com.compremelhor.model.service.FreightTypeService;
import com.compremelhor.web.util.JSFUtil;

@ManagedBean
@ViewScoped
public class FreightTypeController {
	
	@Inject private FreightTypeService service;
	
	private List<FreightType> types;
	private List<FreightType> filteredTypes;
	private FreightType targetType;

	@PostConstruct
	public void init() {
		
		if (service == null) System.out.println("NULLL");
		else System.out.println("NOT NULL");
		
		types = service.findAll();
		targetType = new FreightType();
	}
	
	public String create() {
		try {
			Account ac;
			if ((ac = JSFUtil.getLoggedUser()) != null) {
				targetType.setPartner(ac.getPartner());
			}
			System.out.println(targetType);
			service.create(targetType);
		} catch (InvalidEntityException e) {
			e.printStackTrace();
			JSFUtil.addErrorMessage("freight_type.save.error");
			return "";
		}
		JSFUtil.addInfoMessage("freight_type.registered.successfully", true);
		return "list.xhtml?faces-redirect=true";
	}
	
	public String openEditPage(int typeId) {
		return "edit.xhtml?faces-redirect=true&typeId=" + typeId;
	}
	
	public void onEditPage() {
		targetType = new FreightType();
		String id = JSFUtil.getRequestParameter("typeId");
		try {
			Integer typeId = Integer.valueOf(id);
			targetType = service.find(typeId);
		} catch (Exception e) {
			System.out.println(e);
			return;
		}
	}
	
	public String edit() {
		try {
			Account ac;
			if ((ac = JSFUtil.getLoggedUser()) != null) {
				targetType.setPartner(ac.getPartner());
			}
			
			service.edit(targetType);
		} catch (InvalidEntityException e) {
			e.printStackTrace();
			JSFUtil.addErrorMessage("freight_type.edit.error");
			return "";
		}
		JSFUtil.addInfoMessage("freight_type.changed.successfully", true);
		return "list.xhtml?faces-redirect=true";
	}
	
	public void delete(int typeId) {
		
		try {
			FreightType ft = service.find(typeId);
			service.remove(ft);
			types = service.findAll();
		} catch (Exception e) {
			JSFUtil.addErrorMessage("freght_type.delete.error");
			return;
		}
		JSFUtil.addInfoMessage("freight_type.delete.successfully");
	}
	
	public List<FreightType> getTypes() {
		return types;
	}
	public void setTypes(List<FreightType> types) {
		this.types = types;
	}
	public List<FreightType> getFilteredTypes() {
		return filteredTypes;
	}
	public void setFilteredTypes(List<FreightType> filteredTypes) {
		this.filteredTypes = filteredTypes;
	}
	public FreightType getTargetType() {
		return targetType;
	}
	public void setTargetType(FreightType targetType) {
		this.targetType = targetType;
	}
}
