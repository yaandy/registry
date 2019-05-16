package com.lv.reg.controller;

import com.lv.reg.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping(path = "/ajax/customer")
@RestController()
public class CustomerRestController {
    @Autowired
    private CustomerService customerService;

    @RequestMapping(path = "/{id}/delete", method = RequestMethod.DELETE)
    @ResponseBody
    public String deleteCustomer(@PathVariable("id") long id) {
        customerService.delete(id);
        return "Deleted";
    }
}
