package com.paywithmybank.ncl.freemarker;

public class NCL {
	com.paywithmybank.ncl.NCL ncl = new com.paywithmybank.ncl.NCL();
	
	public Module getModule(String name) {
		return new Module (ncl.getModule(name));
	}
}
