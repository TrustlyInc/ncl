package com.paywithmybank.ncl.model;

import java.io.InputStream;

public class Source {
	String name;
	InputStream is;
	public Source(String name, InputStream is) {
		super();
		this.name = name;
		this.is = is;
		if(is == null){
			throw new RuntimeException("Could not find module named " + name);
		}
	}
	public String getName() {
		return name;
	}
	public InputStream getInputStream() {
		return is;
	}
}
