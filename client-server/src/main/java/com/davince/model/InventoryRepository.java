package com.davince.model;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface InventoryRepository extends CrudRepository<Inventory, Long>{
	List<Inventory> findAll();
	
}