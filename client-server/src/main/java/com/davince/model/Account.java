package com.davince.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.NumberFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "account")
public class Account implements Serializable {
 
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7261521431134259910L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
 
	@Column(name = "userid", unique=true)
	private String user;
 
	@Column(name = "cash")
	private BigDecimal cash;

	@Column(name = "crypto")
	private BigDecimal crypto;
 
	protected Account() {
	}
 
	public Account(String user) {
		this.user = user;
		this.cash = BigDecimal.ZERO;
		this.crypto = BigDecimal.ZERO;
	}
 
	@Override
	public String toString() {
		return String.format("Account[id=%d, user='%s', cash='%s', crypto='%s']", id, user, currencyFormat(cash), cryptoFormat(crypto));
	}
//	public String getUser() {
//        return user;
//    }
//    public void setUser(String user) {
//        this.user = user;
//    }
//    
//    public BigDecimal getCash() {
//		return cash;
//	}
//
//	public void setCash(BigDecimal cash) {
//		this.cash = cash;
//	}
//
//	public BigDecimal getCrypto() {
//		return crypto;
//	}
//
//	public void setCrypto(BigDecimal crypto) {
//		this.crypto = crypto;
//	}

	public String currencyFormat(BigDecimal n) {
        return NumberFormat.getCurrencyInstance().format(n);
    }
	public String cryptoFormat(BigDecimal n) {
        return NumberFormat.getNumberInstance().format(n);
    }
}