/*
 * 
 * class is used to identify each product uniquely in Inventory
 * apart from product details such as product id, name, price;
 * QTY of the product in stock, Ordered QTY, Maximum QTY Allowed, 
 * Reorder point, QTY to be reordered when a reorder point is reached
 * for the product is kept in states
 * 
 */
	

package com.app.product;

import com.app.exceptions.BeyondLimitException;
import com.app.exceptions.NegativeReorderPointException;


public class DurableProducts implements ProductIntf {
	
	
	private int productid,instock;
	private float price;
	private int reorderpoint,limit;
	private int reorderamount,orderedamount;
	private String productname;
	
	
	public DurableProducts(int productid, String productname,int instock, float price, int reorderpoint, int limit, int reorderamount,
			int orderedamount) {
		super();
		this.productid = productid;
		this.productname=productname;
		this.instock = instock;
		this.price = price;
		this.reorderpoint = reorderpoint;
		this.limit = limit;
		this.reorderamount = reorderamount;
		this.orderedamount = orderedamount;
	}
	
	public DurableProducts(int productid) {
		this.productid=productid;
	}

	
	/*
	 * Getters and Setters
	 */
	
	@Override
	public int getProductid() {
		return productid;
	}

	
	@Override
	public void setProductid(int productid) {
		this.productid = productid;
	}

	
	@Override
	public String getProductName() {
		return productname;
	}
	
	
	@Override
	public void setProductName(String productname) {
		this.productname=productname;
	}
	
	
	@Override
	public float getPrice() {
		return price;
	}

	
	@Override
	public void setPrice(float price) {
		this.price = price;
	}

	
	@Override
	public int getOrderedAmount() {
		return orderedamount;
	}
	
	
	@Override
	public void setOrderedAmount(int amount) {
		orderedamount=amount;
	}
	
	
	@Override
	public float calculateTotalInStockPrice() {
		return price*instock;
	}

	
	@Override
	public int getInStock() {
		return instock;
	}

	
	@Override
	public void setStock(int amount) throws BeyondLimitException{
		if(instock>limit)
			throw new BeyondLimitException(instock-limit);
		this.instock=amount;
	}

	
	@Override
	public void setReorderPoint(int reorderpoint) throws BeyondLimitException,NegativeReorderPointException{
		if(reorderpoint>=limit)
			throw new BeyondLimitException((reorderpoint-limit)+1);
		else if(reorderpoint<0)
			throw new NegativeReorderPointException();
		else
			this.reorderpoint=reorderpoint;
	}

	
	@Override
	public int getReorderPoint() {
		return reorderpoint;
	}
	
	
	@Override
	public void setReorderAmount(int amount) throws BeyondLimitException{
			if(reorderpoint+amount>limit)
				throw new BeyondLimitException(reorderpoint+amount-limit);
			else reorderamount=amount;
				
	}
	
	
	@Override
	public int getReorderAmount() {
		return reorderamount;
	}
	
	
	@Override
	public void setLimit(int limit) {
		this.limit=limit;
	}
	
	
	@Override
	public int getLimit() {
		return limit;
	}

	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + productid;
		return result;
	}

	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof DurableProducts)) {
			return false;
		}
		DurableProducts other = (DurableProducts) obj;
		if (productid != other.productid) {
			return false;
		}
		return true;
	}

	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	
	@Override
	public String toString() {
		return "DurableProducts [productid=" + productid + "]";
	}
	
	
}
