package com.lv.reg.dao;

import com.lv.reg.entities.Contract;
import com.lv.reg.entities.ContractLog;
import org.springframework.data.repository.CrudRepository;

public interface ContractLogRepository extends CrudRepository<ContractLog, Long> {
}
