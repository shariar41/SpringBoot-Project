package com;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication(
		//		scanBasePackages = {"packages that are outside .com.example.demo"}
		)
@RestController
public class JavaFinalProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(JavaFinalProjectApplication.class, args);
	}

}
