package com.lv.reg.entities;


import com.lv.reg.formBean.CustomerUserForm;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String orgName;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String region;
    private String adress;

    public Customer(CustomerUserForm customerUserForm) {
        this.orgName = resolveOrgName(customerUserForm);
        this.firstName = customerUserForm.getFirstName();
        this.lastName = customerUserForm.getLastName();
        this.email = customerUserForm.getEmail();
        this.adress = customerUserForm.getAdress();
        this.region = customerUserForm.getRegion();
        this.phone = customerUserForm.getPhone();
    }

    private String resolveOrgName(CustomerUserForm customerUserForm) {
        if (customerUserForm.getOrgName() == null || customerUserForm.getOrgName().isEmpty())
            return customerUserForm.getLastName() + "_" + customerUserForm.getFirstName();
        else
            return customerUserForm.getOrgName();
    }
}
