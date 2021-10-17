package com.davince.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "tranlog")
public class TransactionLog implements Serializable {
 
	public static String ACTION_BUY = "BUY";
	public static String ACTION_SELL = "SELL";
	public static String ACTION_WITHDRAWAL = "WITHDRAWAL";
	public static String ACTION_DEPOSIT = "DEPOSIT";
	public static String SOURCE_STOCK = "STOCK";
	public static String SOURCE_EXCHANGE = "EXCHANGE";

	private static final long serialVersionUID = 378987730265562885L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "fk_account")
	private Account account;
	
	@Column(name = "sysdate")
	private LocalDateTime sysdate;
	
	@Column(name = "action")
	private String action;			// buy / sell / withdrawal / deposit
 
	@Column(name = "amount")
	private BigDecimal amount;		// number of coins

	@Column(name = "source")
	private String source;		// stock/exchange
 
	protected TransactionLog() {
	}
 
	public TransactionLog(Account account, String action, BigDecimal amount, String source) {
		this.account = account;
		this.action = action;
		this.amount = amount;
		this.source = source;
		this.sysdate=LocalDateTime.now();
	}
 
}