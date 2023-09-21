package org.camunda.bpmn.repository;

import org.camunda.bpmn.model.SciencePaper;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SciencePaperRepository extends JpaRepository<SciencePaper, Long> {

    SciencePaper findOneById(Long sciencePaperId);
}
