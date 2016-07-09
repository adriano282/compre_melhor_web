package com.compremelhor.web.controller;

import java.awt.Event;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
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
import com.compremelhor.web.util.JSFUtil;

@ManagedBean
@SessionScoped
public class SkuController implements Serializable {
	private static final long serialVersionUID = 1L;

	@Inject private Logger log = Logger.getGlobal();
	@Inject	private SkuService skuService;
	@Inject	private ManufacturerService mfrService;
	@Inject private CategoryService cs;
	
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
	
	public SkuController() {
		log.log(Level.INFO, "SkuController: Constructor");
		skuTarget = new Sku();
		skuTarget.setManufacturer(new Manufacturer());
		skuTarget.setCategory(new Category());
		System.out.println("Constructed");
	}
	
	public String backToList() {
		cleanTarget();
		listSku();
		RequestContext.getCurrentInstance().update("form");
		return "list?faces-redirect=true";
	}
	
	public void cleanTarget() {
		System.out.println("CHEGOU SER");
		skuTarget = new Sku();
		targetSkuBarCode = "";
	}
	
	public void onRowSelected(Event event) {
		RequestContext.getCurrentInstance().update("form");
		if (skuTarget.getId() != 0) {
			RequestContext
				.getCurrentInstance()
				.execute("optDlg.show();");
		}
	}
	
	public void listSku() {
		skus = skuService.findAll();
	}
	
	public void listMfr() {
		mfrs =  new ArrayList<>();
		mfrs = mfrService.findAll();
	}
	
	public void listCategories() {
		categories = new ArrayList<>();
		categories = cs.findAll();
	}
	
	public String openEditForm(Sku sku) {
		log.log(Level.INFO, "SkuController: openEditForm");
		
		System.out.println("ID: "+sku.getId());
		skuIdTarget = sku.getId();
		skuTarget = skuService.find(skuIdTarget);
		targetSkuBarCode = sku.getCode();
		return "edit";
	}
	
	public void publishSku(Sku sku) {
		log.log(Level.INFO, "SkuController: publishSku");
		try {
			Sku s = skuService.find(sku.getId());
			
			if (s != null) {
				s.setStatus(Status.PUBLICADO);
				skuService.edit(s);
				listSku();
			}
		} catch (Exception e) {
			JSFUtil.addMessage("sku.changed.error", FacesMessage.SEVERITY_ERROR);
			return;
		}
		JSFUtil.addMessage("sku.changed.successufuly", FacesMessage.SEVERITY_INFO);
	}
	
	public String createSku() {
		log.log(Level.INFO, "SkuController: createSku");
		skuTarget.setCode(targetSkuBarCode);

		try {
			skuService.create(skuTarget);
			JSFUtil.addMessage("sku.registered.successufuly", FacesMessage.SEVERITY_INFO);
			return backToList();
		} catch (InvalidEntityException e) {
			JSFUtil.addMessage("sku.registered.error", FacesMessage.SEVERITY_ERROR);
			return null;
		}
	}
	
	public String editSku() {
		log.log(Level.INFO, "SkuController: editSku");
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
		log.log(Level.INFO, "SkuController: getSkus");
		if (skus == null)
			listSku();
		
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
		System.out.println(this.skuIdTarget);
	}

	public String getTargetSkuBarCode() {
		return targetSkuBarCode;
	}

	public void setTargetSkuBarCode(String targetSkuBarCode) {
		this.targetSkuBarCode = targetSkuBarCode;
	}

	public List<Manufacturer> getMfrs() {
		listMfr();
		Collections.sort(mfrs);
		return mfrs;
	}

	public void setMfrs(List<Manufacturer> mfrs) {
		this.mfrs = mfrs;
	}

	public List<Category> getCategories() {
		listCategories();
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
