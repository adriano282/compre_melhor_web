package com.compremelhor.web.controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.inject.Inject;

import com.compremelhor.model.entity.Stock;
import com.compremelhor.model.service.StockService;

@ManagedBean
@ConversationScoped
public class StockController implements Serializable {
	private static final long serialVersionUID = 1L;

	
	@ManagedProperty(value= "#{skuController}")
	private SkuController skuController;
	
	public void setSkuController(SkuController skuController) {
		this.skuController = skuController;
	}
	
	@Inject
	private Conversation conversation;
	
	@Inject
	private StockService stService;
	
	private List<Stock> stocks;
	private List<Stock> filteredStocks;
	private Stock stockTarget;

	public StockController() {}
	
	@PostConstruct
	public void init() {
		stocks = stService.findAll();
		start();
	}
	
	public String start() {
		if (conversation.isTransient())
			conversation.begin();
		
		return null;
	}
	
	public String finish() {
		conversation.end();
		return "/views/home.xthml?faces-redirect=true";
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
