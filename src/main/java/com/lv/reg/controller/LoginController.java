package com.lv.reg.controller;

import com.lv.reg.entities.User;
import com.lv.reg.service.MyUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.support.SessionStatus;

import javax.servlet.http.HttpSession;

@Slf4j
@Controller
public class LoginController {

    // Login form
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login() {
        return "login";
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(SessionStatus session) {
        SecurityContextHolder.getContext().setAuthentication(null);
        session.setComplete();
        return "redirect:/login";
    }

    @RequestMapping(value = "/doLogin", method = RequestMethod.POST)
    public String postLogin(Model model, HttpSession session) {
        log.info("!!!!! doLogin  ()");
        // read principal out of security context and set it to session
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        validatePrinciple(authentication.getPrincipal());
        User loggedInUser = ((MyUserDetails) authentication.getPrincipal()).getUser();
        model.addAttribute("currentUser", loggedInUser.getUsername());
        session.setAttribute("userId", loggedInUser.getId());
        return "redirect:/";
    }

    private void validatePrinciple(Object principal) {
        if (!(principal instanceof MyUserDetails)) {
            throw new  IllegalArgumentException("Principal can not be null!");
        }
    }

    // Login form with error
    @RequestMapping("/login-error")
    public String loginError(Model model) {
        model.addAttribute("loginError", true);
        return "login";
    }

}
