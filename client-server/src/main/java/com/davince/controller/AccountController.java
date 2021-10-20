package com.davince.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.davince.model.Account;
import com.davince.model.AccountRepository;
import com.davince.model.Inventory;
import com.davince.model.InventoryRepository;
import com.davince.model.TranLogRepository;
import com.davince.model.TransactionLog;
import com.davince.rest.Root;

@RestController
public class AccountController {
	private static String CRLF = "\r\n";

	@Autowired
	AccountRepository accountRepository;
	@Autowired
	TranLogRepository tranlogRepository;
	@Autowired
	InventoryRepository inventoryRepository;

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}

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
	public List<Account> findAll() {

		List<Account> accounts = accountRepository.findAll();
		return accounts;
	}

	@RequestMapping("/account/{id}")
	public Account search(@PathVariable long id) {
		return accountRepository.findById(id).orElse(null);
	}

	@RequestMapping("/searchbyuser/{user}")
	public Account fetchDataByUser(@PathVariable String user) {

		Account account = accountRepository.findByUser(user);
		return account;
	}

	@GetMapping("/info")
	public String info() {

		String username = getUsername();
		StringBuilder sb = new StringBuilder();
		sb.append(username).append(CRLF);
		List<TransactionLog> findAll = tranlogRepository.findByAccount(accountRepository.findByUser(username));
		findAll.stream().forEach(t -> sb.append(t).append(CRLF));
		return sb.toString();
	}
	@GetMapping("/tranlog")
	public List<TransactionLog>  tranlog() {

		String username = getUsername();
		List<TransactionLog> findAll = tranlogRepository.findByAccount(accountRepository.findByUser(username));
		return findAll;
	}

	private String getUsername() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username;
		if (principal instanceof UserDetails) {
			// tbv test wel makkelijk
			username = ((UserDetails) principal).getUsername();
		} else {
			// Principal is in dit geval van type: DefaultOidcUser
			DefaultOidcUser user = (DefaultOidcUser) principal;
			username = user.getName();
		}
		return username;
	}

	@RequestMapping("/addcash/{money}")
	public Account addcash(@PathVariable long money) {
		Account account = accountRepository.findByUser(getUsername());
		if (account == null) {
			// create account first...login is taken care off
			account = accountRepository.save(new Account(getUsername()));
		}
		BigDecimal moneyToAdd = new BigDecimal(money);
		account.setCash(account.getCash().add(moneyToAdd));
		TransactionLog tranlog = new TransactionLog(account, TransactionLog.ACTION_DEPOSIT, moneyToAdd, null);
		tranlogRepository.save(tranlog);
		return accountRepository.save(account);
	}

	// bg1.compareTo(bg2)
	// 0 = equal; 1 bg1>bg2 ; -1 bg1<bg2
	@RequestMapping("/buyBTC/{amount}")
	public String buyBTC(@PathVariable long amount) {
		Account account = accountRepository.findByUser(getUsername());
		if (account == null) {
			// create account first...login is taken care off
			account = accountRepository.save(new Account(getUsername()));
		}
		Root BTC = getBTCPrice();
		// calculate commision
		// => using the sellPrice? does that include the commission?
		// enough cash?
		BigDecimal coinsNeeded = BigDecimal.valueOf(amount);
		BigDecimal moneyNeeded = coinsNeeded.multiply(BigDecimal.valueOf(BTC.price.amount)); // commission?
		if (moneyNeeded.compareTo(account.getCash()) == 1) {
			// Not enough cash
			return "Sorry you don't have enough money";
		} else {
			// claim needed money asap
			account.setCash(account.getCash().subtract(moneyNeeded));
		}

		// if yes, go for it
		// inventory is sufficient?
		Optional<Inventory> inventoryOptional = inventoryRepository.findById(1L);
		if (inventoryOptional.isEmpty())
			throw new IllegalStateException("Inventory not present or set!");
		BigDecimal amountFromInventory = coinsNeeded;
		BigDecimal amountFromExchange = BigDecimal.ZERO;
		Inventory inventory = inventoryOptional.get();
		if (inventory.getAmount().compareTo(coinsNeeded) == -1) {
			// inventory not enough....
			amountFromInventory = inventory.getAmount(); // Use what's left in inventory
			inventory.setAmount(BigDecimal.ZERO); // All gone / sold out (again!)

			// TODO Refill Inventory
			// get rest of exchange
			amountFromExchange = coinsNeeded.subtract(amountFromInventory);
			TransactionLog tranlog = new TransactionLog(account, TransactionLog.ACTION_BUY, amountFromExchange,
					TransactionLog.SOURCE_EXCHANGE);
			tranlogRepository.save(tranlog);
		} else {
			inventory.setAmount(inventory.getAmount().subtract(coinsNeeded));
		}
		inventoryRepository.save(inventory);
		// create transaction log
		TransactionLog tranlog = new TransactionLog(account, TransactionLog.ACTION_BUY, amountFromInventory,
				TransactionLog.SOURCE_STOCK);
		tranlogRepository.save(tranlog);
		account.setCrypto(account.getCrypto().add(coinsNeeded)); // Nieuwe cryptos toevoegen aan account

		// ready
		accountRepository.save(account);
		return "Your new coins are added to your account";

		// return null;
	}

	public Root getBTCPrice() {
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<Root[]> response = restTemplate.getForEntity("https://blox.weareblox.com/api/markets",
				Root[].class);
		Root[] prices = response.getBody();
		// Determine price
		Root BTC = prices[0]; // a bit dirty..assume we have results
		return BTC;
	}
}
