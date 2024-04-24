package com.paywithmybank.ncl;

import java.util.HashMap;
import java.util.Map;

import com.paywithmybank.ncl.loader.ClassModuleLoader;
import com.paywithmybank.ncl.loader.ModuleLoader;
import com.paywithmybank.ncl.model.Node;
import com.paywithmybank.ncl.model.Source;

public class NCL {

    final ModuleLoader loader;

    Map<String,Module> modules = new HashMap<>();

    public NCL() {
        loader = new ClassModuleLoader();
    }

    public NCL(String path) {
        loader = new ClassModuleLoader(ClassModuleLoader.class, path);
    }
	
	public synchronized Module loadModule(Source source) {
		Module module = new Module(source,this);
		
		modules.put(module.getName(),module);
		
		return module;
	}
	
	public synchronized Module loadModule(String source) {
		Module module = new Module(source,this);
		
		modules.put(module.getName(),module);
		
		return module;
	}
	
	public Module getModule(String name){
		Module module = modules.get(name);
		
		if(module == null){
			module = loadModule(loader.openModule(name));
		}
		
		return module;
	}

	public static void main(String[] args) {
		
		NCL ncl = new NCL();

		long start = System.currentTimeMillis();				
		//System.out.println(new Parser(code).parse().toString());
		
		Module module;
		module = ncl.getModule("fb");
		System.out.println("Load time: " + (System.currentTimeMillis() - start));
		start = System.currentTimeMillis();	
		Node node = module.getNode("page#select_bank");
		System.out.println("Apply rules time: " + (System.currentTimeMillis() - start) + "\n");
		System.out.println(node.toString());
	}
}
