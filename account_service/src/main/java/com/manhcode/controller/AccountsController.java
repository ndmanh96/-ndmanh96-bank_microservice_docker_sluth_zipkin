/**
 * 
 */
package com.manhcode.controller;



import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.manhcode.AccountsServiceConfig;
import com.manhcode.feignclient.CardsFeignClient;
import com.manhcode.feignclient.LoansFeignClient;
import com.manhcode.model.Accounts;
import com.manhcode.model.Cards;
import com.manhcode.model.Customer;
import com.manhcode.model.CustomerDetails;
import com.manhcode.model.Loans;
import com.manhcode.model.Properties;
import com.manhcode.repository.AccountsRepository;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;

@RestController
public class AccountsController {
	
	private static final Logger logger = LoggerFactory.getLogger(AccountsController.class);
	
	@Autowired
	private AccountsRepository accountsRepository;

	@Autowired
	private AccountsServiceConfig accountsConfig;
	
	@Autowired
	private CardsFeignClient cardsFeignClient;
	
	@Autowired
	private LoansFeignClient loansFeignClient;
	@PostMapping("/myAccount")
	public Accounts getAccountDetails(@RequestHeader("eazybank-correlation-id") String correlationid, @RequestBody Customer customer) {

		Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId());
		if (accounts != null) {
			return accounts;
		} else {
			return null;
		}

	}
	
	@GetMapping("/account/properties")
	public String getPropertyDetails() throws JsonProcessingException {
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		Properties properties = new Properties(accountsConfig.getMsg(), accountsConfig.getBuildVersion(),
				accountsConfig.getMailDetails(), accountsConfig.getActiveBranches());
		String jsonStr = ow.writeValueAsString(properties);
		return jsonStr;
	}
	
	@PostMapping("/myCustomerDetails")
	//@CircuitBreaker(name = "detailsFocusCustomerSupportApp", fallbackMethod = "myCustomDetailFallback")
	@Retry(name = "retryForCustomerDetails", fallbackMethod = "myCustomDetailFallback")
	public CustomerDetails myCustomerDetails(@RequestHeader("eazybank-correlation-id") String correlationid, @RequestBody Customer customer) {
		logger.info("myCustomerDetails() method started");
		Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId());
		List<Loans> loans = loansFeignClient.getLoans(correlationid, customer);
		List<Cards> cards = cardsFeignClient.getCards(correlationid, customer);

		CustomerDetails customerDetails = new CustomerDetails();
		customerDetails.setAccounts(accounts);
		customerDetails.setLoans(loans);
		customerDetails.setCards(cards);
		logger.info("myCustomerDetails() method ended");
		return customerDetails;

	}
	
	private CustomerDetails myCustomDetailFallback(@RequestHeader("eazybank-correlation-id") String correlationid, Customer customer, Throwable t) {
		Accounts accounts = accountsRepository.findByCustomerId(customer.getCustomerId());
		List<Loans> loans = loansFeignClient.getLoans(correlationid, customer);
		CustomerDetails customerDetails = new CustomerDetails();
		customerDetails.setAccounts(accounts);
		customerDetails.setLoans(loans);
		return customerDetails;

	}
	
	@GetMapping("/sayHello")
	@RateLimiter(name = "sayHello", fallbackMethod = "sayHelloFallback")
	public String sayHello() {
		return "Hello, Welcome to EazyBank";
	}

	private String sayHelloFallback(Throwable t) {
		return "Hi, Welcome to EazyBank";
	}

}
