package com.app.tester;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

import com.app.exceptions.BeyondLimitException;
import com.app.exceptions.DuplicateEntryException;
import com.app.exceptions.NegativeReorderPointException;
import com.app.inventory.InventoryForDurable;
import com.app.inventory.InventoryIntf;
import com.app.manager.InventoryManager;
import com.app.manager.ManagerIntf;
import com.app.pojo.Stat;
import com.app.product.DurableProducts;
import com.app.product.ProductIntf;
import com.app.utility.CollectionUtilites;

public class Tester {

	public static void main(String[] args) throws IOException {
		
		/*
		 * Populating Inventory with products
		 * 
		 * @see com.app.Utility.CollectionUtilities#populateProducts()
		 * 
		 */
		
		InventoryIntf inventory =  new InventoryForDurable(CollectionUtilites.populateProducts());
		InventoryManager manager = new InventoryManager(inventory);
		Tester testobject = new Tester();
		int choice=0;
		
		try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))){
			while(choice!=9)
			{
				try{
					System.out.println("*****************************************");
				System.out.println("1 -> Display all Products\n2 -> Display Inventory Stat\n3 -> Add new Product to Inventory"
						+ "\n4 -> Delete product from Inventory\n5 -> Stock out products from Inventory"
						+ "\n6 -> Move Ordered products to Inventory stock\n7 -> Increase Stock amount of products"
						+ "\n8 -> Change Rorderpoint for products\n9 -> Exit");
					System.out.println("*****************************************");
				choice=Integer.parseInt(br.readLine());
				switch (choice) {
				case 1: testobject.displayAllProducts(manager);
					break;
				case 2:	testobject.displayStat(inventory);
					break;
				case 3:	testobject.addProductToInventory(inventory);
					break;
				case 4:	testobject.removeProductFromInventory(inventory);
					break;
				case 5:	testobject.removeProductsFromInventory(manager);
					break;
				case 6:	testobject.addConfirmedOrdersToStock(manager);
					break;
				case 7:	testobject.addProductsToStock(manager);
					break;
				case 8:	testobject.changeReorderPoint(inventory);
					break;
				case 9:	
					break;
				default:System.out.println("Enter valid optioin");
					break;
				}
				
				}
				catch(Exception e)
				{
					System.out.println("plz retry with proper inputs");
				}
			}
		}


	}
	
	
	public void displayAllProducts(ManagerIntf manager) {
		
		System.out.println("******************** Products in Inventory ********************");
		for(ProductIntf products:manager.getAllProducts())
		{
			System.out.println("ID : "+products.getProductid()+"\nName : "+products.getProductName()+"\nIn Stock QTY : "+products.getInStock()+"\nPrice(Single Product) : "+products.getPrice()+
					"\nReorder Point : "+products.getReorderPoint()+"\nMaximum QTY allowed : "+products.getLimit()+"\nHow much to Reorder : "+products.getReorderAmount()+"\nOrdered QTY : "+products.getOrderedAmount());
			System.out.println();
		}
		
	}
	
	public void removeProductsFromInventory(ManagerIntf manager) {
		
		Map<ProductIntf,Integer> data = new HashMap<>();
		Map<ProductIntf,String> report = new HashMap<>();
		Scanner scanner = new Scanner(System.in);
		String temp;
		String[] array;
		System.out.println("Enter Product id and Amount to be removed from stock"
				+ "\nEx. 17 200Enter"
				+ "\nPress Q to Finish");
		
		while(true)
		{
			temp=scanner.nextLine();
			if(temp.equals("Q") || temp.equals("q"))
				break;
			array=temp.split(" ");
			data.put(new DurableProducts(Integer.parseInt(array[0])),Integer.parseInt(array[1]));
		}
		report=manager.Stockout(data);
		
		for(ProductIntf product : report.keySet())
		{
			System.out.println("Product id : "+product.getProductid()+"\tStatus : "+report.get(product));
		}
	}
	
	public void addConfirmedOrdersToStock(ManagerIntf manager) {
	
		Map<ProductIntf,Integer> data = new HashMap<>();
		Map<ProductIntf,String> report = new HashMap<>();
		Scanner scanner = new Scanner(System.in);
		String temp;
		String[] array;
		System.out.println("Enter amount to add to stock, enter product id and QTY"
				+ "\nEx. 17 200Enter"
				+ "\nPress Q to Finish");
		
		while(true)
		{
			temp=scanner.nextLine();
			if(temp.equals("Q") || temp.equals("q"))
				break;
			array=temp.split(" ");
			data.put(new DurableProducts(Integer.parseInt(array[0])),Integer.parseInt(array[1]));
		}
		report=manager.confirmAllReorderProducts(data);
		
		for(ProductIntf product : report.keySet())
		{
			System.out.println("Product id : "+product.getProductid()+"\tStatus : "+report.get(product));
		}
		
		
	}
	
	public void addProductToInventory(InventoryIntf inventory) throws IOException {

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		ProductIntf temp = null;
		try {
			System.out.println("Fill Product details");
			System.out.println("Enter product id : ");
			temp = new DurableProducts(Integer.parseInt(br.readLine()));
			System.out.println("Enter Name");
			temp.setProductName(br.readLine());
			System.out.println("Enter price");
			temp.setPrice(Float.parseFloat(br.readLine()));
			System.out.println("Enter Limit");
			temp.setLimit(Integer.parseInt(br.readLine()));
			System.out.println("Enter In Stock Amount");
			temp.setStock(Integer.parseInt(br.readLine()));
			System.out.println("Enter Reorder point - QTY of product when reached to or below this point, this product will be reordered");
			temp.setReorderPoint(Integer.parseInt(br.readLine()));
			System.out.println("Amount to Order when stock QTY reaches reorder point");
			temp.setReorderAmount(Integer.parseInt(br.readLine()));
			System.out.println("Enter Ordered QTY for this product");
			temp.setOrderedAmount(Integer.parseInt(br.readLine()));
			inventory.addNewProduct(temp);
			System.out.println("Added Product Successfully");
			if(temp.getReorderPoint()>=temp.getInStock())
			{
				String input=null;
				System.out.println("Since Product has Reached to reorder point \n do you want to reorder this product\n press y or n");
				input=br.readLine();
				if(input.equals("y") || input.equals("Y"))
				{
					temp.setOrderedAmount(temp.getReorderAmount());
					System.out.println("Product ordered with QTY "+temp.getOrderedAmount());
				}
			}
		} catch (BeyondLimitException e) {
			System.out.println("Instock Amount or Reorder amount exceded by " + e.getExcededAmount());
		} catch (DuplicateEntryException e) {
			System.out.println("Product already exists with this id");
		} catch (NegativeReorderPointException e) {
			System.out.println("Reoder point should be either 0 or positive");
		}
	}
	
	public void removeProductFromInventory(InventoryIntf inventory) {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter Product id");
		if(inventory.removeProduct(new DurableProducts(scanner.nextInt()))!=null)
		{
			System.out.println("Product removed from inventory");
		}
		
	}
	
	public void addProductsToStock(ManagerIntf manager) {
		
		Map<ProductIntf,Integer> data = new HashMap<>();
		Map<ProductIntf,String> report = new HashMap<>();
		Scanner scanner = new Scanner(System.in);
		String temp;
		String[] array;
		System.out.println("To increase QTY of In Stock QTY, enter product id and QTY"
				+ "\nEx. 17 200Enter"
				+ "\nPress Q to Finish");
		
		while(true)
		{
			temp=scanner.nextLine();
			if(temp.equals("Q") || temp.equals("q"))
				break;
			array=temp.split(" ");
			data.put(new DurableProducts(Integer.parseInt(array[0])),Integer.parseInt(array[1]));
		}
		report=manager.StockUp(data);
		
		for(ProductIntf product : report.keySet())
		{
			System.out.println("Product id : "+product.getProductid()+"\tStatus : "+report.get(product));
		}
		
		
	}
	
	public void displayStat(InventoryIntf inventory) {
		
		Stat stat = new Stat();
		stat=inventory.calculateStat();
		System.out.println("Inventory capacity : "+stat.getCapacity()+" / In Stock : "+stat.getTotalInStock());
		System.out.println("Free Space : "+stat.getFreespace()+" / Ordered Product : "+stat.getOrderedproducts());
		System.out.println("Free Space Percent : "+stat.getFreespacepercent()+" / Ordered Percent : "+stat.getOrderedpercent());
		System.out.println("Products : "+stat.getTotalProducts()+" / Total Product price : "+stat.getTotalPrice());
	}
	
	
	public void changeReorderPoint(InventoryIntf inventory) {
		
		Map<ProductIntf,Integer> data = new HashMap<>();
		Map<ProductIntf,String> report = new HashMap<>();
		Scanner scanner = new Scanner(System.in);
		String temp;
		String[] array;
		System.out.println("Enter Product id and Reorder point to be changed"
				+ "\nEx. 17 200Enter"
				+ "\nPress Q to Finish");
		
		while(true)
		{
			temp=scanner.nextLine();
			if(temp.equals("Q") || temp.equals("q"))
				break;
			array=temp.split(" ");
			data.put(new DurableProducts(Integer.parseInt(array[0])),Integer.parseInt(array[1]));
		}
		
		ProductIntf tempproduct=null;
		for(ProductIntf product : data.keySet())
		{
			try{
				
			tempproduct=inventory.getProduct(product);
			tempproduct.setReorderPoint(data.get(product));
			report.put(tempproduct,"Changed reorderpoint to "+tempproduct.getReorderPoint());
			
			}
			catch(NegativeReorderPointException e)
			{
				report.put(tempproduct,"plz enter positive reorder point");
			}
			catch(BeyondLimitException e)
			{
				report.put(tempproduct,"Reorder point exceded by "+e.getExcededAmount());
			}
		}
		
		for(ProductIntf product : report.keySet())
		{
			System.out.println("Product id  : "+product.getProductid()+" Status : "+report.get(product));
		}
	}

}