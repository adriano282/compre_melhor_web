package com.compremelhor.web.converter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter(value="localDateConverter")
public class LocalDateConverter implements Converter {
	@Override
	public Object getAsObject(FacesContext context, UIComponent component,
			String value) {
		
		String dateForParsing = value.substring(6) + "-" + value.substring(3, 5) + "-" + value.substring(0, 2);
		
		if (Integer.valueOf(value.substring(6)) > (Integer.valueOf(LocalDate.now().format(DateTimeFormatter.ofPattern("yy"))))) {
			dateForParsing = "19".concat(dateForParsing);
		} else {
			dateForParsing = "20".concat(dateForParsing);
		}
		
		System.out.println(dateForParsing);
		System.out.println(value);
		return LocalDate.parse(dateForParsing);
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component,
			Object value) {
		LocalDate dateValue;
		
		if (value instanceof LocalDateTime) {
			dateValue = ((LocalDateTime) value).toLocalDate();
		} else {
			dateValue = (LocalDate) value;
		}
		return dateValue
				.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
	}
}
