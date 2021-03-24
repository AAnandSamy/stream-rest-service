package com.org.aci.stream.service.dta.repo;
import com.org.aci.stream.service.model.ETLValCriteria;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.Size;
import java.util.List;

@Repository
public interface ETLValCriteriaRepo extends CrudRepository<ETLValCriteria, String> {

    List<ETLValCriteria> findAll();



}
