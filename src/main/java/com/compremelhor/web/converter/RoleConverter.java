package com.compremelhor.web.converter;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;

import com.compremelhor.model.entity.Role;

@FacesConverter(value="roleConverter")
public class RoleConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext context, UIComponent component,
			String value) {
		if (value == null || value.trim().equals("")) return null;
		try {
			return Role.valueOf(value);
		} catch (Exception e ) {
			System.out.println(" Error" + e.getMessage());
			throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Erro de conversão", "Role Inválido"));
		}
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component,
			Object value) {
		if (value == null) return "";
		return value.toString();
	}

}
