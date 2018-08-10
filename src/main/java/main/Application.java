package main;

import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import mediaset.MediasetController;

@SpringBootApplication

public class Application {
	
	final static Logger logger = Logger.getLogger(Application.class);

    public static void main(String[] args) {
        Object[] sources = { Application.class, MediasetController.class };
        SpringApplication.run(sources, args);
        logger.debug("Application started!");
    }
}
