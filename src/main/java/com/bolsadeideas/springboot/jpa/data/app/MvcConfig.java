package com.bolsadeideas.springboot.jpa.data.app;

//import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer{

	
	
	//private final Logger log = LoggerFactory.getLogger(getClass());
	/*@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		// TODO Auto-generated method stub
		WebMvcConfigurer.super.addResourceHandlers(registry);
		//LOS DOS ASTERISCOS ES PORQUE ES UN NOMBRE VARIABLE EN ESTE CASO DE IMAGEN
		String resourcePath = Paths.get("uploads").toAbsolutePath().toUri().toString();
		log.info(resourcePath);
		registry.addResourceHandler("/uploads/**").addResourceLocations(resourcePath);
	}
*/
	
}
