package com.lv.reg.controller;

import com.lv.reg.dao.*;
import com.lv.reg.entities.Contract;
import com.lv.reg.entities.Customer;
import com.lv.reg.entities.User;
import com.lv.reg.enums.RegionEnum;
import com.lv.reg.formBean.ContractForm;
import com.lv.reg.service.ContractService;
import com.lv.reg.service.MyUserDetails;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.Arrays;

@Slf4j
@RequestMapping(path = "/contract")
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Controller
public class ContractController {

    private ContractService contractService;
    private CustomerRepository customerRepository;
    private StatusRepository statusRepository;
    private StageRepository stageRepository;
    private ContractTypeRepository contractTypeRepository;

    @GetMapping(path = "/all")
    public String getAllCustomers(Model model, Principal principal){

        Iterable<Contract> contracts = contractService.findAllAvailableForUser(principal);
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
    public String addNewContract(@ModelAttribute("contractForm") ContractForm contractForm,
                                 BindingResult result,
                                 final RedirectAttributes redirectAttributes, Principal principal){

        contractService.saveContract(contractForm, principal);

        redirectAttributes.addAttribute("contracts", contractService.findAll());
        return "redirect:/contract/all";
    }


    @RequestMapping(value = "/{id}/update", method = RequestMethod.GET)
    public String showUpdateUserForm(@PathVariable("id") long id, Model model) {

        Contract contract = contractService.findById(id);
        model.addAttribute("contract", contract);
        model.addAttribute("updatedContractForm", new ContractForm());
        model.addAttribute("statusOptions", statusRepository.findAll());
        model.addAttribute("stagesOptions", stageRepository.findAll());

        return "contractUpdate";
    }

    @RequestMapping(path = "/{id}/update", method = RequestMethod.POST)
    public String updateContractById(@ModelAttribute("updatedContractForm") ContractForm contractForm, //
                                     @PathVariable("id") long id, //
                                     final RedirectAttributes redirectAttributes){
        contractService.updateContract(contractForm, id);
        redirectAttributes.addAttribute("contracts", contractService.findAll());
        return "redirect:/contract/all";
    }
}

