package com.app.manager;

import java.util.*;

import com.app.product.ProductIntf;

public interface ManagerIntf {

	public int autoReorderProduct(ProductIntf product);
	public void reorderProduct(ProductIntf product,int amount);
	public List<ProductIntf> getAllProducts();
	public Map<ProductIntf,String> StockUp(Map<ProductIntf,Integer> amountforeachproduct);
	public Map<ProductIntf, String> confirmAllReorderProducts(Map<ProductIntf, Integer> confirmedamountforeachproduct);
	public Map<ProductIntf,String> Stockout(Map<ProductIntf,Integer> amountforeachproduct);
	
}
