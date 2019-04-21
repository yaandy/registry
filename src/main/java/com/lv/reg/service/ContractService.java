package com.lv.reg.service;

import com.lv.reg.dao.ContractRepository;
import com.lv.reg.dao.CustomerRepository;
import com.lv.reg.entities.AuthorityType;
import com.lv.reg.entities.Contract;
import com.lv.reg.entities.User;
import com.lv.reg.formBean.ContractForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.sql.Date;
import java.time.LocalDate;

@Service
public class ContractService {
    @Autowired
    private ContractRepository contractRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    @Qualifier("userDetailsService")
    private UserDetailsService userDetailsService;

    public void saveContract(ContractForm contractForm, Principal principal) {
        User user = ((MyUserDetails)userDetailsService.loadUserByUsername(principal.getName())).getUser();

        Contract contract = Contract.builder().customer(customerRepository.findById(contractForm.getCustomerId()).orElseThrow())
                .district(contractForm.getDistrict())
                .region(contractForm.getRegion())
                .registered(Date.valueOf(LocalDate.now()))
                .updated(Date.valueOf(LocalDate.now()))
                .orderStatus(contractForm.getStatus())
                .orderType(contractForm.getType())
                .stage(contractForm.getStage())
                .totalPrice(contractForm.getTotalPrice())
                .createdBy(user)
                .build();

        contractRepository.save(contract);
    }

    public void updateContract(ContractForm contractForm, long id) {
        Contract toBeUpdated = contractRepository.findById(id).orElseThrow();

        toBeUpdated.setFinished(contractForm.isFinished());
        toBeUpdated.setOrderStatus(contractForm.getStatus());
        toBeUpdated.setStage(contractForm.getStage());
        toBeUpdated.setPayedAmount(toBeUpdated.getPayedAmount() + contractForm.getPayedAmount());
        toBeUpdated.setUpdated(Date.valueOf(LocalDate.now()));

        contractRepository.save(toBeUpdated);
    }

    public Iterable<Contract> findAll() {
        return contractRepository.findAll();
    }

    public Iterable<Contract> findAllAvailableForUser(Principal principal) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
        boolean isAdmin = userDetails.getAuthorities().stream()
                .anyMatch(el -> el.getAuthority().equals(AuthorityType.ROLE_ADMIN.name()));
        if (isAdmin)
            return contractRepository.findAll();
        else
            return contractRepository.findContractByCreatedBy(((MyUserDetails)userDetails).getUser());
    }

    public Contract findById(long id) {
        return contractRepository.findById(id).orElseThrow();
    }

}
