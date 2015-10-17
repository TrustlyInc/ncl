package com.paywithmybank.ncl.loader;

import com.paywithmybank.ncl.model.Source;

public class ClassModuleLoader implements ModuleLoader {
	Class<?> loader;
	String path;
	
	public ClassModuleLoader() {
		this.loader = this.getClass();
		this.path = "/";
	}
	
	public ClassModuleLoader(Class<?> loader, String path) {
		this.loader = loader;
		this.path = path;
	}

	@Override
	public Source openModule(String name) {
		String fileName = path + name + ".ncl";
		return new Source (fileName , loader.getResourceAsStream(fileName));
	}
	
	
}
