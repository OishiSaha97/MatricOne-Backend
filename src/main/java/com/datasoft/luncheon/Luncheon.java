package com.datasoft.luncheon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class Luncheon {

	
	public static void main(String[] args) {
		SpringApplication.run(Luncheon.class, args);
	}

}
