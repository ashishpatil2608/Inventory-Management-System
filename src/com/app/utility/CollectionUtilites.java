package com.app.utility;

import java.util.*;

import com.app.product.DurableProducts;
import com.app.product.ProductIntf;

public class CollectionUtilites {

	private static Map<Integer, ProductIntf> products = new HashMap<>(); 

	public static Map<Integer, ProductIntf> populateProducts()
	{
		//new DurableProducts(productid, productname, instock, price, reorderpoint, limit, reorderamount, orderedamount)
		products.put(10, new DurableProducts(10, "Bigcola", 6, 55.55f, 5,10,5, 0));
		products.put(11, new DurableProducts(11, "Sprite", 8, 78.92f, 5, 10,4, 0));
		products.put(12, new DurableProducts(12, "Nachos", 4, 90.00f, 3, 10, 3,3));
		products.put(13, new DurableProducts(13, "Ferrero-rocher", 4, 200.00f,7, 10,3,0));
		products.put(14, new DurableProducts(14, "Coca-Cola", 7, 35.00f, 4, 10, 6, 0));
		products.put(15, new DurableProducts(15, "Cheetos", 6, 10.00f, 5, 10, 5, 0));
		products.put(16, new DurableProducts(16, "Coritos", 5, 20.00f,4, 10, 4, 0));
		products.put(17, new DurableProducts(17, "Thumbsup", 10, 35.00f,5, 10,3,0));
		return products;
	}
	
}
