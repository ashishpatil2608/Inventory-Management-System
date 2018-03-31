package com.app.inventory;

import java.util.List;

import com.app.exceptions.BeyondLimitException;
import com.app.exceptions.DuplicateEntryException;
import com.app.exceptions.NoSufficientStockException;
import com.app.pojo.Stat;
import com.app.product.ProductIntf;

public interface InventoryIntf {
	
	public Stat calculateStat();
	public ProductIntf addNewProduct(ProductIntf product) throws DuplicateEntryException;
	public ProductIntf removeProduct(ProductIntf product);
	public ProductIntf getProduct(ProductIntf product);
	public List<ProductIntf> getAllProducts();
	public boolean addStock(ProductIntf product,int amount) throws BeyondLimitException;
	public boolean removeStock(ProductIntf product,int amount) throws NoSufficientStockException;
}
