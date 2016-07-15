package com.compremelhor.web.controller;

import java.awt.Event;
import java.io.Serializable;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import org.primefaces.context.RequestContext;

import com.compremelhor.model.entity.Account;
import com.compremelhor.model.entity.Sku;
import com.compremelhor.model.entity.Stock;
import com.compremelhor.model.service.StockService;
import com.compremelhor.web.util.JSFUtil;

@ManagedBean
@ViewScoped
public class StockController implements Serializable {
	private static final long serialVersionUID = 1L;

	@Inject
	private StockService stService;
	
	private List<Stock> stocks;
	private List<Stock> filteredStocks;
	private Stock stockTarget;

	public StockController() {}
	
	@PostConstruct
	public void init() {
		refreshStocks();
	}
	
	private void refreshStocks() {
		Account ac;
		if ((ac = JSFUtil.getLoggedUser()) != null) {
			Predicate<Stock> onlyFromPartnerUser = s -> s.getSkuPartner().getPartner().getId() == ac.getPartner().getId();
			
			stocks = stService.findAll()
					.stream()
					.filter(onlyFromPartnerUser)
					.filter(s -> s.getSkuPartner().getSku().getStatus().equals(Sku.Status.PUBLICADO))
					.collect(Collectors.toList());
		}
	}
	
	public void editStock() {
		try {
			stService.edit(stockTarget);
			refreshStocks();
		} catch (Exception e) {
			JSFUtil.addErrorMessage("stock.changed.error");
			System.out.println("Error: " +e.getMessage());
			return;
		}
		JSFUtil.addInfoMessage("stock.changed.successfully");
	}
	
	public void onRowSelected(Event event) {
		RequestContext.getCurrentInstance()
		.update("form");
		if (stockTarget.getId() != 0) {
			RequestContext				
				.getCurrentInstance()
				.execute("PF('stockFormDlg').show();");
		}
	}
	
	public List<Stock> getStocks() { return stocks; }
	public void setStocks(List<Stock> stocks) { this.stocks = stocks; }

	public Stock getStockTarget() {
		return stockTarget;
	}

	public void setStockTarget(Stock stockTarget) {
		this.stockTarget = stockTarget;
	}

	public List<Stock> getFilteredStocks() {
		return filteredStocks;
	}

	public void setFilteredStocks(List<Stock> filteredStocks) {
		this.filteredStocks = filteredStocks;
	}
}
