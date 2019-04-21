package com.lv.reg.dao;

import com.lv.reg.entities.Contract;
import com.lv.reg.entities.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface ContractRepository extends CrudRepository<Contract, Long> {
    Collection<Contract> findContractByCreatedBy(User user);
}
