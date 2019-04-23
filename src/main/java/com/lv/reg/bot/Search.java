package com.lv.reg.bot;

import com.lv.reg.entities.Contract;
import com.lv.reg.service.ContractService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

public interface Search {
    Iterable<Contract> performSearch(String query);

    @AllArgsConstructor
    @Component
    class SearchFactory{
        private ByEmailSearch byEmailSearch;

        Search getSearch(SearchStage.SearchBy searchBy){
            switch (searchBy){
                case NAME: return byEmailSearch;
                case PHONE: return byEmailSearch;
                default: return byEmailSearch;
            }
        }
    }

    @Component
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    class ByEmailSearch implements Search{
        @Autowired
        private ContractService contractService;

        @Override
        public Iterable<Contract> performSearch(String query) {
            return contractService.findAll();
        }
    }
}
