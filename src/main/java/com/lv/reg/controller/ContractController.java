package com.lv.reg.controller;

import com.lv.reg.dao.*;
import com.lv.reg.entities.Contract;
import com.lv.reg.entities.Customer;
import com.lv.reg.enums.RegionEnum;
import com.lv.reg.formBean.ContractForm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Arrays;

@Slf4j
@Controller
@RequestMapping(path = "/contract")
public class ContractController {
    @Autowired
    private ContractRepository contractRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private StatusRepository statusRepository;
    @Autowired
    private StageRepository stageRepository;
    @Autowired
    private ContractTypeRepository contractTypeRepository;

    @GetMapping(path = "/all")
    public String getAllCustomers(Model model){
        Iterable<Contract> contracts = contractRepository.findAll();
        Iterable<Customer> customers = customerRepository.findAll();
        ContractForm contractForm = new ContractForm();



        model.addAttribute("contracts", contracts);
        model.addAttribute("customers", customers);
        model.addAttribute("contractForm", contractForm);
        model.addAttribute("regionOptions", Arrays.asList(RegionEnum.CHMELNYTSKIY, RegionEnum.LVIV, RegionEnum.VOLYN, RegionEnum.TERNOPIL));
        model.addAttribute("districtOptions", Arrays.asList("Самбірський", "СтароСамбірський", "Бузький", "Дрогобицький"));
        model.addAttribute("typeOptions", contractTypeRepository.findAll());
        model.addAttribute("statusOptions", statusRepository.findAll());
        model.addAttribute("stagesOptions", stageRepository.findAll());

        return "contractsPage";
    }


    @RequestMapping(path = "/register", method = RequestMethod.POST)
    public String addNewContract(Model model, //
                                 @ModelAttribute("contractForm") ContractForm contractForm, //
                                 BindingResult result, //
                                 final RedirectAttributes redirectAttributes){
        log.info(contractForm.toString());
        Contract contract = Contract.builder().customer(customerRepository.findById(contractForm.getCustomerId()).orElseThrow())
                .district(contractForm.getDistrict())
                .region(contractForm.getRegion())
                .updated(Date.valueOf(LocalDate.now()))
                .orderStatus(contractForm.getStatus())
                .orderType(contractForm.getType())
                .stage(contractForm.getStage())
                .totalPrice(contractForm.getTotalPrice())
                .build();

        contractRepository.save(contract);
        return "redirect:/contract/all";
    }


    @RequestMapping(value = "/{id}/update", method = RequestMethod.GET)
    public String showUpdateUserForm(@PathVariable("id") long id, Model model) {

        Contract contract = contractRepository.findById(id).orElseThrow();
        model.addAttribute("contract", contract);
        model.addAttribute("statusOptions", statusRepository.findAll());
        model.addAttribute("stagesOptions", stageRepository.findAll());

        return "contractUpdate";
    }
}
