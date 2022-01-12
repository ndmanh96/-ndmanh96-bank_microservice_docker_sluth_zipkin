package com.manhcode.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.manhcode.model.Loans;

@Repository
public interface LoansRepository extends CrudRepository<Loans, Long> {

	
	public List<Loans> findByCustomerIdOrderByStartDtDesc(int customerId);

}
