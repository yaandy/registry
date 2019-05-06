package com.lv.reg.dao;

import com.lv.reg.entities.Contract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ContractDictionaryRepository extends JpaRepository<Contract, Long> {
    @Query("SELECT distinct region FROM Contract")
    List<String> findUniqueRegions();

    @Query("SELECT distinct district FROM Contract")
    List<String> findUniqueDistricts();

    @Query("SELECT distinct villageCouncil FROM Contract")
    List<String> findUniqueVillages();
}
