package com.app.product;
import com.app.exceptions.BeyondLimitException;
import com.app.exceptions.NegativeReorderPointException;

public interface ProductIntf {
	
	public int getProductid();
	public void setProductid(int productid);
	public String getProductName();
	public void setProductName(String productname);
	public float getPrice();
	public void setPrice(float price);
	float calculateTotalInStockPrice();
	int getInStock();
	void setStock(int amount) throws BeyondLimitException;
	void setReorderPoint(int reorderpoint) throws BeyondLimitException,NegativeReorderPointException;
	int getReorderPoint();
	void setLimit(int limit);
	int getLimit();
	void setReorderAmount(int amount) throws BeyondLimitException;
	int getReorderAmount();
	void setOrderedAmount(int amount);
	int getOrderedAmount();
}
