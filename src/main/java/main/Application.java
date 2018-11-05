package main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestTemplate;

import configuration.ConfigurationInfo;
import mediaset.MediasetController;
import mediaset.jobs.CacheMediasetProgramSectionsThread;

@SpringBootApplication
@ComponentScan(basePackageClasses= {ConfigurationInfo.class, MediasetController.class, CacheMediasetProgramSectionsThread.class})
public class Application {

	public static void main(String[] args) {
		
//		Class[] sources = { Application.class, MediasetController.class };
		SpringApplication.run(Application.class, args);
		
		RestTemplate rt = new RestTemplate();
		rt.getForObject("http://mediaset-api.herokuapp.com/prova", Void.class);
//		rt.getForObject("http://localhost:8080/prova", Void.class);
	}
	
	

}
