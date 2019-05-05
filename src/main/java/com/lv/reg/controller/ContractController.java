package com.lv.reg.controller;

import com.lv.reg.dao.*;
import com.lv.reg.entities.Contract;
import com.lv.reg.entities.Customer;
import com.lv.reg.enums.RegionEnum;
import com.lv.reg.formBean.ContractForm;
import com.lv.reg.service.ContractService;
import com.lv.reg.service.CustomerService;
import com.lv.reg.service.IUserService;
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

import static org.apache.commons.lang3.StringUtils.isEmpty;

@Slf4j
@RequestMapping(path = "/contract")
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Controller
public class ContractController {

    private ContractService contractService;
    private IUserService userService;
    private CustomerService customerService;

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

        Contract contract = null;
        if(isCustomerInfoValid(contractForm, redirectAttributes))
            contract = contractService.saveContract(contractForm, principal);

        redirectAttributes.addFlashAttribute("contracts", contractService.findAll());
        redirectAttributes.addFlashAttribute("newContract", contract);

        return "redirect:/contract/all";
    }


    @RequestMapping(value = "/{id}/update", method = RequestMethod.GET)
    public String showUpdateUserForm(@PathVariable("id") long id, Model model) {

        Contract contract = contractService.findById(id);
        model.addAttribute("contract", contract);
        model.addAttribute("updatedContractForm", new ContractForm());
        model.addAttribute("statusOptions", statusRepository.findAll());
        model.addAttribute("stagesOptions", stageRepository.findAll());
        model.addAttribute("employee", userService.getAllUsers());

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

    @RequestMapping(path = "/{id}/close", method = RequestMethod.POST)
    public String closeContract(@PathVariable("id") long id, final RedirectAttributes redirectAttributes){
        contractService.closeContract(id);
        redirectAttributes.addAttribute("contracts", contractService.findAll());
        return "redirect:/contract/all";
    }

    private boolean isCustomerInfoValid(ContractForm contractForm, RedirectAttributes redirectAttributes){
        if(contractForm.getCustomerId() == null || contractForm.getCustomerId() < 0){
            if(! isNewCustomerAttributesPresent(contractForm)){
                redirectAttributes.addFlashAttribute("customerError", "Customer was not selected and some fields required for new customer creation are empty, please try again");
                return false;
            }else {
                Customer newCustomer = customerService.quickCustomerCreation(contractForm.getCustomerFirstName(), contractForm.getCustomerLastName(), contractForm.getCustomerPhone());
                contractForm.setCustomerId(newCustomer.getId());
                return true;
            }
        }else
            return true;
    }

    private boolean isNewCustomerAttributesPresent(ContractForm contractForm){
        if(isEmpty(contractForm.getCustomerFirstName()) || isEmpty(contractForm.getCustomerLastName()) || isEmpty(contractForm.getCustomerPhone())){
            return false;
        }else {
            return true;
        }
    }
    
}

