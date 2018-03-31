package com.app.exceptions;

public class BeyondLimitException extends Exception {

	private int excededlimit;
	
	public BeyondLimitException(int excededlimit) {
		this.excededlimit=excededlimit;
	}
	
	public int getExcededAmount() {
		return excededlimit;
	}
	
}