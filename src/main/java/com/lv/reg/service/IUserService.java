package com.lv.reg.service;

import com.lv.reg.entities.User;

public interface IUserService {
    Iterable<User> getAllUsers();
    User saveUser(User user);
    User findUserByUserName(String userName);
}
