package com.davince.model;

import java.math.BigDecimal;

public class AccountUI {

	private String user;
	private BigDecimal cash;
	private BigDecimal crypto;

	protected AccountUI() {
	}

//	public AccountUI(String user, ) {
//		this.user = user;
//		
//	}




	public AccountUI(String user, BigDecimal cash, BigDecimal crypto) {
		super();
		this.user = user;
		this.cash = cash;
		this.crypto = crypto;
	}

	public BigDecimal getCash() {
		return cash;
	}

	public void setCash(BigDecimal cash) {
		this.cash = cash;
	}

	public BigDecimal getCrypto() {
		return crypto;
	}

	public void setCrypto(BigDecimal crypto) {
		this.crypto = crypto;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String toString() {
		return String.format("Account[user='%s']", user);
	}
}
