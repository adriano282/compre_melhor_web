package com.compremelhor.web.controller;

import java.awt.Event;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.validation.constraints.Pattern;

import org.primefaces.context.RequestContext;

import com.compremelhor.model.entity.Account;
import com.compremelhor.model.entity.Category;
import com.compremelhor.model.entity.Manufacturer;
import com.compremelhor.model.entity.Sku;
import com.compremelhor.model.entity.Sku.Status;
import com.compremelhor.model.entity.Sku.UnitType;
import com.compremelhor.model.exception.InvalidEntityException;
import com.compremelhor.model.service.CategoryService;
import com.compremelhor.model.service.ManufacturerService;
import com.compremelhor.model.service.SkuService;
import com.compremelhor.model.service.StockService;
import com.compremelhor.web.util.JSFUtil;

@ManagedBean
@ViewScoped
public class SkuController implements Serializable {
	private static final long serialVersionUID = 1L;

	@Inject	private SkuService skuService;
	@Inject	private ManufacturerService mfrService;
	@Inject private CategoryService cs;
	@Inject private StockService ss;
	
	@Pattern(regexp="^\\d{13}$")
//	@BarCode(message = "sku.barcode.invalid")
	private String targetSkuBarCode;
	private Integer skuIdTarget;
	
	private Sku skuTarget;
	private List<Sku> filteredSkus;
	private List<Sku> skus;
	private List<Manufacturer> mfrs;
	private List<Category> categories;

	public void onListPage() {
		instanceTarget();		
		if (skus == null)
			skus = skuService.findAll();
	}
	
	public void onCreatePage() {
		onForm();
		instanceTarget();
	}
	
	public String onEditPage() {
		String skuId = JSFUtil.getRequestParameter("skuId");
		Integer id;
		try {
			id = Integer.valueOf(skuId);
		} catch (NumberFormatException e) {
			return "list?faces-redirect=true";
		}
		skuTarget = skuService.find(id);
		if (skuTarget == null) {
			JSFUtil.addMessage("sku.not.found.error", FacesMessage.SEVERITY_ERROR);
			return "";
		} 
		targetSkuBarCode = skuTarget.getCode();
		onForm();
		return "";
	}
	
	public void onForm() {
		if (mfrs == null)
			mfrs = mfrService.findAll();
		
		if (categories == null)
			categories = cs.findAll();
	}
	
	public void instanceTarget() {
		if (skuTarget == null) {
			skuTarget = new Sku();
			skuTarget.setManufacturer(new Manufacturer());
			skuTarget.setCategory(new Category());
		}
	}
	
	public void onRowSelected(Event event) {
		RequestContext.getCurrentInstance()
			.update("form");
		if (skuTarget.getId() != 0) {
			RequestContext				
				.getCurrentInstance()
				.execute("PF('optDlg').show();");
		}
	}
	
	public String openEditForm(Integer skuId) {
		return "edit?faces-redirect=true&skuId="+skuId;
	}
	
	public void publishSku(Integer skuId) {
		try {
			Sku s = skuService.find(skuId);
			if (s != null) {
				s.setStatus(Status.PUBLICADO);
				skuService.edit(s);
				skus = skuService.findAll();
			}
		} catch (Exception e) {
			JSFUtil.addErrorMessage("sku.changed.error");
			System.out.println("Error: " +e.getMessage());
			return;
		}
		JSFUtil.addInfoMessage("sku.published.successfully");
	}
	
	public String createSku() {
		skuTarget.setCode(targetSkuBarCode);
		try {
			Category c = cs.find("name", skuTarget.getCategory().getName());
			skuTarget.setCategory(c);
			skuService.create(skuTarget);
			JSFUtil.addMessage("sku.registered.successfully", FacesMessage.SEVERITY_INFO);
			
			Account ac = null;
			if ((ac = JSFUtil.getLoggedUser()) != null 
					&& ac.getPartner() != null) { 
				ss.createStock(ac.getPartner(), skuTarget);
			}
			return "list?faces-redirect=true";
		} catch (InvalidEntityException e) {
			tryResolveErrorMessage(e, "sku.registered.error");
			return null;
		}
		
	}
	
	public void deleteSku(Integer skuId) {
		try {
			Sku s = skuService.find(skuId);
			if (s != null) {
				s.setStatus(Status.DELETADO);
				skuService.edit(s);
				skus = skuService.findAll();
			}
		} catch (Exception e) {
			JSFUtil.addMessage("sku.deleted.error", FacesMessage.SEVERITY_ERROR);
			return;
		}
		JSFUtil.addMessage("sku.deleted.successfully", FacesMessage.SEVERITY_INFO);
	}
	
	public String editSku() {
		skuTarget.setCode(targetSkuBarCode);
		try {
			Category c = cs.find("name", skuTarget.getCategory().getName());
			skuTarget.setCategory(c);
			skuService.edit(skuTarget);
		} catch (InvalidEntityException e) {
			tryResolveErrorMessage(e, "sku.changed.error");
			return null;
		}
		JSFUtil.addInfoMessage("sku.changed.successfully", true);
		return "list?faces-redirect=true";
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
					String componentName = "skuFormSubmit:form:".concat(field);
					JSFUtil.addErrorMessage(s,  componentName);
					return;
				} catch (Exception r) {r.printStackTrace();}
			}
		}
		JSFUtil.addErrorMessage(errorMessage);
	}
	
	
	public List<Sku> getSkus() {
		if (skus == null)
			skus = skuService.findAll();
		
		return skus;
	}

	public void setSkus(List<Sku> skus) {
		this.skus = skus;
	}

	public Sku getSkuTarget() {
		return skuTarget;
	}

	public void setSkuTarget(Sku skuTarget) {
		this.skuTarget = skuTarget;
	}

	public Integer getSkuIdTarget() {
		return skuIdTarget;
	}

	public void setSkuIdTarget(Integer skuIdTarget) {
		this.skuIdTarget = skuIdTarget;
	}

	public String getTargetSkuBarCode() {
		return targetSkuBarCode;
	}

	public void setTargetSkuBarCode(String targetSkuBarCode) {
		this.targetSkuBarCode = targetSkuBarCode;
	}

	public List<Manufacturer> getMfrs() {
		Collections.sort(mfrs);
		return mfrs;
	}

	public void setMfrs(List<Manufacturer> mfrs) {
		this.mfrs = mfrs;
	}

	public List<Category> getCategories() {
		Collections.sort(categories);
		return categories;
	}

	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}

	public List<Sku> getFilteredSkus() {
		return filteredSkus;
	}

	public void setFilteredSkus(List<Sku> filteredSkus) {
		this.filteredSkus = filteredSkus;
	}
	
	public UnitType[] getUnitTypeValues() {
		return UnitType.values();
	}
}

