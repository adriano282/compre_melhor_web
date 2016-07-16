package com.compremelhor.web.converter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter(value="localDateTimeConverter")
public class LocalDateTimeConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext context, UIComponent component,
			String value) {
		System.out.println("getAsObject: " + value);
		DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		return LocalDateTime.parse(value, fmt);
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component,
			Object value) {
		System.out.println("getAsString: " + value);
		DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
		return ((LocalDateTime)value).format(fmt);
	}

}
