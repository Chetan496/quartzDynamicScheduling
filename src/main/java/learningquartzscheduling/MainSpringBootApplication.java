package learningquartzscheduling;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;



@SpringBootApplication

public class MainSpringBootApplication {
	
	private static final Logger logger = LoggerFactory.getLogger(MainSpringBootApplication.class);
	
    public static void main(String[] args) {
        SpringApplication.run(MainSpringBootApplication.class, args);
        logger.info("Application has started, Hello from Spring boot.. Hi!");
        
        
    }

	


   
}
