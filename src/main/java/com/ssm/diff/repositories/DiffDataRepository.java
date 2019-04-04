package com.ssm.diff.repositories;

import com.ssm.diff.domain.DiffData;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiffDataRepository extends CrudRepository<DiffData, Long> {
}
