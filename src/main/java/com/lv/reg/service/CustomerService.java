package com.lv.reg.service;

import com.lv.reg.dao.CustomerRepository;
import com.lv.reg.entities.Customer;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

    public void delete(long id){
        Optional<Customer> customer = customerRepository.findById(id);
        if(customer.isPresent()){
            if(customer.get().getContracts().size() == 0)
                customerRepository.delete(customer.get());
            else
                throw new IllegalStateException("Cusotmer has some assigned contracts please remove them first bacause their will be corrupted");
        }

    }

    public Optional<Customer> findById(long id){
        return customerRepository.findById(id);
    }
}
