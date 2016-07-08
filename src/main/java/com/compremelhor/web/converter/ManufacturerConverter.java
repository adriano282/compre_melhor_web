package com.compremelhor.web.converter;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
import javax.inject.Inject;

import com.compremelhor.model.entity.Manufacturer;
import com.compremelhor.model.service.ManufacturerService;

@FacesConverter(value= "mfrConverter")
public class ManufacturerConverter implements Converter {

	@Inject ManufacturerService mfrService;
	
	@Override
	public Object getAsObject(FacesContext context, UIComponent component,
			String value) {
		if (value == null || value.trim().equals("")) return null;
		try {
			return Manufacturer.valueOf(value);
		} catch (NumberFormatException e ) {
			System.out.println(" " + e.getMessage());
			throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Erro de conversão", "Fabricante Inválido"));
		}
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component,
			Object value) {
		if (value == null) return "";
		return value.toString();
	}
}
