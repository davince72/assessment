package com.davince.model;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface AccountRepository extends CrudRepository<Account, Long>{
	Account findByUser(String User);
	List<Account> findAll();
}