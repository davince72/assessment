package com.davince.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "inventory")
public class Inventory implements Serializable {
 
	/**
	 * 
	 */
	private static final long serialVersionUID = 1977544530619439678L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(name = "amount")
	private BigDecimal amount;		// number of coins

	@Column(name = "coin")
	private String coin;		// type of coin

	protected Inventory() {
	}
 
	public Inventory(BigDecimal amount, String coin) {
		this.amount = amount;
		this.coin = coin;
	}
 
}