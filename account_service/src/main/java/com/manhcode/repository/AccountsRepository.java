package com.manhcode.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.manhcode.model.Accounts;



@Repository
public interface AccountsRepository extends CrudRepository<Accounts, Long> {

	public Accounts findByCustomerId(int customerId);

}
