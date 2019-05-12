package com.lv.reg.service;

import com.lv.reg.dao.CustomerRepository;
import com.lv.reg.entities.Customer;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    public Customer quickCustomerCreation(String fName, String lName, String phone){
        Customer customer = Customer.builder()
                .address("N/A")
                .email("N/A")
                .region("N/A")
                .firstName(fName)
                .lastName(lName)
                .phone(phone)
                .orgName(lName + "_" + fName + "_" + StringUtils.right(phone, 4))
                .build();
        return customerRepository.save(customer);
    }

    public Iterable<Customer> findAll(){
        return customerRepository.findAll();
    }
}
