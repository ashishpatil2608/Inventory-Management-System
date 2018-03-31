/*
 * Inventory allows to add or remove new products
 * Stock in and Stock out products
 * Calculate stats for inventory
 * 
 */

package com.app.inventory;

import java.util.*;

import com.app.exceptions.BeyondLimitException;
import com.app.exceptions.DuplicateEntryException;
import com.app.exceptions.NoSufficientStockException;
import com.app.pojo.Stat;
import com.app.product.ProductIntf;

public class InventoryForDurable implements InventoryIntf {

	private Stat stat;
	private Map<Integer, ProductIntf> products = new HashMap<>();

	/*
	 * Parameterized constructor is used for testing purpose to pre-populate the
	 * product details, Map is used to store product which is uniquely identified
	 * by its product id
	 */

	public InventoryForDurable(Map<Integer, ProductIntf> products) {
		this.products = products;
	}
	
	
	/*
	 * (non-Javadoc)
	 * @see com.app.inventory.InventoryIntf#addNewProduct(com.app.product.ProductIntf)
	 * 
	 * Add new product to inventory
	 */

	
	@Override
	public ProductIntf addNewProduct(ProductIntf product) throws DuplicateEntryException {
		if (products.containsKey(product.getProductid()))
			throw new DuplicateEntryException();
		else
			return products.put(product.getProductid(), product);
	}
	
	
	/*
	 * (non-Javadoc)
	 * @see com.app.inventory.InventoryIntf#removeProduct(com.app.product.ProductIntf)
	 * 
	 * Remove Product from inventory
	 */

	
	@Override
	public ProductIntf removeProduct(ProductIntf product) {
		return products.remove(product.getProductid()); // Consumer need to
														// perform null check
	}
	
	
	/*
	 * (non-Javadoc)
	 * @see com.app.inventory.InventoryIntf#getProduct(com.app.product.ProductIntf)
	 * 
	 * Get Single Product from Inventory
	 */

	@Override
	public ProductIntf getProduct(ProductIntf product) {
		return products.get(product.getProductid()); // Consumer need to perform
														// null check
	}

	
	/*
	 * (non-Javadoc)
	 * @see com.app.inventory.InventoryIntf#addStock(com.app.product.ProductIntf, int)
	 * 
	 * increase stock for product
	 */
	
	
	@Override
	public boolean addStock(ProductIntf product, int amount) throws BeyondLimitException {
		if (getProduct(product) != null) {
			ProductIntf temp = products.get(product.getProductid());
			int instock = temp.getInStock();
			int limit = temp.getLimit();
			if (instock + amount > limit)
				throw new BeyondLimitException(instock + amount - limit);
			else
				temp.setStock(instock + amount);
			return true;
		}
		return false;
	}
	
	
	/*
	 * (non-Javadoc)
	 * @see com.app.inventory.InventoryIntf#removeStock(com.app.product.ProductIntf, int)
	 * 
	 * decrease stock for product 
	 */

	@Override
	public boolean removeStock(ProductIntf product, int amount) throws NoSufficientStockException {
		if (getProduct(product) != null) {
			ProductIntf temp = products.get(product.getProductid());
			int instock = temp.getInStock();
			if (instock >= amount) {
				try {
					temp.setStock(instock - amount);
					return true;
				} catch (BeyondLimitException e) {
					return false;
				}
			} else
				throw new NoSufficientStockException(amount - instock);
		}
		return false;
	}

	
	@Override
	public List<ProductIntf> getAllProducts() {
		return new ArrayList<ProductIntf>(products.values());
	}

	
	/*
	 * (non-Javadoc)
	 * @see com.app.inventory.InventoryIntf#calculateStat()
	 * 
	 * Compute Capacity, In Stock QTY
	 * FreeSpacePercent, OrderedPercent
	 * Total products and Total Price
	 */
	
	@Override
	public Stat calculateStat() {

		float totalprice = 0, freespace = 0, freespacepercent = 0, orderedpercent = 0;
		int capacity = 0, totalInStock = 0;
		int totalproducts = 0, orderedproducts = 0;

		Stat stat = new Stat();

		for (ProductIntf product : products.values()) {
			totalprice += product.calculateTotalInStockPrice();
			totalInStock += product.getInStock();
			totalproducts++;
			orderedproducts += product.getOrderedAmount();
			capacity += product.getLimit();
			freespace += product.getLimit() - product.getInStock();
		}

		freespacepercent = (capacity - totalInStock) * 100.00f / capacity;
		orderedpercent = (new Float(orderedproducts) / capacity) * 100.00f;

		stat.setCapacity(capacity);
		stat.setFreespace(freespace);
		stat.setOrderedproducts(orderedproducts);
		stat.setFreespacepercent(freespacepercent);
		stat.setOrderedpercent(orderedpercent);
		stat.setTotalInStock(totalInStock);
		stat.setTotalPrice(totalprice);
		stat.setTotalProducts(totalproducts);

		return stat;
	}

}
