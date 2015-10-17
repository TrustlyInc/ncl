package com.paywithmybank.ncl.freemarker;

public class Module {
	com.paywithmybank.ncl.Module m;
	
	public Module (com.paywithmybank.ncl.Module m){
		this.m = m;
	}
	
	public TemplateNode getNode(String str){
		return new TemplateNode(m.getNode(str));
	}
}
