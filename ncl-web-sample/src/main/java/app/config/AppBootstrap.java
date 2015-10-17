package app.config;

import org.javalite.activeweb.AppContext;
import org.javalite.activeweb.Bootstrap;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class AppBootstrap extends Bootstrap {
	
	Injector injector;

	public void init(AppContext context) {        
		injector = Guice.createInjector();
		setInjector(injector);
    }

	@Override
	public void completeInit() {
		super.completeInit();
		
	}
}
