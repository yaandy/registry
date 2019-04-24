package com.lv.reg.service;

import com.lv.reg.dao.ContractLogRepository;
import com.lv.reg.dao.ContractRepository;
import com.lv.reg.dao.CustomerRepository;
import com.lv.reg.dao.StageRepository;
import com.lv.reg.entities.Contract;
import com.lv.reg.entities.ContractLog;
import com.lv.reg.entities.Stage;
import com.lv.reg.entities.User;
import com.lv.reg.formBean.ContractForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.sql.Date;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.lv.reg.entities.AuthorityType.ROLE_ADMIN;
import static com.lv.reg.entities.AuthorityType.ROLE_GEODEZ;

@Service
public class ContractService {
    @Autowired
    private ContractRepository contractRepository;
    @Autowired
    private StageRepository stageRepository;
    @Autowired
    private ContractLogRepository contractLogRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    @Qualifier("userDetailsService")
    private UserDetailsService userDetailsService;

    public void saveContract(ContractForm contractForm, Principal principal) {
        User user = ((MyUserDetails) userDetailsService.loadUserByUsername(principal.getName())).getUser();

        Contract contract = Contract.builder().customer(customerRepository.findById(contractForm.getCustomerId()).orElseThrow())
                .district(contractForm.getDistrict())
                .region(contractForm.getRegion())
                .registered(Date.valueOf(LocalDate.now()))
                .villageCouncil(contractForm.getVillageCouncil())
                .updated(Date.valueOf(LocalDate.now()))
                .orderStatus(contractForm.getStatus())
                .orderType(contractForm.getType())
                .stage(contractForm.getStage())
                .totalPrice(contractForm.getTotalPrice())
                .createdBy(user)
                .assignedTo(user)
                .build();

        Contract saved = contractRepository.save(contract);
        ContractLog contractLog = ContractLog.builder().contract(saved)
                .message(String.format("Contract created by %s on %s", user.getUsername(), LocalDate.now().toString()))
                .build();
        contractLogRepository.save(contractLog);
    }

    public void updateContract(ContractForm contractForm, long id) {
        Contract toBeUpdated = contractRepository.findById(id).orElseThrow();

        ContractLog contractLog = ContractLog.builder().contract(toBeUpdated)
                .message(getChangeLog(contractForm, toBeUpdated)).build();
        contractLogRepository.save(contractLog);

        toBeUpdated.setFinished(contractForm.isFinished());
        toBeUpdated.setOrderStatus(contractForm.getStatus());
        toBeUpdated.setStage(contractForm.getStage());
        toBeUpdated.setPayedAmount(toBeUpdated.getPayedAmount() + contractForm.getPayedAmount());
        toBeUpdated.setUpdated(Date.valueOf(LocalDate.now()));

        contractRepository.save(toBeUpdated);
    }

    public Iterable<Contract> findAll() {
        return contractRepository.findAll();
    }

    public Iterable<Contract> findAllAvailableForUser(Principal principal) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
        List<String> currentUserAuthorities = userDetails.getAuthorities().stream()
                .map(el -> ((GrantedAuthority) el).getAuthority())
                .collect(Collectors.toList());

        if (currentUserAuthorities.contains(ROLE_ADMIN.name())) {
            return setAfterLastUpdatedPeriod(contractRepository.findAll());
        } else if (currentUserAuthorities.contains(ROLE_GEODEZ.name())) {
            Stage geodezAvailableStage = StreamSupport.stream(stageRepository.findAll().spliterator(), false)
                    .filter(stage -> stage.getLabel().equalsIgnoreCase("Зйомка"))
                    .findAny().orElse(new Stage(111, "xxx", "xxx"));
            return setAfterLastUpdatedPeriod(contractRepository.findContractsByStage(geodezAvailableStage.getLabel()));
        } else {
            return setAfterLastUpdatedPeriod(contractRepository.findContractByCreatedBy(((MyUserDetails) userDetails).getUser()));
        }
    }

    public Contract findById(long id) {
        Contract contract = contractRepository.findById(id).orElseThrow();
        calculatePeriodAndSet().accept(contract);
        return contract;
    }

    private String getChangeLog(ContractForm form, Contract contract) {
        StringBuilder stringBuilder = new StringBuilder("Updated: ");
        if (!form.getStatus().equals(contract.getOrderStatus()))
            stringBuilder.append("status updated to ").append(form.getStatus());
        if (!form.getStage().equals(contract.getStage()))
            stringBuilder.append(", stage updated to ").append(form.getStage());
        if (!(form.getPayedAmount() > 0))
            stringBuilder.append(", payed amount set to ").append(form.getPayedAmount());
        if (!form.getAssignedTo().equalsIgnoreCase(contract.getAssignedTo().getUsername())) {
            stringBuilder.append(" , assigned to changed from ")
                    .append(contract.getAssignedTo().getUsername())
                    .append(" to ").append(form.getAssignedTo());
        }
        return stringBuilder.toString();
    }

    private Iterable<Contract> setAfterLastUpdatedPeriod(Iterable<Contract> contracts) {
        contracts.forEach(calculatePeriodAndSet());
        return contracts;
    }

    private Consumer<Contract> calculatePeriodAndSet() {
        return c -> {
            Date updatedDate = Optional.ofNullable(c.getUpdated()).orElse(c.getRegistered());
            c.setPassedDaysAfterLastUpdated(Period.between(LocalDate.now(), updatedDate.toLocalDate()).getDays());
        };
    }


}
