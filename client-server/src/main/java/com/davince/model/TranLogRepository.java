package com.davince.model;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface TranLogRepository extends CrudRepository<TransactionLog, Long>{
	List<TransactionLog> findAll();
	
	List<TransactionLog> findByAccount(Account account);
}