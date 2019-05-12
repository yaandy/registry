package com.lv.reg.entities;


import com.lv.reg.formBean.CustomerUserForm;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
    private String district;
    private String address;
    private String comment;
    @OneToMany(mappedBy = "customer")
    private Collection<Contract> contracts;

    public Customer(CustomerUserForm customerUserForm) {
        this.orgName = resolveOrgName(customerUserForm);
        this.firstName = customerUserForm.getFirstName();
        this.lastName = customerUserForm.getLastName();
        this.email = customerUserForm.getEmail();
        this.address = customerUserForm.getAddress();
        this.region = customerUserForm.getRegion();
        this.district = customerUserForm.getDistrict();
        this.phone = customerUserForm.getPhone();
    }

    private String resolveOrgName(CustomerUserForm customerUserForm) {
        if (customerUserForm.getOrgName() == null || customerUserForm.getOrgName().isEmpty())
            return customerUserForm.getLastName() + "_" + customerUserForm.getFirstName();
        else
            return customerUserForm.getOrgName();
    }
}
