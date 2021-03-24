package com.org.aci.stream.service.dta.repo;
import com.org.aci.stream.service.model.ETLValResult;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ETLValResultSetRepo extends CrudRepository<ETLValResult, Long> {
}
