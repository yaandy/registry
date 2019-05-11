package com.lv.reg.service;

import com.lv.reg.entities.User;
import com.lv.reg.formBean.UserForm;

public interface IUserService {
    Iterable<User> getAllUsers();
    User saveUser(User user);
    User updateUser(UserForm userForm, long id);
    User findUserByUserName(String userName);
    User findUserById(long id);

}
