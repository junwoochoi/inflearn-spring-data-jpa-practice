package com.ingang.datajpa.repository;

import com.ingang.datajpa.entity.Team;
import org.hibernate.annotations.BatchSize;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TeamRepository extends JpaRepository<Team, Long> {
    Team findByName(String name);

    @Query("select t from Team t join fetch t.members m join fetch m.address where t.name = :name")
    Team findByNameWithMemberAndAddress(@Param("name") String name);

    @Query("select t from Team t where t.name = :name")
    Team findByNameBatchSize(@Param("name") String name);
}
