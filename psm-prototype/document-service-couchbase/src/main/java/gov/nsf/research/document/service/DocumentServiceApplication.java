package gov.nsf.research.document.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableAutoConfiguration
@EnableScheduling
@EnableCaching
public class DocumentServiceApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(DocumentServiceApplication.class, args);
    }
    
    /**
     * Used when run as a WAR
     * 
     */
    protected SpringApplicationBuilder configure (SpringApplicationBuilder builder){
    	return builder.sources(DocumentServiceApplication.class);
    }
}
