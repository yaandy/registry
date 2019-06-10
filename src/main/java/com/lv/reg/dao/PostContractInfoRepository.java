package com.lv.reg.dao;

import com.lv.reg.entities.Authority;
import com.lv.reg.entities.AuthorityType;
import com.lv.reg.entities.PostContractInfo;
import org.springframework.data.repository.CrudRepository;

public interface PostContractInfoRepository extends CrudRepository<PostContractInfo, Integer> {

}
