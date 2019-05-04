package com.lv.reg.formBean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserForm {
    private Long id;
    private String userName;
    private String firstName;
    private String lastName;
    private String password;
    private String confirmPassword;
    private String phoneNumber;
    private String authority;
}
