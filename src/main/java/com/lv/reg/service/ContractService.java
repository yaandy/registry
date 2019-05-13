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
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
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

    public Contract saveContract(ContractForm contractForm, Principal principal) {
        User user = ((MyUserDetails) userDetailsService.loadUserByUsername(principal.getName())).getUser();

        Contract contract = Contract.builder().customer(customerRepository.findById(contractForm.getCustomerId()).orElseThrow(() -> new EntityNotFoundException()))
                .district(contractForm.getDistrict())
                .region(contractForm.getRegion())
                .registered(Date.valueOf(LocalDate.now()))
                .villageCouncil(contractForm.getVillageCouncil())
                .square(contractForm.getSquare())
                .updated(Date.valueOf(LocalDate.now()))
                .orderStatus(contractForm.getStatus())
                .orderType(contractForm.getType())
                .stage(contractForm.getStage())
                .totalPrice(contractForm.getTotalPrice())
                .createdBy(user)
                .assignedTo(user)
                .comment(contractForm.getComment())
                .build();

        Contract saved = contractRepository.save(contract);
        saved.setContractIdentifier(generateContractIdentifier(saved));
        Contract withIdentifier = contractRepository.save(saved);
        ContractLog contractLog = ContractLog.builder().contract(saved)
                .message(String.format("Contract created by %s on %s", user.getUsername(), LocalDate.now().toString()))
                .build();
        contractLogRepository.save(contractLog);
        return withIdentifier;
    }

    public Contract updateContract(ContractForm contractForm, long id) {
        Contract toBeUpdated = contractRepository.findById(id).orElseThrow(() -> new EntityNotFoundException());

        ContractLog contractLog = ContractLog.builder().contract(toBeUpdated)
                .message(generateChangeLog(contractForm, toBeUpdated)).build();
        contractLogRepository.save(contractLog);

        if (!contractForm.getStage().equals(toBeUpdated.getStage()) ||
                !contractForm.getStatus().equals(toBeUpdated.getOrderStatus())) {
            toBeUpdated.setUpdated(Date.valueOf(LocalDate.now()));
        }

        toBeUpdated.setFinished(contractForm.isFinished());
        toBeUpdated.setOrderStatus(contractForm.getStatus());
        toBeUpdated.setStage(contractForm.getStage());
        toBeUpdated.setPayedAmount(toBeUpdated.getPayedAmount() + contractForm.getPayedAmount());
        toBeUpdated.setTotalCosts(toBeUpdated.getTotalCosts() + contractForm.getTotalCosts());
        toBeUpdated.setAssignedTo(((MyUserDetails) userDetailsService.loadUserByUsername(contractForm.getAssignedTo())).getUser());
        toBeUpdated.setComment(contractForm.getComment());
        return contractRepository.save(toBeUpdated);
    }

    public void closeContract(long id) {
        Contract toBeUpdated = contractRepository.findById(id).orElseThrow(() -> new EntityNotFoundException());
        toBeUpdated.setFinished(true);
        contractRepository.save(toBeUpdated);
    }

    public void deleteContract(long id){
        Optional<Contract> contract = contractRepository.findById(id);
        if(contract.isPresent()){
            contractLogRepository.deleteAll(contract.get().getLog());
            contractRepository.delete(contract.get());
        }
    }

    public Iterable<Contract> findAll() {
        return contractRepository.findAll();
    }

    public Iterable<Contract> findAllAvailableForUser(Principal principal, boolean showActive, boolean showArchived) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
        List<String> currentUserAuthorities = userDetails.getAuthorities().stream()
                .map(el -> ((GrantedAuthority) el).getAuthority())
                .collect(Collectors.toList());

        if (currentUserAuthorities.contains(ROLE_ADMIN.name())) {
            return setAfterLastUpdatedPeriod(contractRepository.findContractsByFinishedStatus(showActive, showArchived));
        } else if (currentUserAuthorities.contains(ROLE_GEODEZ.name())) {
            Stage geodezAvailableStage = StreamSupport.stream(stageRepository.findAll().spliterator(), false)
                    .filter(stage -> stage.getLabel().equalsIgnoreCase("Зйомка"))
                    .findAny().orElse(new Stage(111, "xxx", "xxx"));
            return setAfterLastUpdatedPeriod(contractRepository.findContractsByStage(geodezAvailableStage.getLabel()));
        } else {
            return setAfterLastUpdatedPeriod(contractRepository.findContractsByAssignedToAndFinishedStatus(((MyUserDetails) userDetails).getUser(), showActive, showArchived));
        }
    }

    public Contract findById(long id) {
        Contract contract = contractRepository.findById(id).orElseThrow(() -> new EntityNotFoundException());
        calculatePeriodAndSet().accept(contract);
        return contract;
    }

    private String generateChangeLog(ContractForm form, Contract contract) {
        StringBuilder stringBuilder = new StringBuilder(LocalDate.now().toString() + " Updated: ");
        if (!form.getStatus().equals(contract.getOrderStatus())) {
            stringBuilder.append("status updated to ").append(form.getStatus());
        }
        if (!form.getStage().equals(contract.getStage())) {
            stringBuilder.append(", stage updated to ").append(form.getStage());
        }
        if (form.getPayedAmount() > 1) {
            stringBuilder.append(", payed amount set to ").append(form.getPayedAmount());
        }
        if (!form.getAssignedTo().equalsIgnoreCase(contract.getAssignedTo().getUsername())) {
            stringBuilder.append(" , assigned to changed from ")
                    .append(contract.getAssignedTo().getUsername())
                    .append(" to ").append(form.getAssignedTo());
        }
        if(form.getTotalCosts() > 0.5){
            stringBuilder.append(", costs added to total costs ").append(form.getTotalCosts());
        }
        if(form.getCustomerDocument().stream().filter(el -> !el.isEmpty()).count() > 0){
            stringBuilder.append(", some attachments added");
        }
        if(form.isFinished()){
            stringBuilder.append(", Finished !");
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
            Period period = Period.between(LocalDate.now(), updatedDate.toLocalDate());
            c.setPassedDaysAfterLastUpdated(Math.toIntExact(period.getDays() + period.getMonths() * 30)); // aroximate month calc
        };
    }

    private String generateContractIdentifier(Contract contract){
        //FL_PHONE2DIGITS_NUMBER_MOTH_DATE
        String registeredBy = StringUtils.left(contract.getCreatedBy().getUsername(), 4).toUpperCase();
        Long id = contract.getId();
        LocalDate registered = contract.getRegistered().toLocalDate();
        int month = registered.getMonth().getValue();
        int day = registered.getDayOfMonth();
        return String.format(String.format("@%s_#%d_m%d_d%d", registeredBy, id.intValue(), month, day));

    }


}
