package com.lv.reg.controller;

import com.lv.reg.dao.ContractRepository;
import com.lv.reg.entities.Contract;
import com.lv.reg.entities.Customer;
import com.lv.reg.formBean.CustomerUserForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping(path = "/contract")
public class ContractController {
    @Autowired
    private ContractRepository contractRepository;


    @GetMapping(path = "/all")
    public String getAllCustomers(Model model){
        Iterable<Contract> all = contractRepository.findAll();

        model.addAttribute("contracts", all);
        return "contractsPage";
    }
}
