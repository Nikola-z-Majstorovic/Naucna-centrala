package org.camunda.bpmn.service;

import org.camunda.bpmn.model.ScienceField;
import org.camunda.bpmn.repository.ScienceFieldRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScienceFieldService {

    @Autowired
    private ScienceFieldRepository scienceFieldRepositor;

    public List<ScienceField> findAll() {
        return scienceFieldRepositor.findAll();
    }
}
