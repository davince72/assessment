package com.davince.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.davince.model.Account;
import com.davince.model.AccountRepository;
import com.davince.model.AccountUI;
import com.davince.model.TranLogRepository;
import com.davince.model.TransactionLog;

@RestController
public class AccountController {
	private static String CRLF = "\r\n";
	
	@Autowired
	AccountRepository accountRepository;
	@Autowired
	TranLogRepository tranlogRepository;
	
	@GetMapping("/createaccount")
	public String create() {
		// save a single Account
		try {
			accountRepository.save(new Account(getUsername()));
		} catch (DataIntegrityViolationException dex) {
			return "Account allready created";
		}

		return "Account is created";
	}

	@GetMapping("/findallaccounts")
	public List<AccountUI> findAll() {

		List<Account> accounts = accountRepository.findAll();
		List<AccountUI> accountUI = new ArrayList<>();

		for (Account account : accounts) {
			accountUI.add(new AccountUI(account.getUser(), account.getCash(), account.getCrypto()));
		}

		return accountUI;
	}

	@RequestMapping("/account/{id}")
	public String search(@PathVariable long id) {
		String account = "";
		account = accountRepository.findById(id).toString();
		return account;
	}
	
	@RequestMapping("/searchbyuser/{user}")
	public List<AccountUI> fetchDataByUser(@PathVariable String user) {

		Account account = accountRepository.findByUser(user);
		List<AccountUI> accountUI = new ArrayList<>();

		accountUI.add(new AccountUI(account.getUser(), account.getCash(), account.getCrypto()));

		return accountUI;
	}

	@GetMapping("/info")
	public String info() {

		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username;
		if (principal instanceof UserDetails) {
			username = ((UserDetails) principal).getUsername();
		} else {
			// Principal is in dit geval van type: DefaultOidcUser
			DefaultOidcUser user = (DefaultOidcUser) principal;
			username = user.getName();
			// username = principal.toString();
			// Name: [admin], Granted Authorities: [[ROLE_USER, SCOPE_openid]], User
			// Attributes: [{sub=admin, aud=[articles-client], azp=articles-client,
			// iss=http://auth-server:9000, exp=2021-10-17T15:28:29Z,
			// iat=2021-10-17T14:58:29Z, nonce=SqEynmKT26xyGPOZselUEc7BPiU5lp7MLZMLgHTnXio}]
		}
		StringBuilder sb = new StringBuilder();
		sb.append(username).append(CRLF);
		List<TransactionLog> findAll = tranlogRepository.findByAccount(accountRepository.findByUser(username));
		findAll.stream().forEach(t -> sb.append(t).append(CRLF));
		return sb.toString();
	}

	public String getUsername() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		DefaultOidcUser user = (DefaultOidcUser) principal;
		return user.getName();
	}

	@RequestMapping("/addcash/{money}")
	public String addcash(@PathVariable long money) {
		Account account = accountRepository.findByUser(getUsername());
		if (account == null) { 
			// create account first...login is taken care off
			account = accountRepository.save(new Account(getUsername()));
		}
		BigDecimal moneyToAdd = new BigDecimal(money);
		account.setCash(account.getCash().add(moneyToAdd));
		TransactionLog tranlog = new TransactionLog(account, TransactionLog.ACTION_DEPOSIT , moneyToAdd , null);
		tranlogRepository.save(tranlog);
		return accountRepository.save(account).toString();
	}

	
}
