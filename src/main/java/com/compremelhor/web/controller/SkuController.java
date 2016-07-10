package com.compremelhor.web.controller;

import java.awt.Event;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.validation.constraints.Pattern;

import org.primefaces.context.RequestContext;

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
	
	@ManagedProperty("#{userMB}")
	private UserMB userMB;
	
	@Inject	private SkuService skuService;
	@Inject	private ManufacturerService mfrService;
	@Inject private CategoryService cs;
	@Inject private StockService ss;
	
	@Pattern(regexp="^\\d{13}$",
			message = "sku.barcode.invalid.format")
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
			JSFUtil.addMessage("sku.changed.error", FacesMessage.SEVERITY_ERROR);
			System.out.println("Error: " +e.getMessage());
			return;
		}
		JSFUtil.addMessage("sku.changed.successufuly", FacesMessage.SEVERITY_INFO);
	}
	
	public String createSku() {
		skuTarget.setCode(targetSkuBarCode);
		try {
			skuService.create(skuTarget);
			JSFUtil.addMessage("sku.registered.successufuly", FacesMessage.SEVERITY_INFO);
			
//			ss.createStock(partner, sku);
		
			return "list?faces-redirect=true";
		} catch (InvalidEntityException e) {
			JSFUtil.addMessage("sku.registered.error", FacesMessage.SEVERITY_ERROR);
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
		JSFUtil.addMessage("sku.deleted.successufuly", FacesMessage.SEVERITY_INFO);
	}
	
	public String editSku() {
		skuTarget.setCode(targetSkuBarCode);
		try {
			skuService.edit(skuTarget);
			RequestContext.getCurrentInstance().update("form");
		} catch (InvalidEntityException e) {
			JSFUtil.addMessage("sku.changed.error", FacesMessage.SEVERITY_ERROR);
			return null;
		}
		JSFUtil.addMessage("sku.changed.successufuly", FacesMessage.SEVERITY_INFO);
		return "";
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
