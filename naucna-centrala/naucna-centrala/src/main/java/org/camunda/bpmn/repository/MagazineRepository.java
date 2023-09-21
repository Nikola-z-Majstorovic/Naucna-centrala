package org.camunda.bpmn.repository;

import org.apache.ibatis.annotations.Param;
import org.camunda.bpmn.model.Magazine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MagazineRepository extends JpaRepository<Magazine, Long>{

    Magazine findByName(String name);

    @Query("select magazine from Magazine magazine where magazine.chiefEditor.username = :username")
    List<Magazine> findAllByChiefEditor(@Param("username") String username);

}
