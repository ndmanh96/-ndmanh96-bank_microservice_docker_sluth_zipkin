package com.manhcode.feignclient;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.manhcode.model.Cards;
import com.manhcode.model.Customer;

@FeignClient(name = "cards")
@Component
public interface CardsFeignClient {
	@PostMapping(value = "myCards", consumes = "application/json")
	public List<Cards> getCards(@RequestHeader("eazybank-correlation-id") String correlationid, @RequestBody Customer customer);
}
