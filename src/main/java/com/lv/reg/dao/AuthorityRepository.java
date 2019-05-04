package com.lv.reg.dao;

import com.lv.reg.entities.Authority;
import com.lv.reg.entities.AuthorityType;
import com.lv.reg.entities.Status;
import org.springframework.data.repository.CrudRepository;

public interface AuthorityRepository extends CrudRepository<Authority, Integer> {
    Authority findByName(AuthorityType authorityType);
}
