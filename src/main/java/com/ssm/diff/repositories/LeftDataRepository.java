package com.ssm.diff.repositories;

import com.ssm.diff.domain.LeftData;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LeftDataRepository extends CrudRepository<LeftData, Long> {
}
