package com.davince;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import com.davince.model.CustomerRepository;

@SpringBootApplication
//@ComponentScan({"com.davince"})
public class OAuth2ClientApplication {

	@Autowired
	CustomerRepository repository;
    public static void main(String[] args) {
        SpringApplication.run(OAuth2ClientApplication.class, args);
    }

}