package com.f1tekkz.springrestfulapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class SpringRestfulApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringRestfulApiApplication.class, args);
	}

	@GetMapping("/hello")
	public String sayHello(@RequestParam(value = "myName", defaultValue = "F1Tekkz") String name) {
		return String.format("Hello, %s!", name);
	}

}