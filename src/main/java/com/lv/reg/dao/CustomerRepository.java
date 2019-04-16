package com.lv.reg.dao;

import com.lv.reg.entities.Customer;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;

public interface CustomerRepository extends CrudRepository<Customer, Integer> {
    Collection<Customer> findAllByEmail(String email);
}
