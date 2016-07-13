package com.compremelhor.web.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import com.compremelhor.model.entity.Category;
import com.compremelhor.model.exception.InvalidEntityException;
import com.compremelhor.model.service.CategoryService;
import com.compremelhor.web.util.JSFUtil;

@ManagedBean
@ViewScoped
public class CategoryController {
	
	@Inject
	private CategoryService categoryService;
	
	private List<Category> categories;
	private List<Category> filteredCategories;
	private Category categoryTarget;
	
	@PostConstruct
	public void init() {
		categoryTarget = new Category();
//		filteredCategories = new ArrayList<>();
		categories = categoryService.findAll();
	}
	
	public void delete(Integer id) {
		if (id != null) {
			Category ca = categoryService.find(id);
			
			try {
				categoryService.remove(ca);
				categories = categoryService.findAll();
			} catch (Exception e) {
				JSFUtil.addErrorMessage("category.has.sku.associated.error");
				return;
			}
			JSFUtil.addInfoMessage("category.removed.successfully");
		}
	}
	
	public String create() {
		try {
			categoryService.create(categoryTarget);
		} catch (InvalidEntityException e) {
			tryResolveErrorMessage(e, "category.regiter.error");
			return "";
		}
		JSFUtil.addInfoMessage("category.created.succssfully",true);
		return "list?faces-redirect=true";
	}
	
	public String edit() {
		try {
			categoryService.edit(categoryTarget);
		} catch (InvalidEntityException e) {
			tryResolveErrorMessage(e, "category.change.error");
			return "";
		}
		JSFUtil.addInfoMessage("category.changed.successfully", true);
		return "list?faces-redirect=true";
	}
	
	public String onEditPage() {
		String categoryId = JSFUtil.getRequestParameter("id");
		Integer id;
		try {
			System.out.println("ID AQUI: " +categoryId);
			id = Integer.valueOf(categoryId);
		} catch (Exception e) {
			e.printStackTrace();
			return "list?faces-redirect=true";
		}
		categoryTarget = categoryService.find(id);
		if (categoryTarget == null) {
			JSFUtil.addErrorMessage("category.not.found.error");
		}
		return "";
	}
	
	public String openEditForm(Integer categoryId) {
		System.out.println("CHGLD:JG:LKJ:LKFJG:LKJ");
		return "edit?faces-redirect=true&id="+categoryId;
	}
	
	public List<Category> getCategories() {
		return categories;
	}
	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}
	public List<Category> getFilteredCategories() {
		return filteredCategories;
	}
	public void setFilteredCategories(List<Category> filteredCategories) {
		this.filteredCategories = filteredCategories;
	}
	public Category getCategoryTarget() {
		return categoryTarget;
	}
	public void setCategoryTarget(Category categoryTarget) {
		this.categoryTarget = categoryTarget;
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
	
}
