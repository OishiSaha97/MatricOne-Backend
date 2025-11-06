package com.datasoft.luncheon.weekend;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WeekendRepository extends JpaRepository<Weekend, Integer> {


    List<Weekend> findAllByOrgId(Integer currentOrgId);

    void deleteByOrgId(Integer orgId);
}
