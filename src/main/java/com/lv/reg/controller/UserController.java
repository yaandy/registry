package com.lv.reg.controller;

import com.lv.reg.dao.CountryDAO;
import com.lv.reg.dao.CustomerRepository;
import com.lv.reg.entities.Customer;
import com.lv.reg.entities.User;
import com.lv.reg.formBean.CustomerUserForm;
import com.lv.reg.formBean.CustomerUserFormValidator;
import com.lv.reg.formBean.UserForm;
import com.lv.reg.model.Country;
import com.lv.reg.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
@RequestMapping(path = "/user")
public class UserController {
    @Autowired
    private IUserService userService;

    @GetMapping(path = "/all")
    public String getAllCustomers(Model model) {
        Iterable<User> allUsers = userService.getAllUsers();
        UserForm userForm = new UserForm();
        model.addAttribute("users", allUsers);
        model.addAttribute("userForm", userForm);

        return "usersPage";
    }



}
