package com.ece.oath2.autherization;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
//
@SpringBootApplication
@RestController
@RequestMapping("test")
public class Oath2AutherizationServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(Oath2AutherizationServerApplication.class, args);
	}
	@GetMapping
	public String test() {
		return "test";
	}

}
