package com.nbicc.cu.carsunion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@ServletComponentScan
@EnableTransactionManagement
public class CarsUnionApplication {

	public static void main(String[] args) {
		System.setProperty("Log4jContextSelector", "org.apache.logging.log4j.core.async.AsyncLoggerContextSelector");
		SpringApplication.run(CarsUnionApplication.class, args);
	}
}
