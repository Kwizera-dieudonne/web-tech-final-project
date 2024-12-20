package com.furniture.furniture_management_system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.furniture.repository")
@EntityScan(basePackages = "com.furniture.domain")
@ComponentScan(basePackages = "com.furniture")
public class FurnitureManagementSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(FurnitureManagementSystemApplication.class, args);
	}

}
