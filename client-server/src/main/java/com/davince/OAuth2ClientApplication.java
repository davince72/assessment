package com.davince;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.davince.model.AccountRepository;
import com.davince.model.CustomerRepository;
import com.davince.model.TranLogRepository;


@SpringBootApplication
public class OAuth2ClientApplication {

	@Autowired
	CustomerRepository repository;
	@Autowired
	AccountRepository accountRepository;
	@Autowired
	TranLogRepository tranlogRepository;
    public static void main(String[] args) {
        SpringApplication.run(OAuth2ClientApplication.class, args);
    }

}