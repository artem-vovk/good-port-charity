package com.charity.charity;



import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;


@SpringBootApplication(exclude={SecurityAutoConfiguration.class}) //remove brackets for enable default security config
public class CharityApplication {


	public static void main(String[] args) throws NullPointerException {
		SpringApplication.run(CharityApplication.class, args);


	}


}