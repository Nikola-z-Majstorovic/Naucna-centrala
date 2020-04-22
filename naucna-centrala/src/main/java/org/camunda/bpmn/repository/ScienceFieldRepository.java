package org.camunda.bpmn.repository;


import org.camunda.bpmn.model.ScienceField;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScienceFieldRepository extends JpaRepository<ScienceField, Long> {

    ScienceField findOneByName(String fieldValue);
}
