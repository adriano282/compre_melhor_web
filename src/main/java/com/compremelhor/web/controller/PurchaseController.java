package com.compremelhor.web.controller;

import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import com.compremelhor.model.entity.Purchase;
import com.compremelhor.model.service.PurchaseService;

@ManagedBean
@ViewScoped
public class PurchaseController {
	
	@Inject private PurchaseService purchaseService;
	
	private List<Purchase> purchases;
	private List<Purchase> filteredPurchases;
	
	private Purchase purchaseTarget;
	
	
	public void onListPage() {
		purchases = purchaseService.findAll();
		purchaseTarget = new Purchase();
	}
	
	
	
	
	
	
	
	
	
	
	
//	------------------ Getters and Setters ----------------------
	
	public List<Purchase> getPurchases() {
		return purchases;
	}
	public void setPurchases(List<Purchase> purchases) {
		this.purchases = purchases;
	}
	public List<Purchase> getFilteredPurchases() {
		return filteredPurchases;
	}
	public void setFilteredPurchases(List<Purchase> filteredPurchases) {
		this.filteredPurchases = filteredPurchases;
	}
	public Purchase getPurchaseTarget() {
		return purchaseTarget;
	}
	public void setPurchaseTarget(Purchase purchaseTarget) {
		this.purchaseTarget = purchaseTarget;
	}
}
