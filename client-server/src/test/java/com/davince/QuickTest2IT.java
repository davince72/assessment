package com.davince;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import com.davince.controller.AccountController;
import com.davince.model.AccountRepository;
import com.davince.model.Inventory;
import com.davince.model.InventoryRepository;
import com.davince.model.TranLogRepository;
import com.davince.model.TransactionLog;
import com.davince.rest.Price;
import com.davince.rest.Root;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;;

@ActiveProfiles("integrationtest")
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class QuickTest2IT {
	@Autowired
	private MockMvc mvc;

	@SpyBean
	AccountController accountControllerMock;
	@Autowired
	AccountRepository accountRepository;
	@Autowired
	TranLogRepository tranlogRepository;
	@Autowired
	InventoryRepository inventoryRepository;

	ObjectMapper mapper = new ObjectMapper()
			.registerModule(new ParameterNamesModule())
		   .registerModule(new Jdk8Module())
		   .registerModule(new JavaTimeModule()); // new module, NOT JSR310Module

	@BeforeEach
	public void setup() {
		Inventory inventory = new Inventory(new BigDecimal(100), "BTC");
		inventoryRepository.save(inventory);
	}

	@WithMockUser(value = "spring")
	@Test
	public void givenMyName_shouldSucceedWith200() throws Exception {
		mvc.perform(get("/info").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(content().string("spring\r\n"));
	}

	@WithMockUser(value = "spring")
	@Test
	public void givenAuthRequestOnPrivateService_shouldSucceedWith200() throws Exception {

		// FIX BTC Price!
		Root btc = Root.builder().price(Price.builder().unit("BTC").amount(50000d).build()).build();
		when(accountControllerMock.getBTCPrice()).thenReturn(btc);

		// create account
		mvc.perform(get("/createaccount").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(content().string("Account is created"));

		// New user spring should begin with 0.00;
		mvc.perform(get("/searchbyuser/spring").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(content().json("{\"user\":\"spring\",\"cash\":0.00,\"crypto\":0.00}"));

		// Add $10
		mvc.perform(get("/addcash/10").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(content().json("{\"user\":\"spring\",\"cash\":10.00,\"crypto\":0.00}"));

		// Try to buy BTC ;-)
		mvc.perform(get("/buyBTC/100").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(content().string("Sorry you don't have enough money"));

		// Add serious money to buy BTC - $1000000
		mvc.perform(get("/addcash/1000000").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(content().json("{\"user\":\"spring\",\"cash\":1000010.00,\"crypto\":0.00}"));

		// Buy coins
		mvc.perform(get("/buyBTC/1").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(content().string("Your new coins are added to your account"));
		
		
		// check transactionlog through /info
		MvcResult result = mvc.perform(get("/tranlog").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				 .andDo(MockMvcResultHandlers.print())
				 .andReturn();
		
		String content = result.getResponse().getContentAsString();
		List<TransactionLog> tranlogs = Arrays.asList(mapper.readValue(content, TransactionLog[].class));
		
		assertEquals(3, tranlogs.size());
		assertEquals(TransactionLog.ACTION_DEPOSIT, tranlogs.get(0).getAction());
		assertEquals(1, tranlogs.get(0).getAccount().getCrypto().longValue());
		assertEquals(new BigDecimal("950010.00"), tranlogs.get(0).getAccount().getCash());    // prijs van BTC veranderd natuurlijk....dus niet te meten...mocken?
		assertEquals(TransactionLog.ACTION_DEPOSIT, tranlogs.get(1).getAction());

	}


}

