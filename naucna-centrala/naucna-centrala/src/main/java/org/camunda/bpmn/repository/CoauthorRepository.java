package org.camunda.bpmn.repository;

import org.camunda.bpmn.model.Coauthor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoauthorRepository extends JpaRepository<Coauthor,Long> {

    Coauthor findOneById(Long id);
}
