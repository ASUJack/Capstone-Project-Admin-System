package dev.capstone.asu.Capstone.Project.Admin.System;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class CapstoneProjectAdminSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(CapstoneProjectAdminSystemApplication.class, args);
	}

	@GetMapping("/hello")
	public String apiRoot()
	{
		return "Hello World";
	}
}
