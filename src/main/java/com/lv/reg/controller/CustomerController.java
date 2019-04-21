package com.lv.reg.controller;

import com.lv.reg.dao.CountryDAO;
import com.lv.reg.dao.CustomerRepository;
import com.lv.reg.entities.Customer;
import com.lv.reg.formBean.CustomerUserForm;
import com.lv.reg.formBean.CustomerUserFormValidator;
import com.lv.reg.model.Country;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Slf4j
@Controller
@RequestMapping(path = "/demo")
public class CustomerController {
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private CountryDAO countryDAO;
    @Autowired
    private CustomerUserFormValidator customerUserFormValidator;

    @InitBinder
    protected void initBinder(WebDataBinder dataBinder) {
        Object target = dataBinder.getTarget();
        if (target == null) {
            return;
        }
        if (target.getClass() == CustomerUserForm.class) {
            dataBinder.setValidator(customerUserFormValidator);
        }
    }

    @GetMapping(path = "/all")
    public String getAllCustomers(Model model){
        Iterable<Customer> all = customerRepository.findAll();
        CustomerUserForm form = new CustomerUserForm();
        CustomerUserForm customerUserForm = new CustomerUserForm();
        model.addAttribute("appUserForm", customerUserForm);
        model.addAttribute("customers", all);
        model.addAttribute("appUserForm", form);
        return "customersPage";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String saveRegister(Model model, //
                               @ModelAttribute("appUserForm") @Validated CustomerUserForm customerUserForm, //
                               BindingResult result, //
                               final RedirectAttributes redirectAttributes) {

        // Validate result
        if (result.hasErrors()) {
            List<Country> countries = countryDAO.getCountries();
            model.addAttribute("countries", countries);
            model.addAttribute("errorMessage", "Error occured on form validation. Customer was not created.");
            model.addAttribute("customers", customerRepository.findAll());
            return "customersPage";
        }
        Customer customer= null;
        try {
            customer = customerRepository.save(new Customer(customerUserForm));
        }
        // Other error!!
        catch (Exception e) {
            List<Country> countries = countryDAO.getCountries();
            model.addAttribute("countries", countries);
            model.addAttribute("errorMessage", "Error: " + e.getMessage());
            return "customersPage";
        }

        redirectAttributes.addFlashAttribute("customer", customer);

        return "redirect:/demo/all";
    }
}
