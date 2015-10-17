package com.paywithmybank.ncl.loader;

import com.paywithmybank.ncl.model.Source;

public interface ModuleLoader {
	Source openModule(String name); 
}
