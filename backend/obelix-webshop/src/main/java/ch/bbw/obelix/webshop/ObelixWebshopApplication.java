package ch.bbw.obelix.webshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "ch.bbw.obelix")
public class ObelixWebshopApplication {
//sneaky change for testing ci
	public static void main(String[] args) {
		SpringApplication.run(ObelixWebshopApplication.class, args);
	}

}
