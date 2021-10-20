package com.davince.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.davince.model.Customer;
import com.davince.model.CustomerRepository;
 
 
@RestController
public class CustomerController {
	@Autowired
	CustomerRepository repository;
	
	@GetMapping("/bulkcreate")
	public String bulkcreate(){
		// save a single Customer
		repository.save(new Customer("Rajesh", "Bhojwani"));
		
		// save a list of Customers
        repository.saveAll(Arrays.asList(new Customer("Salim", "Khan")
                       , new Customer("Rajesh", "Parihar")
                       , new Customer("Rahul", "Dravid")
                       , new Customer("Dharmendra", "Bhojwani")));
		
		return "Customers are created";
	}
	@PostMapping("/create")
	public String create(@RequestBody Customer customer){
		// save a single Customer
		repository.save(new Customer(customer.getFirstName(), customer.getLastName()));

		return "Customer is created";
	}
	@GetMapping("/findall")
	public List<Customer> findAll(){

		List<Customer> customers = repository.findAll();
		return customers;
	}
	
	@RequestMapping("/search/{id}")
	public String search(@PathVariable long id){
		String customer = "";
		customer = repository.findById(id).toString();
		return customer;
	}
	
	@RequestMapping("/searchbyfirstname/{firstname}")
	public List<Customer> fetchDataByLastName(@PathVariable String firstname){
	
		List<Customer> customers = repository.findByFirstName(firstname);
		return customers;

	}
}
