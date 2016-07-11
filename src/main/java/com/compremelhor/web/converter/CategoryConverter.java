package com.compremelhor.web.converter;


import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import com.compremelhor.model.entity.Category;

@FacesConverter(value="categoryConverter")
public class CategoryConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext context, UIComponent component,
			String value) {
	
		if (value == null || value.trim().equals("")) return null;
		System.out.println("VALOR:  " + value);
		try {
			return Category.fromName(value);
		} catch (NumberFormatException e ) {
			System.out.println(" " + e.getMessage());
			throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Erro de conversão", "Categoria Inválido"));
		}
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component,
			Object value) {
		if (value == null) return "";		
		value.toString();
		return null;
	}

}
