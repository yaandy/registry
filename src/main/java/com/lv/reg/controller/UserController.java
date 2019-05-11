package com.lv.reg.controller;

import com.lv.reg.dao.AuthorityRepository;
import com.lv.reg.entities.Authority;
import com.lv.reg.entities.AuthorityType;
import com.lv.reg.entities.User;
import com.lv.reg.formBean.*;
import com.lv.reg.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
//        UserForm userForm = new UserForm();
        model.addAttribute("users", userService.getAllUsers());
//        model.addAttribute("userForm", userForm);
        model.addAttribute("roles", AuthorityType.values());

        return "userPage";
    }

    @GetMapping(path = "/register")
    public String registerUser(Model model) {
        UserForm userForm = new UserForm();
        model.addAttribute("userForm", userForm);
        model.addAttribute("roles", AuthorityType.values());

        return "userRegisterPage";
    }

    @RequestMapping(path = "/register", method = RequestMethod.POST)
    public String register(Model model, @ModelAttribute("userForm") @Validated UserForm appUserForm,
                           BindingResult result, final RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("users", userService.getAllUsers());
            model.addAttribute("errorMessage", generateErrorMsg(result.getFieldErrors()));
            model.addAttribute("roles", AuthorityType.values());
            return "userRegisterPage";
        }
        User user = null;
        try {
            Authority authority = authorityRepository.findByName(AuthorityType.valueOf(appUserForm.getAuthority()));
            user = new User(appUserForm, authority);
            userService.saveUser(user);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Error: " + e.getMessage());
            return "userRegisterPage";
        }

        redirectAttributes.addFlashAttribute("user", user);
        return "redirect:/user/all";
    }

    @RequestMapping(value = "/{id}/update", method = RequestMethod.GET)
    public String updateUser(@PathVariable("id") long id, Model model) {
        User actualUser = userService.findUserById(id);
        model.addAttribute("userForm", new UserForm());
        model.addAttribute("user", actualUser);
        model.addAttribute("currentRole", actualUser.getAuthorities().stream().findFirst().get().getName());
        model.addAttribute("roles", AuthorityType.values());
        return "userUpdate";
    }

    @RequestMapping(path = "/{id}/update", method = RequestMethod.POST)
    public String updateUserById(@ModelAttribute("userForm") UserForm userForm, Model model, //
                                 @PathVariable("id") long id, //
                                 final RedirectAttributes redirectAttributes) {
        if (userForm.getPassword().equals(userForm.getConfirmPassword())) {
            User user = userService.updateUser(userForm, id);
            redirectAttributes.addFlashAttribute("user", user);
            return "redirect:/user/all";
        }else {
            redirectAttributes.addFlashAttribute("errorMessage", "Password does not match, leave it empty if want change");
            return "redirect:/user/"+id+"/update";
        }
    }


    private String generateErrorMsg(List<FieldError> errorList) {
        return "Form validation errors occured, please check and fix -> " + errorList.stream().map(el -> "Field error: " + el.getObjectName() + ": " + el.getField()).collect(Collectors.joining(","));
    }

}
