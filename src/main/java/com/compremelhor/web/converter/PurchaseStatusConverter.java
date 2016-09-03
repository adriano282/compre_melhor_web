package com.compremelhor.web.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import com.compremelhor.model.entity.Purchase;
import com.compremelhor.model.entity.Purchase.Status;
import com.compremelhor.web.util.JSFUtil;

@FacesConverter(value="PurchaseStatusConverter")
public class PurchaseStatusConverter implements Converter{

	@Override
	public Object getAsObject(FacesContext context, UIComponent component,
			String value) {
		System.out.println("getAsObject: " + value);
		Status s = null;
		
		try {
			s = Purchase.Status.valueOf(value);
		} catch (Exception e) {
			switch(value) {
				case "COMPRA SEPARADA":
					s = Purchase.Status.PURCHASE_SEPARATED;
					break;
					
				case "TRANSAÇÃO INICIADA":
					s = Purchase.Status.STARTED_TRANSACTION;
					break;
					
				case "ENTREGUE":
					s = Purchase.Status.SHIPPED;
					break;
					
				case "PAGO":
					s = Purchase.Status.PAID;
					break;
				
				case "ABERTA":
					s = Purchase.Status.OPENED;
					break;
			}
		}

		return s;
			
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component,
			Object value) {
		System.out.println("getAsString: " + value);
		return JSFUtil.getBundleLabel(value.toString());		
	}

}
