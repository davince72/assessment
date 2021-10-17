package com.davince;


import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.davince.model.AccountRepository;
import com.davince.model.CustomerRepository;
import com.davince.model.TranLogRepository;
import com.davince.rest.Root;


@SpringBootApplication
//@ComponentScan({"com.davince"})
public class OAuth2ClientApplication {
	private static final Logger log = LoggerFactory.getLogger(OAuth2ClientApplication.class);

	@Autowired
	CustomerRepository repository;
	@Autowired
	AccountRepository accountRepository;
	@Autowired
	TranLogRepository tranlogRepository;
    public static void main(String[] args) {
        SpringApplication.run(OAuth2ClientApplication.class, args);
    }
    
//    @Bean
//	public RestTemplate restTemplate(RestTemplateBuilder builder) {
//		return builder.build();
//	}
//
//	@Bean
//	public CommandLineRunner run(RestTemplate restTemplate) throws Exception {
//		return args -> {
//
//			ResponseEntity<Root[]> response = restTemplate.getForEntity(
//					"https://blox.weareblox.com/api/markets", Root[].class);
//			Root[] prices = response.getBody();
//			log.info(Arrays.toString(prices));
//		};
//	}

}