package org.camunda.bpmn.service;

import org.camunda.bpmn.model.Reviewer;
import org.camunda.bpmn.model.User;
import org.camunda.bpmn.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

	@Autowired
	UserRepository userRepo;
	
	public User findOneByEmailAndPassword(String username,String password) {
	        return userRepo.findOneByEmailAndPassword(username,password);
	}

    public User findOneByUsername(String username){
		User user = userRepo.findOneByUsername(username);
        return user;
    }

    public List<User> findAllReviewers() {
		List<User> reviewers = new ArrayList<>();
		List<User> users = userRepo.findAll();
		for(User user: users){
			if(user instanceof Reviewer){
				reviewers.add((Reviewer) user);
			}
		}
		return reviewers;
    }
}
