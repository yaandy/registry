package com.lv.reg.controller;

import com.lv.reg.dao.CountryDAO;
import com.lv.reg.dao.CustomerRepository;
import com.lv.reg.entities.Customer;
import com.lv.reg.formBean.AppUserForm;
import com.lv.reg.formBean.CustomerUserForm;
import com.lv.reg.model.AppUser;
import com.lv.reg.model.Country;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping(path = "/demo")
public class DemoControler {
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private CountryDAO countryDAO;

    @GetMapping(path = "/all")
    public String getAllCustomers(Model model){
        Iterable<Customer> all = customerRepository.findAll();
        CustomerUserForm customerUserForm = new CustomerUserForm();
        model.addAttribute("appUserForm", customerUserForm);
        model.addAttribute("customers", all);
        return "customersPage";
    }

    // Show Register page.
    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String viewRegister(Model model) {

        CustomerUserForm form = new CustomerUserForm();
        List<Country> countries = countryDAO.getCountries();

        model.addAttribute("appUserForm", form);
        model.addAttribute("countries", countries);

        return "customerRegister";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String saveRegister(Model model, //
                               @ModelAttribute("appUserForm") @Validated Customer customerUserForm, //
                               BindingResult result, //
                               final RedirectAttributes redirectAttributes) {

        // Validate result
        if (result.hasErrors()) {
            List<Country> countries = countryDAO.getCountries();
            model.addAttribute("countries", countries);
            return "customerRegister";
        }
        Customer customer= null;
        try {
            customer = customerRepository.save(customerUserForm);
        }
        // Other error!!
        catch (Exception e) {
            List<Country> countries = countryDAO.getCountries();
            model.addAttribute("countries", countries);
            model.addAttribute("errorMessage", "Error: " + e.getMessage());
            return "customerRegister";
        }

        redirectAttributes.addFlashAttribute("customer", customer);

        return "redirect:/registerSuccessful";
    }
}
