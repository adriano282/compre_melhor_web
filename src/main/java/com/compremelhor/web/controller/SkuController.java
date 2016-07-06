package com.compremelhor.web.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.validation.constraints.Pattern;

import com.compremelhor.model.entity.Sku;
import com.compremelhor.model.entity.Sku.Status;
import com.compremelhor.model.entity.Sku.UnitType;
import com.compremelhor.model.exception.InvalidEntityException;
import com.compremelhor.model.service.ManufacturerService;
import com.compremelhor.model.service.SkuService;
import com.compremelhor.web.validator.BarCode;

@ManagedBean
@SessionScoped
public class SkuController implements Serializable {
	private static final long serialVersionUID = 1L;

	@Inject
	private SkuService skuService;
	@Inject 
	private ManufacturerService mfrService;
	
	private Integer skuIdTarget;
	
	@Pattern(regexp="^\\d{13}$",
			message = "O código de barras precisa conter 13 dígitos")
	@BarCode(message = "Código de Barras inválido")
	private String targetSkuBarCode;
	
	private Sku skuTarget;
	private List<Sku> skus;
	private List<SelectItem> mfrs;
	
	
	public SkuController() {
		skuTarget = new Sku();
		System.out.println("Constructed");
	}
	
	public UnitType[] getUnitTypeValues() {
		return UnitType.values();
	}
	
	public void listSku() {
		skus = skuService.findAll();
	}
	
	public void listMfr() {
		mfrs =  new ArrayList<>();
		
		
		mfrs = mfrService
				.findAll()
				.stream()
				.map(m -> {
					System.out.println(m.getName());
					SelectItem si = new SelectItem(m);
					si.setLabel(m.getName());
					return si;
				})
				.collect(Collectors.toList());
		
	}
	
	
	
	public String openEditForm(Sku sku) {
		
		Set<String> fetches = new HashSet<>();
		fetches.add("manufacturer");
		
		skuIdTarget = sku.getId();
		skuTarget = skuService.find(skuIdTarget, fetches);
		
		
		targetSkuBarCode = sku.getCode();
		System.out.println(sku.getName());
		return "form?redirect=true";
	}
	
	public void publishSku(Sku sku) {
		try {
			Sku s = skuService.find(sku.getId());
			
			if (s != null) {
				s.setStatus(Status.PUBLICADO);
				skuService.edit(s);
				listSku();
			}
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, 
							"Desculpe. Ocorreu um erro ao tentar publicar o produto", null));
			return;
		}
		
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, 
						"Produto publicado com sucesso!", null));
	}
	
	public String createSku() {
		skuTarget.setCode(targetSkuBarCode);
		try {
			skuService.create(skuTarget);
			
		} catch (InvalidEntityException e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, 
							"Desculpe. Ocorreu um erro ao tentar criar o produto", null));
			return null;
		}
		
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, 
						"Produto publicado com sucesso!", null));
		
		return "list";
	}
	
	public String editSku() {
		skuTarget.setCode(targetSkuBarCode);
		try {
			skuService.edit(skuTarget);
		} catch (InvalidEntityException e) {
			FacesContext.getCurrentInstance().addMessage(null, 
					new FacesMessage(FacesMessage.SEVERITY_INFO, 
							"Desculpe. Ocorreu um erro ao tentar salvar o produto", null));
			return null;
		}
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, 
						"Produto alterado com sucesso!", null));
		return "list";
	}
	
	
	public void deleteSku(Sku sku) {
		
		try {
			Sku s = skuService.find(sku.getId());
			
			if (s != null) {
				s.setStatus(Status.DELETADO);
				skuService.edit(s);
				listSku();
			}
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, 
							"Desculpe. Ocorreu um erro ao tentar excluir o produto", null));
			return;
		}
		
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, 
						"Produto excluído com sucesso!", null)); 
	}
	
	

	public List<Sku> getSkus() {
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

	public List<SelectItem> getMfrs() {
		return mfrs;
	}

	public void setMfrs(List<SelectItem> mfrs) {
		this.mfrs = mfrs;
	}
}
