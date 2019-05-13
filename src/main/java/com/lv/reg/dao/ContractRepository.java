package com.lv.reg.dao;

import com.lv.reg.entities.Contract;
import com.lv.reg.entities.Stage;
import com.lv.reg.entities.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface ContractRepository extends CrudRepository<Contract, Long> {
    Collection<Contract> findContractByCreatedBy(User user);
    Collection<Contract> findContractByAssignedTo(User user);
    Collection<Contract> findContractsByStage(String stage);

    @Query("SELECT c  FROM Contract c where assignedTo = ?1 and (not(isFinished = ?2) or isFinished = ?3)")
    Collection<Contract> findContractsByAssignedToAndFinishedStatus(User user, boolean showActive, boolean showFinished);

    @Query("SELECT c  FROM Contract c where (not(isFinished=?1) or isFinished=?2)")
    Collection<Contract> findContractsByFinishedStatus( boolean showActive, boolean showFinished);
}
