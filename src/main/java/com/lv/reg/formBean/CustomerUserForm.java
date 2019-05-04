package com.lv.reg.formBean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerUserForm {
    private String orgName;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String region;
    private String adress;
}
