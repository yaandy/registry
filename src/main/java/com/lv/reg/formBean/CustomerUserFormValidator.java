package com.lv.reg.formBean;

import com.lv.reg.dao.AppUserDAO;
import com.lv.reg.dao.CustomerRepository;
import com.lv.reg.entities.Customer;
import com.lv.reg.model.AppUser;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.Collection;

@Component
public class CustomerUserFormValidator implements Validator {

    // common-validator library.
    private EmailValidator emailValidator = EmailValidator.getInstance();

    @Autowired
    private CustomerRepository customerRepository;

    // The classes are supported by this validator.
    @Override
    public boolean supports(Class<?> clazz) {
        return clazz == AppUserForm.class;
    }

    @Override
    public void validate(Object target, Errors errors) {
        CustomerUserForm customerUserForm = (CustomerUserForm) target;

        // Check the fields of AppUserForm.
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "NotEmpty.appUserForm.userName");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "NotEmpty.appUserForm.firstName");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "phone", "NotEmpty.appUserForm.lastName");

        if (!this.emailValidator.isValid(customerUserForm.getEmail())) {
            // Invalid email.
            errors.rejectValue("email", "Pattern.appUserForm.email");
        }
        if (!errors.hasFieldErrors("userName")) {
            Collection<Customer> allByEmail = customerRepository.findAllByEmail(customerUserForm.getEmail());
            if (!allByEmail.isEmpty()) {
                // Username is not available.
                errors.rejectValue("customerName", "Duplicate.appUserForm.userName");
            }
        }
    }

}