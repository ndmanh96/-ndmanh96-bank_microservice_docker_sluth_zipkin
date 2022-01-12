package com.manhcode.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.manhcode.model.Cards;

@Repository
public interface CardsRepository extends CrudRepository<Cards, Long> {

	
	public List<Cards> findByCustomerId(int customerId);

}
