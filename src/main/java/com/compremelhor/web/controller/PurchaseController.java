package com.compremelhor.web.controller;

import java.awt.Event;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;
import javax.inject.Inject;

import org.primefaces.event.TabChangeEvent;

import com.compremelhor.model.entity.Purchase;
import com.compremelhor.model.entity.Purchase.Status;
import com.compremelhor.model.entity.PurchaseLine;
import com.compremelhor.model.entity.PurchaseLog;
import com.compremelhor.model.entity.SyncronizeMobile;
import com.compremelhor.model.exception.InvalidEntityException;
import com.compremelhor.model.service.PurchaseLogService;
import com.compremelhor.model.service.PurchaseService;
import com.compremelhor.model.service.SyncronizeMobileService;
import com.compremelhor.web.util.JSFUtil;
import com.compremelhor.web.util.PurchaseFilters;

@ManagedBean
@ViewScoped
public class PurchaseController {
	
	@Inject private PurchaseLogService purchaseLogService;
	@Inject private PurchaseService purchaseService;
	@Inject private SyncronizeMobileService syncService;
	
	private List<Purchase> purchases;
	private List<Purchase> filteredPurchases;
	
	private List<PurchaseLine> lines;
	private Purchase purchaseTarget;
	
	
		
	public void onListPage() {
		purchases = purchaseService.findAll().stream().filter(PurchaseFilters.opened).collect(Collectors.toList());
		purchaseTarget = new Purchase();
	}
	
	public String edit() {
		try {
			purchaseService.edit(purchaseTarget);
			
			SyncronizeMobile sync = new SyncronizeMobile();
			sync.setEntityId(purchaseTarget.getId());
			sync.setEntityName("purchase");
			sync.setMobileUserIdRef(purchaseTarget.getUser().getId());
			sync.setAction(SyncronizeMobile.Action.EDITED);
			syncService.create(sync);
			
		} catch (InvalidEntityException e) {
			tryResolveErrorMessage(e, "purchase.changing.error");
			return "";
		}
		JSFUtil.addInfoMessage("purchase.status.altered.succssfully", true);
		return "list.xhtml?faces-redirect=true";
		
	}
	
	public void onTabChange(TabChangeEvent event) {
		purchases = purchaseService.findAll();
		switch(event.getTab().getId()) {
			case "paid":
				purchases = PurchaseFilters.filterPurchasesBiFunction.apply(purchases.stream(), PurchaseFilters.paid);
				break;
				
			case "opened":
				purchases = PurchaseFilters.filterPurchasesBiFunction.apply(purchases.stream(), PurchaseFilters.opened);
				
				break;
			
			case "separated":
				purchases = PurchaseFilters.filterPurchasesBiFunction.apply(purchases.stream(), PurchaseFilters.separated);
				break;
				
			case "shipped":
				purchases = PurchaseFilters.filterPurchasesBiFunction.apply(purchases.stream(), PurchaseFilters.shipped);
				break;
		}
	}
	
	public String daysAgoSinceLastUpdated() {
		Period p = Period.between(purchaseTarget.getLastUpdated().toLocalDate(), LocalDate.now());
		
		
		StringBuilder sb = new StringBuilder();
		sb.append(p.getDays() + " dia(s) e ");
		Duration d = Duration.between(purchaseTarget.getLastUpdated().toLocalTime(), LocalTime.now());
		
		
		long hours = d.getSeconds() >= 0L? ((d.getSeconds()/60) /60) : 0;
		
		sb.append(hours + " hora(s)");
		return sb.toString();
	}
	
	public void onViewPage() {
		String id = JSFUtil.getRequestParameter("id");
		Integer purchaseId = 0;
		try {
			purchaseId = Integer.valueOf(id);
		} catch (Exception e) {}
		
		purchaseTarget = purchaseService.find(purchaseId);
		System.out.println(purchaseTarget);
		lines = new ArrayList<>();
		lines = purchaseService.findAllItensByPurchase(purchaseTarget, true);
		System.out.println(lines);
	}
	
	public void onRowSelected(Event event) {
		System.out.println(purchaseTarget.getId());
		
	}
	
	public String openViewPage(int id) {
		return "details.xhtml?faces-redirect=true&id="+id;
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

	public List<PurchaseLine> getLines() {
		return lines;
	}

	public void setLines(List<PurchaseLine> lines) {
		this.lines = lines;
	}
	
	public SelectItem[] getStatus() {
		SelectItem[] items = new SelectItem[Status.values().length];
		
		int i = 0;
		for (Status s : Status.values()) {
			items[i++] = new SelectItem(s, JSFUtil.getBundleLabel(s.name()));
		}
		return items;
	}
	
	public List<PurchaseLog> getHistory() {
		return purchaseLogService.findAll().stream().filter( l -> l.getPurchaseId() == purchaseTarget.getId()).collect(Collectors.toList());
	}
}
