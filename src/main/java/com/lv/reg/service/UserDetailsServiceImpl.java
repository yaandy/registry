package com.lv.reg.service;

import com.lv.reg.dao.UserRepository;
import com.lv.reg.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService, IUserService {
    private static final Logger log = LoggerFactory.getLogger(UserDetailsServiceImpl.class);
    @Autowired
    private UserRepository userRepository;

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found.");
        }
        log.info("loadUserByUsername() : {}", username);
        return new MyUserDetails(user);
    }

    @Override
    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User saveUser(User user){
        return userRepository.save(user);
    }

    @Override
    public User findUserByUserName(String userName) {
        return userRepository.findUserByUsername(userName);
    }
}
