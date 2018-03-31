package com.app.exceptions;

public class NoSufficientStockException extends Exception{
	
	private int insufficientamount;
	
	public NoSufficientStockException(int insufficientamount) {
		this.insufficientamount=insufficientamount;
	}
	
	public int getInsufficientamount() {
		return insufficientamount;
	}
	
}
