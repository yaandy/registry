package com.lv.reg.service.info;

import com.lv.reg.entities.Contract;
import com.lv.reg.entities.User;
import com.lv.reg.formBean.ContractForm;
import com.lv.reg.service.ContractService;
import org.glassfish.jersey.internal.util.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
public class InfoService {
    @Autowired
    ContractService contractService;

    public Map<User, ContractByUser> getUserContractStat(Principal principal) {
        Map<User, List<Contract>> initialCollect = findAllAvailableForUser(principal).collect(Collectors.groupingBy(s -> s.getAssignedTo()));

        return initialCollect.entrySet().stream().map(el -> new ContractByUser(el.getKey(), el.getValue()))
                .collect(Collectors.toMap(el -> el.getUser(), el -> el));
    }

    public Map<String, Long> getContractsByStatus(Principal principal){
        return findAllAvailableForUser(principal).map(el -> el.getOrderStatus())
            .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    }

    public Map<String, Long> getContractsByStage(Principal principal){
        return findAllAvailableForUser(principal).map(el -> el.getStage())
            .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    }


    private Stream<Contract> findAllAvailableForUser(Principal principal){
        return StreamSupport.stream(contractService.findAllAvailableForUser(principal).spliterator(), false);
    }

}
