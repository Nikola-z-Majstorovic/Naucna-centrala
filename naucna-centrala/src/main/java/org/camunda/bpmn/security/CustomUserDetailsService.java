package org.camunda.bpmn.security;

import org.camunda.bpmn.model.User;
import org.camunda.bpmn.repository.UserRepository;
//import org.camunda.bpmn.model.User;
//import org.camunda.bpmn.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
	
	@Autowired
	private UserRepository userRepo;
	

	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user =  userRepo.findOneByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));
		} else {
			return user;

		}
	}
}
