package app.config;

import java.io.Writer;
import java.util.Map;

import org.javalite.activeweb.freemarker.AbstractFreeMarkerConfig;
import org.javalite.activeweb.freemarker.FreeMarkerTag;

import freemarker.template.Configuration;


public class FreeMarkerConfig extends AbstractFreeMarkerConfig {
    @Override
    public void init() {
        Configuration config = getConfiguration();

        //this is to override a strange FreeMarker default processing of numbers
        config.setNumberFormat("0.##");
        
        registerTag("currentTime", new FreeMarkerTag() {
			@Override
			protected void render(Map params, String body, Writer writer)
					throws Exception {
				writer.write(Long.toString(System.currentTimeMillis()));
				
			}
        });

    }
}
