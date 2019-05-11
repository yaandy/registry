package com.lv.reg.service;

import com.lv.reg.dao.AuthorityRepository;
import com.lv.reg.dao.UserRepository;
import com.lv.reg.entities.Authority;
import com.lv.reg.entities.AuthorityType;
import com.lv.reg.entities.User;
import com.lv.reg.formBean.UserForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;

import javax.persistence.OneToOne;
import java.util.*;
import java.util.stream.Collectors;

@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService, IUserService {
    private static final Logger log = LoggerFactory.getLogger(UserDetailsServiceImpl.class);
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthorityRepository authorityRepository;

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
    public User updateUser(UserForm userForm, long id){
        User user = userRepository.findById(id).orElseThrow(()-> new RuntimeException("User not found"));
        List<String> authoritiesName = user.getAuthorities().stream().map(el -> el.getName().toString()).collect(Collectors.toList());
        if(! authoritiesName.contains(userForm.getAuthority())){
            Authority newAuth = authorityRepository.findByName(AuthorityType.valueOf(userForm.getAuthority()));
            user.setAuthorities(new HashSet<>(Arrays.asList(newAuth)));
        }
        if(!userForm.getPassword().isEmpty()){
            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
            String encoded = bCryptPasswordEncoder.encode(userForm.getPassword());
            user.setPassword(encoded);
        }

        return userRepository.save(user);
    }

    @Override
    public User findUserByUserName(String userName) {
        return userRepository.findUserByUsername(userName);
    }

    @Override
    public User findUserById(long id) {
        return userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("User not found in db"));
    }
}
