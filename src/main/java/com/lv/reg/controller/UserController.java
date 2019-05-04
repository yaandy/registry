package com.lv.reg.controller;

import com.lv.reg.dao.AuthorityRepository;
import com.lv.reg.dao.CountryDAO;
import com.lv.reg.dao.CustomerRepository;
import com.lv.reg.entities.Authority;
import com.lv.reg.entities.AuthorityType;
import com.lv.reg.entities.Customer;
import com.lv.reg.entities.User;
import com.lv.reg.formBean.*;
import com.lv.reg.model.Country;
import com.lv.reg.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping(path = "/user")
public class UserController {
    @Autowired
    private IUserService userService;
    @Autowired
    private UserValidator userValidator;
    @Autowired
    private AuthorityRepository authorityRepository;

    @InitBinder
    protected void initBinder(WebDataBinder dataBinder) {
        Object target = dataBinder.getTarget();
        if (target == null) {
            return;
        }
        if (target.getClass() == UserForm.class) {
            dataBinder.setValidator(userValidator);
        }
    }

    @GetMapping(path = "/all")
    public String getAllCustomers(Model model) {
        UserForm userForm = new UserForm();
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("userForm", userForm);
        model.addAttribute("roles", AuthorityType.values());

        return "usersPage";
    }

    @RequestMapping(path = "/register", method = RequestMethod.POST)
    public String register(Model model, @ModelAttribute("userForm") @Validated UserForm appUserForm,
                           BindingResult result, final RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {

            model.addAttribute("users", userService.getAllUsers());
            model.addAttribute("errorMessage", generateErrorMsg(result.getFieldErrors()));
            model.addAttribute("roles", AuthorityType.values());
            return "usersPage";
        }
        User user = null;
        try {
            Authority authority = authorityRepository.findByName(AuthorityType.valueOf(appUserForm.getAuthority()));
            user = new User(appUserForm, authority);
            userService.saveUser(user);
        }catch (Exception e){
            model.addAttribute("errorMessage", "Error: " + e.getMessage());
            return "usersPage";
        }

        redirectAttributes.addFlashAttribute("user", user);
        return "redirect:/user/all";
    }

    private String generateErrorMsg(List<FieldError> errorList){
        return  "Form validation errors occured, please check and fix -> " + errorList.stream().map(el -> "Field error: " + el.getObjectName() +": "+ el.getField()).collect(Collectors.joining(","));
    }

}
