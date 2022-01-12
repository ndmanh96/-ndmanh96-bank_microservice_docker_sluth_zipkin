package com.manhcode.feignclient;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.manhcode.model.Cards;
import com.manhcode.model.Customer;
import com.manhcode.model.Loans;

@FeignClient(name = "loans")
@Component
public interface LoansFeignClient {
	@PostMapping(value = "myLoans", consumes = "application/json")
	public List<Loans> getLoans(@RequestHeader("eazybank-correlation-id") String correlationid, @RequestBody Customer customer);
}
