package com.compremelhor.web.util;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.compremelhor.model.entity.Purchase;
import com.compremelhor.model.entity.Purchase.Status;

public class PurchaseFilters {
	public static Predicate<Purchase> paid = p -> p.getStatus() == Status.PAID;
	public static Predicate<Purchase> opened = p -> p.getStatus() == Status.OPENED;
	public static Predicate<Purchase> separated = p -> p.getStatus() == Status.PURCHASE_SEPARATED;
	public static Predicate<Purchase> shipped = p -> p.getStatus() == Status.SHIPPED;
	public static BiFunction<Stream<Purchase>, Predicate<Purchase>, List<Purchase>> filterPurchasesBiFunction = 
			(s, p) -> {
		
		if (s == null) {
			System.out.println("IS NULL");
			return new ArrayList<>();
		}
		return s.filter(p).collect(Collectors.toList());
	};				
	{
		paid = p -> p.getStatus() == Status.PAID;
		opened = p -> p.getStatus() == Status.OPENED;
		separated = p -> p.getStatus() == Status.PURCHASE_SEPARATED;
		shipped = p -> p.getStatus() == Status.SHIPPED;
	}
}
