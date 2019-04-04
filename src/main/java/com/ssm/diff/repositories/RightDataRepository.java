package com.ssm.diff.repositories;

import com.ssm.diff.domain.RightData;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RightDataRepository extends CrudRepository<RightData, Long> {
}
