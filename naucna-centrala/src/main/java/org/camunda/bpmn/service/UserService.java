package org.camunda.bpmn.service;

import org.camunda.bpmn.model.User;
import org.camunda.bpmn.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

	@Autowired
	UserRepository userRepo;
	
	public User findOneByEmailAndPassword(String username,String password) {
	        return userRepo.findOneByEmailAndPassword(username,password);
	}

    public User findOneByUsername(String username){
        return userRepo.findOneByUsername(username);
    }

}
