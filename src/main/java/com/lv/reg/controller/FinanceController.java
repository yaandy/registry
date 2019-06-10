package com.lv.reg.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping(path = "/finance")
public class FinanceController {


    @GetMapping(path = "/all")
    public String getAllCustomers(Model model) {

        return "financePage";
    }


}
