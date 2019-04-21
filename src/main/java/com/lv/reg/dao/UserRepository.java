package com.lv.reg.dao;

import com.lv.reg.entities.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
    User findUserByUsername(String userName);
}
