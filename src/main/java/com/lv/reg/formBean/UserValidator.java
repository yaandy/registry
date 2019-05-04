package com.lv.reg.formBean;

import com.lv.reg.entities.User;
import com.lv.reg.service.IUserService;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class UserValidator implements Validator {

    private EmailValidator emailValidator = EmailValidator.getInstance();

    @Autowired
    private IUserService userDetailsService;

    // The classes are supported by this validator.
    @Override
    public boolean supports(Class<?> clazz) {
        return clazz == UserForm.class;
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserForm userForm = (UserForm) target;

        // Check the fields of AppUserForm.
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "userName", "NotEmpty.appUserForm.userName");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "firstName", "NotEmpty.appUserForm.firstName");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "lastName", "NotEmpty.appUserForm.lastName");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "NotEmpty.appUserForm.password");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "confirmPassword", "NotEmpty.appUserForm.confirmPassword");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "phoneNumber", "NotEmpty.appUserForm.phone");

        if (!this.emailValidator.isValid(userForm.getUserName())) {
            // Invalid email.
            errors.rejectValue("userName", "Pattern.appUserForm.email");
        }
        if (userForm.getId() == null) {
            User dbUser = userDetailsService.findUserByUserName(userForm.getUserName());
            if (dbUser != null) {
                // Email has been used by another account.
                errors.rejectValue("userName", "Duplicate.appUserForm.email");
            }
        }
        if (!errors.hasErrors()) {
            if (!userForm.getConfirmPassword().equals(userForm.getPassword())) {
                errors.rejectValue("confirmPassword", "Match.appUserForm.confirmPassword");
            }
        }
    }

}