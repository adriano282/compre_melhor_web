package com.compremelhor.web.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import com.compremelhor.model.entity.Manufacturer;
import com.compremelhor.model.exception.InvalidEntityException;
import com.compremelhor.model.service.ManufacturerService;
import com.compremelhor.web.util.JSFUtil;

@ManagedBean
@ViewScoped
public class ManufacturerController {
	@Inject
	private ManufacturerService manufacturerService;
	
	private List<Manufacturer> manufacturers;
	private List<Manufacturer> filteredManufacturers;
	private Manufacturer manufacturerTarget;
	
	@PostConstruct
	public void init() {
		manufacturerTarget = new Manufacturer();
		manufacturers = manufacturerService.findAll();
	}
	
	public void delete(Integer id) {
		if (id != null) {
			Manufacturer m = manufacturerService.find(id);
			try {
				manufacturerService.remove(m);
				manufacturers = manufacturerService.findAll();
			} catch (Exception e) {
				JSFUtil.addErrorMessage("manufacturer.has.sku.associated.error");
				return;
			}
			JSFUtil.addInfoMessage("manufacturer.removed.successfully");		
		}
	}
	
	public String create() {
		try {
			manufacturerService.create(manufacturerTarget);
		} catch (InvalidEntityException e) {
			tryResolveErrorMessage(e, "manufacturer.regiter.error");
			return "";
		}
		JSFUtil.addInfoMessage("manufacturer.created.succssfully",true);
		return "list?faces-redirect=true";
	}
	
	public String edit() {
		try {
			manufacturerService.edit(manufacturerTarget);
		} catch (InvalidEntityException e) {
			tryResolveErrorMessage(e, "manufacturer.change.error");
			return "";
		}
		JSFUtil.addInfoMessage("manufacturer.changed.successfully", true);
		return "list?faces-redirect=true";
	}
	
	public String onEditPage() {
		String mfrId = JSFUtil.getRequestParameter("id");
		Integer id;
		try {
			id = Integer.valueOf(mfrId);
		} catch (Exception e) {
			e.printStackTrace();
			return "list?faces-redirect=true";
		}
		manufacturerTarget = manufacturerService.find(id);
		if (manufacturerTarget == null) {
			JSFUtil.addErrorMessage("manufacturer.not.found.error");
		}
		return "";
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
					String componentName = "form:form:".concat(field);
					JSFUtil.addErrorMessage(s,  componentName);
					return;
				} catch (Exception r) {r.printStackTrace();}
			}
		}
		JSFUtil.addErrorMessage(errorMessage);
	}

	public List<Manufacturer> getManufacturers() {
		return manufacturers;
	}

	public void setManufacturers(List<Manufacturer> manufacturers) {
		this.manufacturers = manufacturers;
	}

	public List<Manufacturer> getFilteredManufacturers() {
		return filteredManufacturers;
	}

	public void setFilteredManufacturers(List<Manufacturer> filteredManufacturers) {
		this.filteredManufacturers = filteredManufacturers;
	}

	public Manufacturer getManufacturerTarget() {
		return manufacturerTarget;
	}

	public void setManufacturerTarget(Manufacturer manufacturerTarget) {
		this.manufacturerTarget = manufacturerTarget;
	}

	
	
}
