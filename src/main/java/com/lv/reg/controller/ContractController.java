package com.lv.reg.controller;

import com.lv.reg.dao.*;
import com.lv.reg.entities.Contract;
import com.lv.reg.entities.Customer;
import com.lv.reg.formBean.ContractForm;
import com.lv.reg.service.*;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.core.io.Resource;

import java.security.Principal;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isEmpty;

@Slf4j
@RequestMapping(path = "/contract")
@AllArgsConstructor(onConstructor = @__(@Autowired))
@Controller
public class ContractController {

    private ContractService contractService;
    private IUserService userService;
    private CustomerService customerService;
    private FilesStoringService filesStoringService;

    private DictionaryService dictionaryService;

    //    private CustomerRepository customerRepository;
    private StatusRepository statusRepository;
    private StageRepository stageRepository;
    private ContractTypeRepository contractTypeRepository;

    @GetMapping(path = "/all")
    public String getAllCustomers(@RequestParam(defaultValue = "false", required = false) boolean finished, @RequestParam(required = false, defaultValue = "true") boolean active, Model model, Principal principal) {

        Iterable<Contract> contracts = contractService.findAllAvailableForUser(principal, active, finished);
        model.addAttribute("contracts", contracts);
        model.addAttribute("stagesOptions", stageRepository.findAll());
        return "contractsPage";
    }

    @RequestMapping(path = "/register", method = RequestMethod.GET)
    public String openRegistrationForm(Model model) {

        populateCreateContarctModelWithDefaultParams(model);
        return "contractCreate";
    }

    @RequestMapping(path = "/register", method = RequestMethod.POST)
    public String addNewContract(@ModelAttribute("contractForm") ContractForm contractForm,
        BindingResult result,
        final RedirectAttributes redirectAttributes, Principal principal, Model model) {

        Contract contract = null;
        if (isCustomerInfoValid(contractForm, model)) {
            contract = contractService.saveContract(contractForm, principal);
            filesStoringService.saveFiles(contractForm, contract);

            redirectAttributes.addFlashAttribute("contracts", contractService.findAll());
            redirectAttributes.addFlashAttribute("newContract", contract);
        } else {
            populateCreateContarctModelWithDefaultParams(model);
            return "contractCreate";
        }

        return "redirect:/contract/all";
    }


    @RequestMapping(value = "/{id}/update", method = RequestMethod.GET)
    public String showUpdateUserForm(@PathVariable("id") long id, Model model) {

        Contract contract = contractService.findById(id);
        model.addAttribute("contract", contract);
        model.addAttribute("updatedContractForm", new ContractForm());
        model.addAttribute("employee", userService.getAllUsers());
        populateCreateContarctModelWithDefaultParams(model);

        model.addAttribute("files", filesStoringService.loadAll(contract).collect(Collectors.toMap(
            path -> MvcUriComponentsBuilder
                .fromMethodName(ContractController.class, "serveFile", contract.getId(), path.getFileName().toString())
                .build().toString(),
            path -> path.getFileName().toString())));

        return "contractUpdate";
    }

    @RequestMapping(path = "/{id}/update", method = RequestMethod.POST)
    public String updateContractById(@ModelAttribute("updatedContractForm") ContractForm contractForm, //
        @PathVariable("id") long id, //
        final RedirectAttributes redirectAttributes) {
        Contract updatedContract = contractService.updateContract(contractForm, id);
        filesStoringService.saveFiles(contractForm, updatedContract);
        redirectAttributes.addAttribute("contracts", contractService.findAll());
        return "redirect:/contract/all";
    }

    @RequestMapping(path = "/{id}/close", method = RequestMethod.GET)
    public String closeContract(@PathVariable("id") long id, final RedirectAttributes redirectAttributes) {
        contractService.closeContract(id);
        redirectAttributes.addAttribute("contracts", contractService.findAll());
        return "redirect:/contract/all";
    }

    private boolean isCustomerInfoValid(ContractForm contractForm, Model model) {
        if (contractForm.getCustomerId() == null || contractForm.getCustomerId() < 0) {
            if (!isNewCustomerAttributesPresent(contractForm)) {
                model.addAttribute("customerError",
                    "Customer was not selected and some fields required for new customer creation are empty, please try again");
                return false;
            } else {
                Customer newCustomer = customerService
                    .quickCustomerCreation(contractForm.getCustomerFirstName(), contractForm.getCustomerLastName(),
                        contractForm.getCustomerPhone());
                contractForm.setCustomerId(newCustomer.getId());
                return true;
            }
        } else {
            return true;
        }
    }

    private boolean isNewCustomerAttributesPresent(ContractForm contractForm) {
        if (isEmpty(contractForm.getCustomerFirstName()) || isEmpty(contractForm.getCustomerLastName()) || isEmpty(
            contractForm.getCustomerPhone())) {
            return false;
        } else {
            return true;
        }
    }

    private void populateCreateContarctModelWithDefaultParams(Model model) {
        model.addAttribute("contractForm", new ContractForm());
        model.addAttribute("ac_region", dictionaryService.regions());
        model.addAttribute("ac_district", dictionaryService.districts());
        model.addAttribute("ac_village", dictionaryService.villages());
        model.addAttribute("typeOptions", contractTypeRepository.findAll());
        model.addAttribute("statusOptions", statusRepository.findAll());
        model.addAttribute("stagesOptions", stageRepository.findAll());
        model.addAttribute("customers", customerService.findAll());
    }

}

