package com.lv.reg.service;

import com.lv.reg.dao.ContractDictionaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DictionaryService {
    @Autowired
    ContractDictionaryRepository contractDictionaryRepository;

    public List<String> regions(){
        return contractDictionaryRepository.findUniqueRegions();
    }

    public List<String> districts(){
        return contractDictionaryRepository.findUniqueDistricts();
    }

    public List<String> villages(){
        return contractDictionaryRepository.findUniqueVillages();
    }
}
