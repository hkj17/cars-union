package com.nbicc.cu.carsunion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan
public class CarsUnionApplication {

	public static void main(String[] args) {
		SpringApplication.run(CarsUnionApplication.class, args);
	}
}
