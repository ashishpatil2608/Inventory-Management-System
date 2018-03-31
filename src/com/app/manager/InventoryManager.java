/*
 * Manager performs autoreorder of products when reorder point is reached
 * Addition or removal of stock for multiple products
 * pings during autoreorder
 * Confirmed Reordered Products are added to current stock
 * Change Reorder point to multiple products
 * 
 */


package com.app.manager;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import com.app.exceptions.BeyondLimitException;
import com.app.exceptions.NoSufficientStockException;
import com.app.inventory.InventoryIntf;
import com.app.product.ProductIntf;

public class InventoryManager implements ManagerIntf {

	private	InventoryIntf inventory=null;
	
	public InventoryManager(InventoryIntf inventory) {
		this.inventory=inventory;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.app.manager.ManagerIntf#autoReorderProduct(com.app.product.ProductIntf)
	 * 
	 * Function performs Autoreorder of product
	 */
	
	@Override
	public int autoReorderProduct(ProductIntf product) {
		Scanner scanner = new Scanner(System.in);
		ProductIntf temp=inventory.getProduct(product);
		String option=null;
		if(temp.getInStock()<=temp.getReorderPoint() && temp.getOrderedAmount()==0)
		{
			System.out.println("Reorder point reached for product "+temp.getProductName()+" id : "+temp.getProductid());
			System.out.println("Order will be placed with QTY "+temp.getReorderAmount());
			System.out.println("press y/(any key) to place order/cancel reorder");
			option=scanner.nextLine();
			if(option.equals("y")){
				temp.setOrderedAmount(temp.getReorderAmount());
				return temp.getOrderedAmount();
			}
			return 0;
		}
		return 0;
	}

	
	@Override
	public void reorderProduct(ProductIntf product,int amount) {
		product.setOrderedAmount(product.getOrderedAmount()+amount);
	}

	@Override
	public List<ProductIntf> getAllProducts() {
		return inventory.getAllProducts();
	}

	
	/*
	 * (non-Javadoc)
	 * @see com.app.manager.ManagerIntf#StockUp(java.util.Map)
	 * 
	 * Increase stock for multiple products and return status
	 */
	
	@Override
	public Map<ProductIntf, String> StockUp(Map<ProductIntf, Integer> amountforeachproduct) {
		Map<ProductIntf, String> report = new HashMap<>();
			for(ProductIntf product: amountforeachproduct.keySet()){
				try {
					if(inventory.addStock(product,amountforeachproduct.get(product)))
						report.put(product,"Added "+amountforeachproduct.get(product)+" QTY");
					else
						report.put(product,"Failed to add");
				} catch (BeyondLimitException e) {
					report.put(product,"Failed to add, limit exceded by "+e.getExcededAmount());
				}
			}
		return report;
	}

	
	/*
	 * (non-Javadoc)
	 * @see com.app.manager.ManagerIntf#confirmAllReorderProducts(java.util.Map)
	 * 
	 * Confirmed Reordered amount is added to current stock and returns status
	 */
	
	@Override
	public Map<ProductIntf, String> confirmAllReorderProducts(Map<ProductIntf, Integer> confirmedAmountForEachProduct) {
		ConcurrentHashMap<ProductIntf, Integer> confirmedamountforeachproduct = new ConcurrentHashMap<>(confirmedAmountForEachProduct);
		ProductIntf temp=null;
		Map<ProductIntf, String> report = new HashMap<>();
		List<ProductIntf> removedProducts = new ArrayList<>();
		for(ProductIntf product:confirmedamountforeachproduct.keySet())
		{
			temp=inventory.getProduct(product);
			if(confirmedamountforeachproduct.get(product)>temp.getOrderedAmount())
			{
				removedProducts.add(product);
				confirmedamountforeachproduct.remove(product);
			}
		}
		report=StockUp(confirmedamountforeachproduct);
		for(ProductIntf product: removedProducts)
		{
			report.put(product,"Amount exceded more than actual ordered amount");
		}
	
		for(ProductIntf product: report.keySet())
		{
			if(report.get(product).contains("Added"))
			{
				temp=inventory.getProduct(product);
				temp.setOrderedAmount(temp.getOrderedAmount()-confirmedamountforeachproduct.get(product));
			}
		}
		
		return report;
	}

	
	/*
	 * (non-Javadoc)
	 * @see com.app.manager.ManagerIntf#Stockout(java.util.Map)
	 * 
	 * Decrease stock for multiple products and return status
	 */
	
	@Override
	public Map<ProductIntf, String> Stockout(Map<ProductIntf, Integer> amountforeachproduct) {
		Map<ProductIntf, String> report = new HashMap<>();
		for(ProductIntf product: amountforeachproduct.keySet()){
			try {
				if(inventory.removeStock(product,amountforeachproduct.get(product)))
				{	
					int orderedamt=autoReorderProduct(product);
					if(orderedamt>0)
						report.put(product,"Removed "+amountforeachproduct.get(product)+" QTY and Reordered "+orderedamt);
					else
						report.put(product,"Removed "+amountforeachproduct.get(product)+" QTY");
				}
				else
					report.put(product,"Failed to remove");
			} catch (NoSufficientStockException e) {
				report.put(product,"Failed to remove,Insufficient amount by "+e.getInsufficientamount());
			}
		}
	return report;
	}

}
