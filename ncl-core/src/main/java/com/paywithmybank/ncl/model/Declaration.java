package com.paywithmybank.ncl.model;

public class Declaration {
	String name;
	Object value; //Number,
	
	
	public Declaration(String name, Object value) {
		super();
		this.name = name;
		this.value = value;
	}
	public String getName() {
		return name;
	}
	public Object getValue() {
		if(value instanceof Function){
			return ((Function)value).call();
		}
		return value;
	}
	
	
}
