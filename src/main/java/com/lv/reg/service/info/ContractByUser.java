package com.lv.reg.service.info;

import com.lv.reg.entities.Contract;
import com.lv.reg.entities.User;
import lombok.Getter;

import java.util.List;

@Getter
public class ContractByUser {
    private User user;
    private long numberOfContractsAssigned;
    private long numberOfContractsFinished;
    private double totaPrice;
    private double payedAmount;

    public ContractByUser(User key, List<Contract> value) {
        this.user = key;

        numberOfContractsAssigned = value.stream().filter(c -> !c.isFinished()).count();
        numberOfContractsFinished = value.stream().filter(c -> c.isFinished()).count();
        totaPrice = value.stream().filter(c -> !c.isFinished())
                .mapToDouble(el -> el.getTotalPrice()).sum();
        payedAmount = value.stream().filter(c -> !c.isFinished())
                .mapToDouble(el -> el.getPayedAmount()).sum();
    }
}
